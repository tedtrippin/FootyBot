package com.rob.betBot.exception;

import com.rob.betBot.MarketType;

public class NoSuchMarketException extends BetBotException {

    private static final long serialVersionUID = 1L;

    private final int exchangeId;
    private final String eventId;
    private final MarketType marketType;

    public NoSuchMarketException(int exchangeId, String eventId, MarketType marketType) {
        this.exchangeId = exchangeId;
        this.eventId = eventId;
        this.marketType = marketType;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public String getEventId() {
        return eventId;
    }

    public MarketType getMarketType() {
        return marketType;
    }
}
