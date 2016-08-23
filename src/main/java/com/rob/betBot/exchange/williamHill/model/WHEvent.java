package com.rob.betBot.exchange.williamHill.model;

import java.util.List;

public class WHEvent {

    private WHSport sport;
    private WHType type;
    private String event; // event ID
    private String event_link; // URL to event page (with markets)
    private String status; // Need to find out what this the possible values are
    private String start_time; // eg. "2015-05-16 20:00:00"
    private String offset; // eg. "0 00:00:00"
    private String name; // eg. "Guingamp v Toulouse"
    private List<WHMarket> markets;
    private List<WHSelection> selections;

    public WHSport getSport() {
        return sport;
    }

    public void setSport(WHSport sport) {
        this.sport = sport;
    }

    public WHType getType() {
        return type;
    }

    public void setType(WHType type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEvent_link() {
        return event_link;
    }

    public void setEvent_link(String event_link) {
        this.event_link = event_link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WHMarket> getMarkets() {
        return markets;
    }

    public void setMarkets(List<WHMarket> markets) {
        this.markets = markets;
    }

    public List<WHSelection> getSelections() {
        return selections;
    }

    public void setSelections(List<WHSelection> selections) {
        this.selections = selections;
    }
}
