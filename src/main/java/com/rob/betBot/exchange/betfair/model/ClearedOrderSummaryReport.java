package com.rob.betBot.exchange.betfair.model;

import java.util.List;

public class ClearedOrderSummaryReport {

    private List<ClearedOrderSummary> clearedOrders;
    private boolean moreAvailable;


    public List<ClearedOrderSummary> getClearedOrders() {
        return clearedOrders;
    }

    public void setClearedOrders(List<ClearedOrderSummary> clearedOrders) {
        this.clearedOrders = clearedOrders;
    }

    public boolean isMoreAvailable() {
        return moreAvailable;
    }

    public void setMoreAvailable(boolean moreAvailable) {
        this.moreAvailable = moreAvailable;
    }
}
