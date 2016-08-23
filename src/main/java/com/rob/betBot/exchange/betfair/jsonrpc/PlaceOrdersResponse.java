package com.rob.betBot.exchange.betfair.jsonrpc;

import java.util.List;

import com.rob.betBot.exchange.betfair.model.MarketBook;

public class PlaceOrdersResponse extends BetfairResponse {

    private List<MarketBook> result;

    public PlaceOrdersResponse() {
    }

    public List<MarketBook> getMarketBooks() {
        return result;
    }

    public void setResult(List<MarketBook> result) {
        this.result = result;
    }
}
