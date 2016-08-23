package com.rob.betBot.exchange.betfair.jsonrpc;

import java.util.List;

import com.rob.betBot.exchange.betfair.model.EventTypeResult;

public class ListEventTypesResponse extends BetfairResponse {

    private List<EventTypeResult> result;

    public ListEventTypesResponse() {
    }

    public List<EventTypeResult> getEventTypeResults() {
        return result;
    }

    public void setResult(List<EventTypeResult> result) {
        this.result = result;
    }
}
