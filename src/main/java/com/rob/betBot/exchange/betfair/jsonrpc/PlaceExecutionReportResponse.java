package com.rob.betBot.exchange.betfair.jsonrpc;

import com.rob.betBot.exchange.betfair.model.PlaceExecutionReport;

public class PlaceExecutionReportResponse extends BetfairResponse {

    private PlaceExecutionReport result;

    public PlaceExecutionReportResponse() {
    }

    public PlaceExecutionReport getPlaceExecutionReport() {
        return result;
    }

    public void setResult(PlaceExecutionReport result) {
        this.result = result;
    }
}
