package com.rob.betBot.api;

import java.util.Collection;

import com.rob.betBot.Event;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.InvalidBotException;
import com.rob.betBot.exception.LoginFailedException;
import com.rob.betBot.exception.NoSuchExchangeException;
import com.rob.betBot.exception.NotLoggedInException;

public interface BetBotApi {

    /**
     * Logs in and authenticates against the request exchange.
     *
     * @param exchangeId
     * @param username
     * @param password
     * @throws LoginFailedException
     * @throws ExchangeException
     * @throws NoSuchExchangeException
     */
    public void login(int exchangeId, String username, String password)
        throws LoginFailedException, ExchangeException, NoSuchExchangeException;

    /**
     * Get the next available races, ordered by start time, for the current exchange.
     *
     * @return
     * @throws ExchangeException
     * @throws NotLoggedInException
     */
    public Collection<Event> getRaces()
        throws ExchangeException, NotLoggedInException;

    /**
     * Attaches a bot to a race.
     *
     * @param exchangeId
     * @param raceId
     * @param botJson
     * @throws ExchangeException
     * @throws NotLoggedInException
     * @throws InvalidBotException
     */
    public void addBot(long raceId, String botJson)
        throws ExchangeException, NotLoggedInException, InvalidBotException;
}
