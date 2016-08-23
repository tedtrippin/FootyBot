package com.rob.betBot.exchange;

public class ExchangeInfo {

    private final int id;
    private final String name;

    public ExchangeInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
