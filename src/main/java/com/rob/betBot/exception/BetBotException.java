package com.rob.betBot.exception;

public class BetBotException extends Exception {

    private static final long serialVersionUID = 1L;

    BetBotException() {
    }

    BetBotException(String message) {
        super(message);
    }

    BetBotException(String message, Exception innerException) {
        super(message, innerException);
    }
}
