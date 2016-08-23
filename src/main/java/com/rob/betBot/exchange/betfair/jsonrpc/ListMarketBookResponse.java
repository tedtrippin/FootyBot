package com.rob.betBot.exchange.betfair.jsonrpc;

import java.util.List;

import com.rob.betBot.exchange.betfair.model.MarketBook;

public class ListMarketBookResponse extends BetfairResponse {

    private List<MarketBook> result;

    public ListMarketBookResponse() {
    }

    public List<MarketBook> getMarketBooks() {
        return result;
    }

    public void setResult(List<MarketBook> result) {
        this.result = result;
    }
}
