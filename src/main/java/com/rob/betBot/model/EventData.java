package com.rob.betBot.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.rob.betBot.SportEnum;

@Entity
@Table (name = "event")
public class EventData extends DataObject {

    private static final long serialVersionUID = 1L;

    @Column (name = "exchange_id")
    private int exchangeId;

    @Column
    private String location;

    @Column (name = "exchange_event_id")
    private String exchangeEventId;

    @Column (name = "event_name")
    private String eventName;

    @Column
    @Enumerated (EnumType.STRING)
    private SportEnum sport;

    @Column (name = "expected_start_time")
    private long expectedStartTime;

    @Column (name = "actual_start_time")
    private long actualStartTime;

    @Column (name = "finished_time")
    private long finishedTime;

    @OneToMany (fetch = FetchType.EAGER)
    @Fetch (FetchMode.SELECT)
    @JoinColumn (name = "event_id", insertable = false, updatable = false)
    private Collection<MarketData> markets = new ArrayList<>();

    public EventData() {
    }

    public EventData(long id, String exchangeEventId, int exchangeId, String location, String eventName,
        SportEnum sport, long expectedStartTime) {

        super(id);

        this.exchangeEventId = exchangeEventId;
        this.exchangeId = exchangeId;
        this.location = location;
        this.eventName = eventName;
        this.sport = sport;
        this.expectedStartTime = expectedStartTime;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExchangeEventId() {
        return exchangeEventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public SportEnum getSport() {
        return sport;
    }

    public void setEventName(SportEnum sport) {
        this.sport = sport;
    }

    public long getExpectedStartTime() {
        return expectedStartTime;
    }

    public void setExpectedStartTime(long expectedStartTime) {
        this.expectedStartTime = expectedStartTime;
    }

    public long getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(long actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public long getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(long finishedTime) {
        this.finishedTime = finishedTime;
    }

    public Collection<MarketData> getMarkets() {
        return markets;
    }

    public void setMarkets(Collection<MarketData> markets) {
        this.markets = markets;
    }

    public void addMarket(MarketData market) {
        markets.add(market);
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof EventData))
            return false;

        EventData other = (EventData) o;
        if (other.exchangeEventId == null || !other.exchangeEventId.equals(exchangeEventId))
            return false;

        return other.expectedStartTime == expectedStartTime;
    }

    @Override
    public int hashCode() {
        return (int) (exchangeId ^ id);
    }

    @Override
    public String toString() {
        return new StringBuilder("eventName:").append(eventName)
            .append(",startTime:").append(expectedStartTime).toString();
    }
}
