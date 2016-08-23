package com.rob.betBot.exchange.emulated;

import java.util.HashMap;
import java.util.Map;

import com.rob.betBot.Event;
import com.rob.betBot.Market;
import com.rob.betBot.MarketPrices;
import com.rob.betBot.PricesManager;
import com.rob.betBot.Runner;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.model.PricesData;

public class EmulatedPriceManager implements PricesManager {

    private MarketPrices lastPrices;
    int counter = 0;

    public EmulatedPriceManager() {
    }

    @Override
    public boolean updateEvent(Event event, long currentTime)
        throws ExchangeException {

        counter++;
        event.getMarkets().forEach(m -> update(event, m));
        return true;
    }


    private void update(Event event, Market market) {

        long duration = counter * 1000;
        long time = duration + event.getEventData().getExpectedStartTime();

        Map<Long, Double> prices = new HashMap<>();
        if (lastPrices == null) {
            for (Runner runner : market.getRunners()) {
                prices.put(runner.getRunnerData().getExchangeRunnerId(), new Double(Math.random() * 10 + 0.1)); // Random price between 0.1 and 10.1
            }
        } else {
            for (Runner runner : market.getRunners()) {
                double price = lastPrices.getPrice(runner.getRunnerData().getExchangeRunnerId());
                price *= (Math.random()*2) + 0.1;
                if (price > 1000)
                    price = 1000;
                else if (price < 1.1)
                    price = 1.1;

                prices.put(runner.getRunnerData().getExchangeRunnerId(), price);
            }
        }

        lastPrices = new MarketPrices(new PricesData(Exchange.EMULATED_EXCHANGE_ID, event.getEventData().getId(),
            market.getMarketData().getId(), time, prices));
        market.setLatestPrices(lastPrices);

        if (duration >= event.getEstimatedDurationMS())
            event.finished(time);
    }
}
