package com.rob.betBot.exception;

public class BetNotFoundException extends BetBotException {

    private static final long serialVersionUID = 1L;

    private String betId;

    public BetNotFoundException(String betId) {
        this.betId = betId;
    }

    public String getBetId() {
        return betId;
    }
}
