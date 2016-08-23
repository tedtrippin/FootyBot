package com.rob.betBot.exception;

public class LoginFailedException extends BetBotException {

    private static final long serialVersionUID = 1L;

    public LoginFailedException(String message) {
        super(message);
    }
}
