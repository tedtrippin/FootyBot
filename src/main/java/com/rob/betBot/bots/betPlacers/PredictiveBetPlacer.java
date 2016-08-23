package com.rob.betBot.bots.betPlacers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rob.betBot.Bet;
import com.rob.betBot.BetManager;
import com.rob.betBot.BetRequest;
import com.rob.betBot.BetRequestImpl;
import com.rob.betBot.BetTypeEnum;
import com.rob.betBot.Event;
import com.rob.betBot.Market;
import com.rob.betBot.MarketPrices;
import com.rob.betBot.MarketType;
import com.rob.betBot.PricePredictor;
import com.rob.betBot.PricesHelper;
import com.rob.betBot.Runner;
import com.rob.betBot.bots.modules.ModulePropertyImpl;
import com.rob.betBot.bots.modules.ModulePropertyType;
import com.rob.betBot.bots.modules.PropertyNames;
import com.rob.betBot.conf.Property;
import com.rob.betBot.conf.PropertyKey;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.NoSuchMarketException;
import com.rob.betBot.model.EventData;
import com.rob.betBot.model.MarketData;

public class PredictiveBetPlacer extends AbstractBetPlacer {

    private final static Logger log = LogManager.getLogger(PredictiveBetPlacer.class);
    private final static String desc = "Patented predictive teechnology inc";

    // Properties
    private long numberOfPrices = Property.getPropertyAsLong(PropertyKey.NUMBER_OF_PRICES);
    private double priceDifferenceForPlacedBet = Property.getPropertyAsLong(PropertyKey.PRICE_DIFFERENCE_FOR_PLACED_BET);
    private double priceDifferenceForWinBet = Property.getPropertyAsLong(PropertyKey.PRICE_DIFFERENCE_FOR_WIN_BET);
    private double MAX_PRICE= 5;

    private List<MarketPrices> prices = new LinkedList<MarketPrices>();
    private List<BetRequest> betRequests;
    private Market winnerMarket = null;

    public PredictiveBetPlacer() {
        super("1", "BetBot patented predictor", desc,
            new ModulePropertyImpl(PropertyNames.BET_AMOUNT, ModulePropertyType.NUMBER, "Bet amount"));
    }

    @Override
    public Collection<Bet> placeBets(Event event, BetManager betManager)
        throws ExchangeException, BetPlaceException {

        long estimatedTimeLeft;
        try {
            estimatedTimeLeft = getEstimatedTimeLeft(event);
        } catch (NoSuchMarketException ex) {
            throw new ExchangeException("No WINNER market, couldn't work out 'time left'", ex);
        }

        if (event.isInPlay())
            log.debug("Time left: " + estimatedTimeLeft);

        if (!canPlaceBet(event, estimatedTimeLeft))
            return new ArrayList<Bet>();

        if (betRequests != null)
            return new ArrayList<Bet>();

        log.debug("Placing bets");

        PricePredictor pricePredictor = new PricePredictor();
        PricesHelper predictedPrices = pricePredictor.predict(winnerMarket, estimatedTimeLeft, prices);

        long firstPlaceRunner = predictedPrices.getSortedRunners()[0];
        long secondPlaceRunner = predictedPrices.getSortedRunners()[1];
        long thirdPlaceRunner = predictedPrices.getSortedRunners()[2];

        double firstPlacePrice = predictedPrices.getSortedPrice(0);
        double secondPlacePrice = predictedPrices.getSortedPrice(1);
        double thirdPlacePrice = predictedPrices.getSortedPrice(2);
        double fourthPlacePrice = predictedPrices.getSortedPrice(3);

        betRequests = new ArrayList<>();

        Market winMarket;
        Market toBePlacedMarket;
        try {
            winMarket = event.getMarket(MarketType.WINNER);
            toBePlacedMarket = event.getMarket(MarketType.TO_BE_PLACED);
        } catch (NoSuchMarketException ex) {
            log.error("Couldn't find markets", ex);
            return new ArrayList<Bet>();
        }

        // Determine whether to place WIN or PLACED bet on leader
        boolean goodToBet = true;
        if (secondPlacePrice - firstPlacePrice > priceDifferenceForWinBet) {
            double price = winMarket.getLatestPrices().getPrice(firstPlaceRunner);
            goodToBet &= tryBet(event, winMarket, firstPlaceRunner, price);

        } else if (fourthPlacePrice - firstPlacePrice > priceDifferenceForPlacedBet) {
            double price = toBePlacedMarket.getLatestPrices().getPrice(firstPlaceRunner);
            goodToBet &= tryBet(event, toBePlacedMarket, firstPlaceRunner, price);
        }

        // Determine whether we should place any more PLACED bets
        if (fourthPlacePrice - secondPlacePrice > priceDifferenceForPlacedBet) {
            double price = toBePlacedMarket.getLatestPrices().getPrice(secondPlaceRunner);
            goodToBet &= tryBet(event, toBePlacedMarket, secondPlaceRunner, price);

            // Check if there are enough PLACED winners
            int numberOfToBePlacedWinners = getNumberOfToBePlacedWinners(winMarket.getRunners().size());
            if (numberOfToBePlacedWinners >= 3) {
                // ...then check if its further enough ahead to warrant betting
                if (fourthPlacePrice - thirdPlacePrice > priceDifferenceForPlacedBet) {
                    price = toBePlacedMarket.getLatestPrices().getPrice(thirdPlaceRunner);
                    goodToBet &= tryBet(event, toBePlacedMarket, thirdPlaceRunner, price);
                }
            }
        }

        if (!goodToBet) {
            log.debug("Looks like there was a bad request");
            return new ArrayList<Bet>();
        }

        Collection<Bet> bets = betManager.placeBets(betRequests);
        log.debug(bets.size() + " bets Placed");
        return bets;
    }

    /**
     * Adds a bet request if the price is within parameters. Returns false if price
     * is "bad".
     *
     * @param event
     * @param market
     * @param runnerId
     * @param price
     * @return
     */
    private boolean tryBet(Event event, Market market, long runnerId, double price) {

        if (price <= 0) {
            log.debug("Can't bet[price:" + price + "]");
            return false;
        }

        if (price > MAX_PRICE) { // TODO - make property
            log.debug("Won't bet[price:" + price + "]");
            return true;
        }

        EventData eventData = event.getEventData();
        MarketData marketData = market.getMarketData();
        Runner runner = market.getRunner(runnerId);

        BetRequest betRequest = new BetRequestImpl(price, price, getBetAmount(), eventData.getExchangeEventId(), eventData.getEventName(),
            marketData.getExchangeMarketId(), marketData.getMarketType(), runnerId, runner.getRunnerData().getRunnerName(), BetTypeEnum.BET);

        betRequests.add(betRequest);

        return true;
    }

    private boolean canPlaceBet(Event race, long estimatedTimeLeft) {

        // Can't place more bets
        if (betRequests != null)
            return false;

        if (estimatedTimeLeft <= 0)
            return false;

        if (estimatedTimeLeft > getWhenToStartBetting())
            return false;

        try {
            winnerMarket = race.getMarket(MarketType.WINNER);
        } catch (NoSuchMarketException ex) {
            return false;
        }

        MarketPrices latestPrices = winnerMarket.getLatestPrices();
        prices.add(latestPrices);

        return prices.size() >= numberOfPrices;
    }

    private long getEstimatedTimeLeft(Event race)
        throws NoSuchMarketException {

        if (race.getEstimatedDurationMS() == 0)
            return 0;

        if (race.getEventData().getActualStartTime() == 0)
            return 0;

        // Get the latest timestamp from prices
        Market market = race.getMarket(MarketType.WINNER);
        MarketPrices latestPrices = market.getLatestPrices();
        long latestPriceTimestamp = latestPrices.getPricesData().getTimeMs();

        return race.getEstimatedDurationMS() - latestPriceTimestamp + race.getEventData().getActualStartTime();
    }

    private double getBetAmount() {
        return getPropertyAsDouble(PropertyNames.BET_AMOUNT);
    }

    private long getWhenToStartBetting() {
        return Property.getPropertyAsLong(PropertyKey.TIME_TO_START_BET_MS);
    }

    private int getNumberOfToBePlacedWinners(int numberOfRunners) {

        if (numberOfRunners < 5)
            return 0;

        if (numberOfRunners < 8)
            return 2;

        if (numberOfRunners < 16)
            return 3;

        return 4;
    }
}
