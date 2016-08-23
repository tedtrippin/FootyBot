package com.rob.betBot.exchange.betfair.model;

public class CancelInstruction {

    private String betId;
    private Double sizeReduction;

    public CancelInstruction(String betId, Double sizeReduction) {
        this.betId = betId;
        this.sizeReduction = sizeReduction;
    }

    public String getBetId() {
        return betId;
    }

    public Double getSizeReduction() {
        return sizeReduction;
    }
}
