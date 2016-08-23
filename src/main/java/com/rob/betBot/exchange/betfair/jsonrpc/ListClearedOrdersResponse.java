package com.rob.betBot.exchange.betfair.jsonrpc;

import com.rob.betBot.exchange.betfair.model.ClearedOrderSummaryReport;


public class ListClearedOrdersResponse extends BetfairResponse {

    private ClearedOrderSummaryReport result;

    public ListClearedOrdersResponse() {
    }

    public ClearedOrderSummaryReport getClearedOrderSummaryReport() {
        return result;
    }

    public void setResult(ClearedOrderSummaryReport result) {
        this.result = result;
    }
}
