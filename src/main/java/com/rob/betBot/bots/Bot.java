package com.rob.betBot.bots;

import java.util.ArrayList;
import java.util.List;

public class Bot {

    private String name;
    private List<Filter> filters;
    private List<BetPlacer> betPlacers;

    public Bot() {
        this("Default");
    }

    public Bot(String name) {
        this.name = name;
        filters = new ArrayList<>();
        betPlacers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void addBetPlacer(BetPlacer betPlacer) {
        betPlacers.add(betPlacer);
    }

    public List<BetPlacer> getBetPlacers() {
        return betPlacers;
    }
}
