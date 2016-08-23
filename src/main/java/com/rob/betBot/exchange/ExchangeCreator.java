package com.rob.betBot.exchange;

import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.LoginFailedException;

public interface ExchangeCreator {

    /**
     * Returns the {@link ExchangeInfo} for exchanges created with this {@code ExchangeCreator}.
     *
     * @return
     */
    public ExchangeInfo getExchangeInfo();

    /**
     * Authenticates the credentials and returns an instance of {@link Exchange}.
     *
     * @param username
     * @param password
     * @return
     */
    public Exchange createExchange(String username, String password)
        throws LoginFailedException, ExchangeException;
}
