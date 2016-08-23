package com.rob.betBot.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.rob.betBot.MarketType;

@Entity
@Table (name = "market")
public class MarketData extends DataObject {

    private static final long serialVersionUID = 1L;

    @Column (name = "event_id")
    private long eventId;

    @Column (name = "exchange_market_id")
    private String exchangeMarketId;

    @Column (name= "market_name")
    private String marketName;

    @Column (name = "market_type")
    @Enumerated (EnumType.STRING)
    private MarketType marketType;

    @OneToMany (fetch = FetchType.EAGER)
    @Fetch (FetchMode.SELECT)
    @JoinTable (
        name = "market_runners",
        joinColumns = @JoinColumn( name = "exchange_market_id", referencedColumnName = "exchange_market_id"),
        inverseJoinColumns = @JoinColumn( name = "exchange_runner_id", referencedColumnName = "exchange_runner_id")
    )
    private Set<RunnerData> runners = new HashSet<>();

    public MarketData() {
    }

    public MarketData(long id, long eventId, String marketId, String marketName, MarketType marketType) {

        super(id);

        this.eventId = eventId;
        this.exchangeMarketId = marketId;
        this.marketName = marketName;
        this.marketType = marketType;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public MarketType getMarketType() {
        return marketType;
    }

    public void setMarketType(MarketType marketType) {
        this.marketType = marketType;
    }

    public String getExchangeMarketId() {
        return exchangeMarketId;
    }

    public void setExchangeMarketId(String exchangeMarketId) {
        this.exchangeMarketId = exchangeMarketId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Set<RunnerData> getRunners() {
        return runners;
    }

    public void setRunners(Set<RunnerData> runners) {
        this.runners = runners;
    }

    public void addRunner(RunnerData runner) {
        runners.add(runner);
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof EventData))
            return false;

        return id == ((MarketData)o).id;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public String toString() {
        return new StringBuilder("marketName:").append(marketName)
            .append(",exchangeMarketId:").append(exchangeMarketId).toString();
    }
}
