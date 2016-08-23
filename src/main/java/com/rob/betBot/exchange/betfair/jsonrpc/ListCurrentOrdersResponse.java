package com.rob.betBot.exchange.betfair.jsonrpc;

import com.rob.betBot.exchange.betfair.model.CurrentOrderSummaryReport;

public class ListCurrentOrdersResponse extends BetfairResponse {

    private CurrentOrderSummaryReport result;

    public ListCurrentOrdersResponse() {
    }

    public CurrentOrderSummaryReport getCurrentOrderSummaryReport() {
        return result;
    }

    public void setResult(CurrentOrderSummaryReport result) {
        this.result = result;
    }
}
