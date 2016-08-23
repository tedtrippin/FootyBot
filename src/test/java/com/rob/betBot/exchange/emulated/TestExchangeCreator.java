package com.rob.betBot.exchange.emulated;

import org.springframework.stereotype.Component;

import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.LoginFailedException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.ExchangeCreator;
import com.rob.betBot.exchange.ExchangeInfo;

@Component
public class TestExchangeCreator implements ExchangeCreator {

    @Override
    public ExchangeInfo getExchangeInfo() {
        return new ExchangeInfo(Exchange.TEST_EXCHANGE_ID, "Test");
    }


    @Override
    public Exchange createExchange(String username, String password)
        throws LoginFailedException, ExchangeException {

        return new TestExchange();
    }
}
