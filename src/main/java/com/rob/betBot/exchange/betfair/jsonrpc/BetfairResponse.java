package com.rob.betBot.exchange.betfair.jsonrpc;

import com.rob.betBot.exchange.betfair.model.Error;

public abstract class BetfairResponse {

    private Error error;
    private String jsonrpc;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }
}
