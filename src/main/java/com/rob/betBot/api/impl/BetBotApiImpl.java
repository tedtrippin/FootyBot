package com.rob.betBot.api.impl;

import java.util.Collection;

import com.rob.betBot.Event;
import com.rob.betBot.api.BetBotApi;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.bots.JsonBotFactory;
import com.rob.betBot.engine.BetEngine;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.InvalidBotException;
import com.rob.betBot.exception.LoginFailedException;
import com.rob.betBot.exception.NoSuchExchangeException;
import com.rob.betBot.exception.NotLoggedInException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.ExchangeFactory;

public class BetBotApiImpl implements BetBotApi {

    private ExchangeFactory exchangeFactory;
    private BetEngine betEngine;
    private Exchange exchange;

    public BetBotApiImpl(ExchangeFactory exchangeFactory, BetEngine betEngine) {
        this.exchangeFactory = exchangeFactory;
        this.betEngine = betEngine;
    }

    @Override
    public void login(int exchangeId, String username, String password)
        throws LoginFailedException, ExchangeException, NoSuchExchangeException {

        exchange = exchangeFactory.getExchange(exchangeId, username, password);
    }

    /**
     * {@Inherit doc}
     */
    @Override
    public Collection<Event> getRaces()
        throws ExchangeException, NotLoggedInException {

        if (exchange == null)
            throw new NotLoggedInException();

        return betEngine.getLoadedRaces(exchange);
    }

    /**
     * {@Inherit doc}
     */
    @Override
    public void addBot(long raceId, String botJson)
        throws ExchangeException, NotLoggedInException, InvalidBotException {

        if (exchange == null)
            throw new NotLoggedInException();

        Bot bot = JsonBotFactory.createBot(botJson);
        Event race = exchange.getEventManager().getEvent(raceId);
        betEngine.addBot(race, bot, exchange);
    }
}
