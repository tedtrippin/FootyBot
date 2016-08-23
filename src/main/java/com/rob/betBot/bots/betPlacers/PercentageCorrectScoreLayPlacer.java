package com.rob.betBot.bots.betPlacers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rob.betBot.Bet;
import com.rob.betBot.BetBotConstants;
import com.rob.betBot.BetManager;
import com.rob.betBot.BetRequest;
import com.rob.betBot.BetRequestImpl;
import com.rob.betBot.BetTypeEnum;
import com.rob.betBot.Event;
import com.rob.betBot.Market;
import com.rob.betBot.MarketPrices;
import com.rob.betBot.MarketType;
import com.rob.betBot.Runner;
import com.rob.betBot.bots.modules.ModulePropertyImpl;
import com.rob.betBot.bots.modules.ModulePropertyType;
import com.rob.betBot.bots.modules.PropertyNames;
import com.rob.betBot.conf.Property;
import com.rob.betBot.conf.PropertyKey;
import com.rob.betBot.exception.BetCancelException;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.NoSuchMarketException;

/**
 * Places lay bets based on all the correct score markets at
 * a percentage of the exchange odds. If price changes then
 * the bet is cancelled and re-layed at the new odds. If part
 * of the bet was matched then lays the remainder.
 *
 * @author robert.haycock
 *
 */
public class PercentageCorrectScoreLayPlacer extends AbstractBetPlacer {

    private static Logger journal = LogManager.getLogger("journal");
    private final static Logger log = LogManager.getLogger(PercentageCorrectScoreLayPlacer.class);
    private final static String desc = "Lays off at a percentage of the exchange odds. Eg. if percentage=90 "
        + ", lay amount=100 and price=10 then places lay bet of £100 at price of 9. Bet is updated when odds change";

    @XmlTransient private Collection<Bet> currentBets = new ArrayList<>(); // Track bets made after last betting round
    @XmlTransient private Collection<Bet> betsToRedo = new ArrayList<>(); // Bets that need to be "re-done" as price changed
    @XmlTransient private Map<Long, Double> runnerAmountLayedMap = new HashMap<>(); // Tracks total amount layed against a runner
    @XmlTransient private boolean betsCancelled = false;

    public PercentageCorrectScoreLayPlacer() {
        super("2", "Winner percentage layer better", desc,
            new ModulePropertyImpl(PropertyNames.LAY_AMOUNT, ModulePropertyType.NUMBER, "Lay amount"),
            new ModulePropertyImpl(PropertyNames.MAX_PRICE, ModulePropertyType.NUMBER, "Max price"),
            new ModulePropertyImpl(PropertyNames.PERCENTAGE, ModulePropertyType.NUMBER, "Percentage of price"));
    }

    @Override
    public Collection<Bet> placeBets(Event event, BetManager betManager)
        throws ExchangeException, BetPlaceException {

        if (betsCancelled)
            return Collections.emptyList();

        long expectedEndTime = event.getEventData().getExpectedStartTime() + BetBotConstants.FOOTBALL_MATCH_DURATION_MS;
        long timeLeft = expectedEndTime - System.currentTimeMillis();
        if (timeLeft > 0 && timeLeft < getWhenToStop()) {
            cancelBets(betManager);
            return Collections.emptyList();
        }

        if (!canPlaceBet(event))
            return Collections.emptyList();

        if (log.isDebugEnabled())
            log.debug("Laying on[race:" + event + "]");

        if (currentBets.isEmpty()) {
            makeInitialBets(event, betManager);
        } else {
            redoBets(event, betManager);
        }

        return new ArrayList<>(currentBets);
    }

    private boolean canPlaceBet(Event event) {

        // If not placed a bet yet, return true
        if (currentBets.isEmpty())
            return true;

        // Otherwise, check if fully matched or odds have changed
        betsToRedo = new ArrayList<>();
        for (Bet bet : currentBets) {

            if (bet.getMatchedSoFar() == bet.getAmount())
                continue;

            long runnerId;
            String marketId;
            if (bet.getPricesInfo() == null) {
                runnerId = bet.getRunnerId();
                marketId = bet.getMarketId();
            } else {
                runnerId = bet.getPricesInfo().getRunnerId();
                marketId = bet.getPricesInfo().getMarketId();
            }

            Market market = event.getMarket(marketId);
            MarketPrices marketPrices = market.getLatestPrices();
            double currentPrice = marketPrices.getPrice(runnerId);

            if (bet.getExchangePrice() == currentPrice)
                continue;

            String runnerName = market.getRunner(runnerId).getRunnerData().getRunnerName();
            journal.info("Price for runner[" + runnerName + "] changed on WH, was[" + bet.getExchangePrice() + "] now[" + currentPrice + "]");

            log.debug(" Marking bet for redo[" + bet + "]");
            betsToRedo.add(bet);
        }

        return !betsToRedo.isEmpty();
    }

    /**
     * Lays off against all runners.
     *
     * @param event
     * @param betManager
     * @throws ExchangeException
     * @throws BetPlaceException
     */
    private void makeInitialBets(Event event, BetManager betManager)
        throws ExchangeException, BetPlaceException {

        if (log.isDebugEnabled())
            log.debug("Placing initial bets for event[" + event + "]");

        currentBets = new ArrayList<>();

        Collection<BetRequest> betRequests = new ArrayList<>();
        Market market;
        try {
            market = event.getMarket(MarketType.CORRECT_SCORE);
        } catch (NoSuchMarketException ex) {
            throw new ExchangeException("Couldnt find CORRECT_SCORE market", ex);
        }

        if (market.getLatestPrices() == null) {
            log.warn("No prices for market[" + market.getMarketData().getMarketName() + "] event[" + event.getEventData().getEventName() + "]");
            return;
        }

        if (market.getRunners().size() == 0) {
            log.warn("No runners for market[" + market.getMarketData().getMarketName() + "] event[" + event.getEventData().getEventName() + "]");
            return;
        }

        for (Runner runner : market.getRunners()) {
            long runnerId = runner.runnerData.getExchangeRunnerId();
            double exchangePrice = market.getLatestPrices().getPrice(runnerId);

            // Calculate price to lay
            double modifiedPrice = getModifiedPrice(exchangePrice);

            // round down to "valid" price
            modifiedPrice = roundPrice(modifiedPrice);

            double amount = getLayAmount();
            BetRequest bet = new BetRequestImpl(modifiedPrice, exchangePrice, amount,
                event.getEventData().getExchangeEventId(),
                event.getEventData().getEventName(), market.getMarketData().getExchangeMarketId(),
                market.getMarketData().getMarketType(), runner.getRunnerData().getExchangeRunnerId(),
                runner.getRunnerData().getRunnerName(), BetTypeEnum.LAY);
            betRequests.add(bet);

            if (log.isDebugEnabled())
                log.debug(bet);
        }

        Collection<Bet> bets = betManager.placeBets(betRequests);
        for (Bet bet : bets) {
            currentBets.add(bet);
            runnerAmountLayedMap.put(bet.getRunnerId(), bet.getAmount());

            log.debug("Adding to runner map, runnerId[" + bet.getRunnerId() + "] bet.amount[" + bet.getAmount() + "]");

            if (bet.getAmount() == 0)
                log.error("bet.amount is zero");
        }
    }

    /**
     * Cancels bets and re-lays for bets where the price has
     * changed. If any bet was part-matched it calculates how
     * much is remaining for that bet.
     *
     * @param event
     * @param betManager
     * @throws ExchangeException
     * @throws BetCancelException
     * @throws BetPlaceException
     */
    private void redoBets(Event event, BetManager betManager)
        throws ExchangeException, BetPlaceException {

        if (log.isDebugEnabled())
            log.debug("Re-doing bets for event[" + event + "]");

        try {
            cancelBets(betsToRedo, betManager);

            Collection<BetRequest> betRequests = new ArrayList<>();
            for (Bet bet : betsToRedo) {
                if (log.isDebugEnabled())
                    log.debug("  cancelling/relaying bet[" + bet + "]");

                // If bet prices come from different exchange make sure we're using the right ones
                long runnerId;
                String marketId;
                if (bet.getPricesInfo() == null) {
                    runnerId = bet.getRunnerId();
                    marketId = bet.getMarketId();
                } else {
                    runnerId = bet.getPricesInfo().getRunnerId();
                    marketId = bet.getPricesInfo().getMarketId();
                }

                // Calculate how much to lay
                double amountMatchedSoFar = runnerAmountLayedMap.get(bet.getRunnerId()); // Map uses the bet exchange runner ID
                double amountRemainingToLay = getLayAmount() - amountMatchedSoFar;
                if (amountRemainingToLay < getMinimumBetAmount())
                    continue;

                // Calculate new price to lay
                Market market = event.getMarket(marketId);
                double newPrice = market.getLatestPrices().getPrice(runnerId); // we use other runner ID for accessing market
                double modifiedPrice = getModifiedPrice(newPrice);

                // round down to "valid" price
                modifiedPrice = roundPrice(modifiedPrice);

                BetRequest betRequest = new BetRequestImpl(modifiedPrice, newPrice, amountRemainingToLay,
                    bet.getEventId(), bet.getEventName(), marketId, bet.getMarketType(), runnerId,
                    bet.getRunnerName(), BetTypeEnum.LAY);

                betRequests.add(betRequest);
            }

            Collection<Bet> newBets = betManager.placeBets(betRequests);
            currentBets.addAll(newBets);

        } catch (BetCancelException ex) {
            log.error("Couldn't cancel bets", ex);
        }
    }

    /**
     * Cancels bet and updates how much is left for new bet
     * (in the case where bet was part-matched).
     *
     * @param bet
     * @throws BetCancelException
     * @throws ExchangeException
     */
    private void cancelBets(Collection<Bet> bets, BetManager betManager)
        throws ExchangeException, BetCancelException {

        Collection<Bet> partMatchedBets = betManager.cancelBets(bets);
        currentBets.removeAll(bets);

        Map<String, Bet> partMatchedBetsMap = new HashMap<>();
        partMatchedBets.forEach(b -> partMatchedBetsMap.put(b.getBetId(), b));

        for (Bet bet : bets) {

            Bet partMatchedBet = partMatchedBetsMap.get(bet.getBetId());

            if (partMatchedBet == null) {
                // Keep track of how much is layed, ie. subtract the cancelled bet amount
                double amountLayed = runnerAmountLayedMap.get(bet.getRunnerId());
                double newAmountLayed = amountLayed - bet.getAmount();
                if (newAmountLayed < 0) {
                    log.error("newAmountLayed less than zero, amountLayed[" + amountLayed + "] betAmount[" + bet.getAmount() + "]");
                    log.error("  runnerId[" + bet.getRunnerId() + "], layed map[" + runnerAmountLayedMap + "]");
                    newAmountLayed = 0;
                }
                runnerAmountLayedMap.put(bet.getRunnerId(), newAmountLayed);
                continue;
            }

            currentBets.add(partMatchedBet);

            if (runnerAmountLayedMap.containsKey(bet.getRunnerId())) {
                double amount = runnerAmountLayedMap.get(bet.getRunnerId());
                double newAmount = amount + partMatchedBet.getAmount();
                if (newAmount < 2) {
                    log.warn("newAmount < 2, amount[" + amount + "] + partMatchedBet.amount[" + partMatchedBet.getAmount() + "]");
                    log.warn("  runnerId[" + bet.getRunnerId() + "], layed map[" + runnerAmountLayedMap + "]");
                }
                runnerAmountLayedMap.put(bet.getRunnerId(), newAmount);

            } else {
                if (bet.getAmount() < 2)
                    log.warn("bet.amount < 2 [" + bet.getAmount() + "]");

                runnerAmountLayedMap.put(bet.getRunnerId(), bet.getAmount());
            }
        }
    }

    private void cancelBets(BetManager betManager) {

        log.debug("10 minutes left, cancelling bets");
        betsCancelled = true;

        try {
            cancelBets(currentBets, betManager);
        } catch (Exception ex) {
            log.error("Couldn't cancel bets", ex);
        }
    }

    private double getModifiedPrice(double price) {

        double newPrice =((double)getPercentage())/100 * price;
        if (newPrice < 1.01)
            return 1.01;

        double maxPrice = getMaxPrice();
        if (maxPrice > 1 && newPrice > maxPrice)
            return maxPrice;

        return newPrice;
    }

    private double getMinimumBetAmount() {
        // TODO: Make a system/exchange property
        return 2;
    }

    private double getLayAmount() {
        return getPropertyAsDouble(PropertyNames.LAY_AMOUNT);
    }

    private double getMaxPrice() {
        return getPropertyAsDouble(PropertyNames.MAX_PRICE);
    }

    private int getPercentage() {
        return getPropertyAsInt(PropertyNames.PERCENTAGE);
    }

    private long getWhenToStop() {
        return Property.getPropertyAsLong(PropertyKey.FOOTBALL_LAYING_WHEN_TO_STOP_MINUTES) * 60 * 1000;
    }
}
