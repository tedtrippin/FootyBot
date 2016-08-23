package com.rob.betBot.exception;

public class NoSuchEventException extends BetBotException {

    private static final long serialVersionUID = 1L;

    private final int exchangeId;
    private final long eventId;
    private final String eventName;

    public NoSuchEventException(int exchangeId, long eventId, String eventName) {
        this.exchangeId = exchangeId;
        this.eventId = eventId;
        this.eventName = eventName;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public long getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }
}
