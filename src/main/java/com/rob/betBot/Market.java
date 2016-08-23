package com.rob.betBot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rob.betBot.model.MarketData;

public class Market {

    private final MarketData marketData;
    private final Map<Long, Runner> runnersMap;
    private MarketPrices latestPrices;
    private List<MarketPrices> prices = new ArrayList<>();

    public Market(MarketData marketData, Collection<Runner> runners) {

        this.marketData = marketData;

        runnersMap = new HashMap<Long, Runner>();
        for (Runner runner : runners)
            runnersMap.put(runner.getRunnerData().getExchangeRunnerId(), runner);
    }

    public MarketPrices getLatestPrices() {
        return latestPrices;
    }

    public void setLatestPrices(MarketPrices latestPrices) {
        this.latestPrices = latestPrices;
        prices.add(latestPrices);
    }

    public List<MarketPrices> getPrices() {
        return prices;
    }

    public MarketData getMarketData() {
        return marketData;
    }

    public Collection<Runner> getRunners() {
        return new ArrayList<>(runnersMap.values());
    }

    public Runner getRunner(long runnerId) {
        return runnersMap.get(runnerId);
    }
}
