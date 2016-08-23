package com.rob.betBot.exchange.betfair.model;



public class EventTypeResult {

    private EventType eventType ;
    private int marketCount;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public int getMarketCount() {
        return marketCount;
    }

    public void setMarketCount(int marketCount) {
        this.marketCount = marketCount;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("eventType=[").append(eventType)
            .append("],marketCount=").append(marketCount).toString();
    }
}
