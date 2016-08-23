package com.rob.betBot.exchange.betfair;

import com.rob.betBot.exchange.AbstractExchange;
import com.rob.betBot.exchange.betfair.jsonrpc.BetfairJsonRpcCommunicator;

public class BetfairExchange extends AbstractExchange {

    protected final static String CURRENCY_CODE = "GBP";

    private final BetfairJsonRpcCommunicator betfair;

    public BetfairExchange(String username, BetfairPricesManager pricesManager,
        BetfairBetManager betManager, BetfairEventManager raceManager, BetfairJsonRpcCommunicator betfair) {

        super(BETFAIR_EXCHANGE_ID, "Betfair", username, pricesManager, betManager, raceManager);

        this.betfair = betfair;
    }

    public BetfairJsonRpcCommunicator getBetfair() {
        return betfair;
    }
}
