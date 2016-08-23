package com.rob.betBot.mvc.model;

public class RunnerPrice {

    private final String name;
    private final Double price;

    public RunnerPrice(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }
}
