package com.rob.betBot.exception;

public class BetCancelException extends Exception {

    private static final long serialVersionUID = 1L;

    private String betId;

    public BetCancelException(String betId) {
        super();
    }

    public BetCancelException(String betId, String message) {
        super(message);
    }

    public String getBetId() {
        return betId;
    }
}
