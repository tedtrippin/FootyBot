package com.rob.betBot.exchange.betBillHill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.LoginFailedException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.ExchangeCreator;
import com.rob.betBot.exchange.ExchangeInfo;
import com.rob.betBot.exchange.betfair.BetfairFootballConverter;
import com.rob.betBot.exchange.betfair.jsonrpc.BetfairJsonLoginCommunicator;
import com.rob.betBot.exchange.combined.WilliamHillBetfairFootballBetManager;
import com.rob.betBot.exchange.williamHill.WilliamHillFootyEventManager;
import com.rob.betBot.exchange.williamHill.WilliamHillPricesManager;

@Component
public class BetBillHillExchangeCreator  implements ExchangeCreator {

    @Autowired
    private WilliamHillFootyEventManager williamHillFootyEventManager;

    @Autowired
    private BetfairFootballConverter betfairConverter;

    @Autowired
    private WilliamHillPricesManager pricesManager;

    @Override
    public Exchange createExchange(String username, String password)
        throws LoginFailedException, ExchangeException {

        BetfairJsonLoginCommunicator betfairSso = new BetfairJsonLoginCommunicator();
        betfairSso.login(username, password);

        return new BetBillHillExchange(username, pricesManager,
            new WilliamHillBetfairFootballBetManager(betfairSso, betfairConverter), williamHillFootyEventManager);
    }

    @Override
    public ExchangeInfo getExchangeInfo() {
        return new ExchangeInfo(Exchange.BETBILLHILL_EXCHANGE_ID, "BetBillHill");
    }
}