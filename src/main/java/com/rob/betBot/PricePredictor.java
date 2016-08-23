package com.rob.betBot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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

        double g1 = price2 / price3;
        double g2 = price1 / price2;
        double g3 = (g2*2 + g1) / 3;

        double predicted = price1 *g3;

        if (log.isDebugEnabled()) {
            log.debug("runner[" + runnerId + ":" + runnerName + "] predicted[" + predicted + "] prices 1,2,3["
                + price1 + ", " + price2 + ", " + price3 + "]");
        }

        return predicted;
    }
}
