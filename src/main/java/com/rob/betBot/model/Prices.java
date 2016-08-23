package com.rob.betBot.model;

import com.rob.betBot.MarketPrices;

public class Prices {

    private int exchangeId;
    private long raceId;
    private long timeMs;
    private String pricesString;

    public Prices() {
    }

    public Prices(int exchangeId, long raceId, MarketPrices racePrices) {

        this.exchangeId = exchangeId;
        this.raceId = raceId;
        timeMs = racePrices.getPricesData().getTimeMs();

        StringBuilder sb = new StringBuilder();
        for (long runnerId : racePrices.getSortedRunners()) {
            sb.append(',')
                .append(runnerId)
                .append('=')
                .append(racePrices.getPrice(runnerId));
        }
        pricesString = sb.substring(1);
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public long getRaceId() {
        return raceId;
    }

    public void setRaceId(long raceId) {
        this.raceId = raceId;
    }

    public long getTimeMs() {
        return timeMs;
    }

    public void setTimeMs(long timeMs) {
        this.timeMs = timeMs;
    }

    public String getPricesString() {
        return pricesString;
    }

    public void setPricesString(String pricesString) {
        this.pricesString = pricesString;
    }
}
