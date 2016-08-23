package com.rob.betBot.exchange.betfair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.LoginFailedException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.ExchangeCreator;
import com.rob.betBot.exchange.ExchangeInfo;
import com.rob.betBot.exchange.betfair.jsonrpc.BetfairJsonLoginCommunicator;
import com.rob.betBot.exchange.betfair.jsonrpc.BetfairJsonRpcCommunicator;

@Component
public class BetfairExchangeCreator implements ExchangeCreator {

    @Autowired
    private BetfairEventManager eventManager;

    @Override
    public Exchange createExchange(String username, String password)
        throws LoginFailedException, ExchangeException {

        BetfairJsonLoginCommunicator betfairSso = new BetfairJsonLoginCommunicator();
        String sessionToken = betfairSso.login(username, password);

        BetfairJsonRpcCommunicator betfair = new BetfairJsonRpcCommunicator(sessionToken);
        return new BetfairExchange(username, new BetfairPricesManager(betfair),
            new BetfairBetManager(betfair), eventManager, betfair);
    }

    @Override
    public ExchangeInfo getExchangeInfo() {
        return new ExchangeInfo(Exchange.BETFAIR_EXCHANGE_ID, "Betfair");
    }
}