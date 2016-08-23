package com.rob.betBot;

/**
 * Info about the bet from the exchange used to get prices.
 * This is needed for when prtices come from one exchange
 * but bet placed on another.
 *
 * @author robert.haycock
 *
 */
public class BetPricesExchangeInfo {

    private final String eventId;
    private final String marketId;
    private final long runnerId;

    public BetPricesExchangeInfo(String eventId, String marketId, long runnerId) {
        this.eventId = eventId;
        this.marketId = marketId;
        this.runnerId = runnerId;
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

    @Override
    public String toString() {
        return new StringBuilder()
            .append("eventId").append(eventId)
            .append(",marketId=").append(marketId)
            .append(",runnerId=").append(runnerId).toString();
    }
}
