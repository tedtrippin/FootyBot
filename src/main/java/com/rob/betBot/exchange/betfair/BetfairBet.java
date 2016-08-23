package com.rob.betBot.exchange.betfair;

import com.rob.betBot.AbstractBet;
import com.rob.betBot.BetRequest;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.betfair.model.ClearedOrderSummary;
import com.rob.betBot.exchange.betfair.model.CurrentOrderSummary;

public class BetfairBet extends AbstractBet {

    public BetfairBet(String betId, BetRequest betRequest) {
        super(Exchange.BETFAIR_EXCHANGE_ID, betId, betRequest);
    }
/*
    public BetfairBet(String betId, double price, double exchangePrice, double amount, double matchedSoFar,
        String eventId, String eventName, String marketId, MarketType marketType, long runnerId, String runnerName,
        BetTypeEnum betType) {

        super(Exchange.BETFAIR_EXCHANGE_ID, betId, price, exchangePrice, amount, matchedSoFar,  eventId, eventName,
            marketId, marketType, runnerId, runnerName, betType);
    }

*/    void update(CurrentOrderSummary summary) {
        matchedSoFar = summary.getSizeMatched();
    }

    void update(ClearedOrderSummary summary) {

        if (complete)
            return;

        complete = true;
        if (summary.getProfit() > 0)
            won = true;
    }
}
