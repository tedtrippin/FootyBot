package com.rob.betBot.mvc.model;

import java.util.List;

import com.google.gson.JsonObject;

public class BetBotCookie {

    private String name;
    private List<JsonObject> filters;
    private List<JsonObject> betPlacers;

    public BetBotCookie() {
    }

    public String getName() {
        return name;
    }

    public List<JsonObject> getFilters() {
        return filters;
    }

    public List<JsonObject> getBetPlacers() {
        return betPlacers;
    }
}
