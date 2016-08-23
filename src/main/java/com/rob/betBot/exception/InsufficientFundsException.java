package com.rob.betBot.exception;

public class InsufficientFundsException extends BetPlaceException {

    private static final long serialVersionUID = 1L;

    private double amount;

    public InsufficientFundsException(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
