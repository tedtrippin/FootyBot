package com.rob.betBot.exception;

public class NoSuchExchangeException extends BetBotException {

    private static final long serialVersionUID = 1L;

    private int exchangeId;

    public NoSuchExchangeException(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public int getExchangeId() {
        return exchangeId;
    }
}
