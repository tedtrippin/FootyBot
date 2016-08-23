package com.rob.betBot.exception;

public class CommunicatorException extends Exception {

    private static final long serialVersionUID = 1L;

    public CommunicatorException(String message) {
        super(message);
    }

    public CommunicatorException(String message, Throwable innerException) {
        super(message, innerException);
    }
}
