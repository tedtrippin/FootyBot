package com.rob.betBot;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.rob.betBot.exception.NoSuchMarketException;
import com.rob.betBot.model.EventData;

/**
 * Provides information about an event.
 *
 * @author robert.haycock
 *
 */
public class Event implements Comparable<Event> {

    private final EventData eventData;
    private final long estimatedDurationMS;
    private final Collection<Market> markets;
    private boolean recordPrices;
    private boolean inPlay;
    private boolean suspended;
    private String warning;
    private long suspendedTime = 0; // Track when market suspended. The last value will be finish time

    public Event(EventData eventData, Collection<Market> markets, long estimatedDurationMS) {

        this.eventData = eventData;
        this.markets = markets;
        this.estimatedDurationMS = estimatedDurationMS;
    }

    public void started(long startTime) {
        eventData.setActualStartTime(startTime);
        inPlay = true;
    }

    public void suspended(long suspendedTime) {
        this.suspendedTime = suspendedTime;
        suspended = true;
    }

    public void finished(long finishTime) {

        // We make the assumption that market is suspended before closed
        if (suspendedTime != 0)
            eventData.setFinishedTime(suspendedTime);
        else
            eventData.setFinishedTime(finishTime);
    }

    public EventData getEventData() {
        return eventData;
    }

    public Collection<Market> getMarkets() {
        return ImmutableList.copyOf(markets);
    }

    public Market getMarket(String exchangeMarketId) {

        for (Market market : markets) {
            if (market.getMarketData().getExchangeMarketId().equals(exchangeMarketId))
                return market;
        }
        return null;
    }

    public Market getMarket(long id) {

        for (Market market : markets) {
            if (market.getMarketData().getId() == id)
                return market;
        }
        return null;
    }

    public Market getMarket(MarketType marketType)
        throws NoSuchMarketException {

        for (Market market : markets) {
            if (market.getMarketData().getMarketType() == marketType)
                return market;
        }

        throw new NoSuchMarketException(eventData.getExchangeId(), String.valueOf(eventData.getId()), marketType);
    }

    public long getEstimatedDurationMS() {
        return estimatedDurationMS;
    }

    public long getDurationMS() {

        if (!isFinished())
            return -1;

        return eventData.getFinishedTime() - eventData.getActualStartTime();
    }

    public boolean isFinished() {
        return eventData.getFinishedTime() > 0;
    }

    public boolean isInPlay() {
        return inPlay;
    }

    public void setInPlay(boolean inPlay) {
        this.inPlay = inPlay;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    /**
     * Returns true is this race should persist prices.
     *
     * @return
     */
    public boolean shouldRecordPrices() {
        return recordPrices;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public boolean getHasCorrectScore() {

        for (Market market : markets) {
            if (market.getMarketData().getMarketType() == MarketType.CORRECT_SCORE)
                return true;
        }
        return false;
    }

    @Override
    public String toString() {

        return new StringBuffer()
            .append("eventData[").append(eventData)
            .append("],isInPlay:").append(inPlay).toString();
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Event))
            return false;

        return eventData.equals(((Event)obj).getEventData());
    }

    @Override
    public int hashCode() {
        return eventData.hashCode();
    }

    @Override
    public int compareTo(Event otherRace) {
        if (eventData.getExpectedStartTime() < otherRace.eventData.getExpectedStartTime())
            return -1;
        else
            return 1;
    }
}
