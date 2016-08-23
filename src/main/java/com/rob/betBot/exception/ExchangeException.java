package com.rob.betBot.exception;

public class ExchangeException extends BetBotException {

    private static final long serialVersionUID = 1L;

    public ExchangeException(String message) {
        super(message);
    }

    public ExchangeException(String message, Exception innerException) {
        super(message, innerException);
    }
}
