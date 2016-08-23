package com.rob.betBot.exchange.betfair.model;

import java.util.Date;

public class ItemDescription {

    private String eventTypeDesc;
    private String eventDesc;
    private String marketDesc;
    private Date marketStartTime;
    private String runnerDesc;
    private int numberOfWinners;

    public String getEventTypeDesc() {
        return eventTypeDesc;
    }

    public void setEventTypeDesc(String eventTypeDesc) {
        this.eventTypeDesc = eventTypeDesc;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getMarketDesc() {
        return marketDesc;
    }

    public void setMarketDesc(String marketDesc) {
        this.marketDesc = marketDesc;
    }

    public Date getMarketStartTime() {
        return marketStartTime;
    }

    public void setMarketStartTime(Date marketStartTime) {
        this.marketStartTime = marketStartTime;
    }

    public String getRunnerDesc() {
        return runnerDesc;
    }

    public void setRunnerDesc(String runnerDesc) {
        this.runnerDesc = runnerDesc;
    }

    public int getNumberOfWinners() {
        return numberOfWinners;
    }

    public void setNumberOfWinners(int numberOfWinners) {
        this.numberOfWinners = numberOfWinners;
    }
}
