package com.rob.betBot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.rob.betBot.model.PricesData;
import com.rob.betBot.model.RunnerData;

public class PricePredictor {

    private final static Logger log = LogManager.getLogger(PricePredictor.class);

    // Don't consider any with a price higher than this
    private double upperLimit = 500; // TODO - make a property

    public PricesHelper predict(Market market, long estimatedTimeLeft, List<MarketPrices> prices) {

        Map<Long, Double> predictedPrices = new HashMap<Long, Double>();
        for (Runner runner : market.getRunners()) {
            double predictedPrice = predict(runner.getRunnerData(), prices);
            predictedPrices.put(runner.getRunnerData().getExchangeRunnerId(), predictedPrice);
        }

        return new PricesHelper(predictedPrices);
    }

    @Test
    public void tttttttttttttttttt() {
        //predicted[10.184313725490195] prices 1,2,3[24.0, 85.0, 120.0]

        MarketPrices p1 = makeMarketPrices(24.0);
        MarketPrices p2 = makeMarketPrices(85.0);
        MarketPrices p3 = makeMarketPrices(120.0);

        Market m = new Market(null, Lists.newArrayList(new Runner(new RunnerData(1, 1, 1, "test"))));

        PricesHelper ph = new PricePredictor().predict(m, 10000, Lists.newArrayList(p1,  p2, p3));

        System.out.println(ph.getSortedPrice(0));

    }

    private MarketPrices makeMarketPrices(double price) {

        Map<Long, Double> pricesMap = new HashMap<>();
        pricesMap.put(1L,  price);
        PricesData pd = new PricesData(1, 1, 1, 1, pricesMap);
        return new MarketPrices(pd);
    }

    private double predict(RunnerData runnerData, List<MarketPrices> prices) {

        long runnerId = runnerData.getExchangeRunnerId();
        String runnerName = runnerData.getRunnerName();
        double price1 = prices.get(0).getPrice(runnerId);

        if (price1 >= upperLimit) {
            log.debug("runner[" + runnerId + "] predicted[1000]");
            return 1000;
        }

        double price2 = prices.get(1).getPrice(runnerId);
        double price3 = prices.get(2).getPrice(runnerId);

        double g1 = price3 / price2;
        double g2 = price2 / price1;
        double g3 = (g2 + g1*2) / 3;

        double predicted = price3 * g3;

        if (log.isDebugEnabled()) {
            log.debug("runner[" + runnerId + ":" + runnerName + "] predicted[" + predicted + "] prices 1,2,3["
                + price1 + ", " + price2 + ", " + price3 + "]");
        }

        return predicted;
    }
}
