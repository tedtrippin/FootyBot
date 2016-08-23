package com.rob.betBot.exchange.betfair.jsonrpc;

import com.rob.betBot.exchange.betfair.model.CancelExecutionReport;

public class CancelExecutionReportResponse extends BetfairResponse {

    private CancelExecutionReport result;

    public CancelExecutionReportResponse () {
    }

    public CancelExecutionReport getCancelExecutionReport() {
        return result;
    }

    public void setResult(CancelExecutionReport result) {
        this.result = result;
    }
}
