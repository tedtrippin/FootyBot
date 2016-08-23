package com.rob.betBot.exchange.betfair.jsonrpc;

import java.util.List;

import com.rob.betBot.exchange.betfair.model.MarketCatalogue;

public class ListMarketCatalogueResponse extends BetfairResponse {

    private List<MarketCatalogue> result;

    public ListMarketCatalogueResponse() {
    }

    public List<MarketCatalogue> getMarketCatalogues() {
        return result;
    }

    public void setResult(List<MarketCatalogue> result) {
        this.result = result;
    }
}
