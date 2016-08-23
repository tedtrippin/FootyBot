package com.rob.betBot.model;

import java.util.Map;

import javax.persistence.Column;

public class PricesData {

    @Column
    private int exchangeId;

    @Column
    private long eventId;

    @Column
    private long marketId;

    @Column
    private long timeMs;

    @Column
    private Map<Long, Double> pricesMap;

    public PricesData(int exchangeId, long eventId, long marketId, long timeMs, Map<Long, Double> pricesMap) {
        this.exchangeId = exchangeId;
        this.eventId = eventId;
        this.marketId = marketId;
        this.timeMs = timeMs;
        this.pricesMap = pricesMap;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getMarketId() {
        return marketId;
    }

    public void setMarketId(long marketId) {
        this.marketId = marketId;
    }

    public long getTimeMs() {
        return timeMs;
    }

    public void setTimeMs(long timeMs) {
        this.timeMs = timeMs;
    }

    public Map<Long, Double> getPricesMap() {
        return pricesMap;
    }

    public void setPricesMap(Map<Long, Double> pricesMap) {
        this.pricesMap = pricesMap;
    }
}
