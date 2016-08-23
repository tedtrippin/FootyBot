package com.rob.betBot.exchange.betfair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.rob.betBot.Event;
import com.rob.betBot.Market;
import com.rob.betBot.MarketPrices;
import com.rob.betBot.PricesManager;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.betfair.jsonrpc.BetfairJsonRpcCommunicator;
import com.rob.betBot.exchange.betfair.model.MarketBook;
import com.rob.betBot.exchange.betfair.model.MarketStatusEnum;
import com.rob.betBot.exchange.betfair.model.Runner;
import com.rob.betBot.model.PricesData;

public class BetfairPricesManager implements PricesManager {

    private static Logger journal = LogManager.getLogger("journal");

    private BetfairJsonRpcCommunicator betfair;

    public BetfairPricesManager(BetfairJsonRpcCommunicator betfair) {
        this.betfair = betfair;
    }

    @Override
    public boolean updateEvent(Event event, long currentTime)
        throws ExchangeException {

        Map<String, Market> betfairIdToMarketMap = new HashMap<>();
        event.getMarkets().forEach(m -> betfairIdToMarketMap.put(m.getMarketData().getExchangeMarketId(), m));

        List<String> marketIds = Lists.newArrayList();
        event.getMarkets().forEach(m -> marketIds.add(m.getMarketData().getExchangeMarketId()));

        journal.info("Fetching prices for " + event.getEventData().getEventName());
        List<MarketBook> marketBooks = betfair.listMarketBook(marketIds);

        for (MarketBook marketBook : marketBooks) {

            if (marketBook.getStatus() == MarketStatusEnum.CLOSED) {
                journal.info("-+-+-+-+-+-+-+-+-+-+-+-+- MARKET CLOSED -+-+-+-+-+-+-+-+-+-+-+-+-");
                event.finished(System.currentTimeMillis());
                return true;
            }

            if (marketBook.getStatus() == MarketStatusEnum.SUSPENDED) {

                // If already suspended, ignore
                if (event.isSuspended())
                    return true;

                journal.info("-+-+-+-+-+-+-+-+-+-+-+-+- Market suspended -+-+-+-+-+-+-+-+-+-+-+-+-");
                event.suspended(System.currentTimeMillis());
                return true;
            }

            // Be on safe side and clear suspended flag
            event.setSuspended(false);

            if (marketBook.getStatus() != MarketStatusEnum.OPEN)
                continue;

            // TODO - what if we start a bot during in play? current time wont be true start time
            if (event.getEventData().getActualStartTime() == 0 && marketBook.getInplay()) {
                journal.info("-+-+-+-+-+-+-+-+-+-+-+-+- EVENT STARTED -+-+-+-+-+-+-+-+-+-+-+-+-");
                event.started(currentTime);
            }

            Map<Long, Double> runnerPriceMap = new HashMap<Long, Double>();
            for (Runner runner : marketBook.getRunners()) {
// TODO - when testing US horses runner.getEx() was null
//                double price = 0;
//                if (runner.getEx() != null) {
//                    List<PriceSize> backPrices = runner.getEx().getAvailableToBack();
//                    if (backPrices.size() > 0)
//                        price = backPrices.get(0).getPrice();
//                }

                // Using this for now cus of the immediate above
                Double d = runner.getLastPriceTraded();
                double price = d == null ? 0 : d;

                runnerPriceMap.put(runner.getSelectionId(), price);
            }

            Market market = betfairIdToMarketMap.get(marketBook.getMarketId());
            PricesData pricesData = new PricesData(Exchange.BETFAIR_EXCHANGE_ID, event.getEventData().getId(),
                market.getMarketData().getId(), currentTime, runnerPriceMap);
            MarketPrices prices = new MarketPrices(pricesData);
            event.getMarket(marketBook.getMarketId()).setLatestPrices(prices);
        }

        return true;
    }
}
