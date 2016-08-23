package com.rob.betBot.exception;

public class NoSuchRunnerException extends BetBotException {

    private static final long serialVersionUID = 1L;

    private final int exchangeId;
    private final String eventId;
    private final String marketId;
    private final String runnerName;
    private final long runnerId;

    public NoSuchRunnerException(int exchangeId, String eventId, String marketId, long runnerId, String runnerName) {
        this.exchangeId = exchangeId;
        this.eventId = eventId;
        this.marketId = marketId;
        this.runnerId = runnerId;
        this.runnerName = runnerName;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getMarketId() {
        return marketId;
    }

    public long getRunnerId() {
        return runnerId;
    }

    public String getRunnerName() {
        return runnerName;
    }
}
