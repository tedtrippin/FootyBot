package com.rob.betBot.exception;

public class InvalidDataException extends BetBotException {

    private static final long serialVersionUID = 1L;

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Exception innerException) {
        super(message, innerException);
    }
}
