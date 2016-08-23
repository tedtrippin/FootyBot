package com.rob.betBot.exception;

public class InvalidBotException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidBotException(String botJson, Exception ex) {
        super("Invalid bot JSON[" + botJson + "], ex");
    }
}
