package com.rob.betBot.exchange;

import com.rob.betBot.BetManager;
import com.rob.betBot.EventManager;
import com.rob.betBot.PricesManager;

public abstract class AbstractExchange implements Exchange {

    private final int exchangeId;
    private final String name;
    private final String username;
    private PricesManager pricesManager;
    private BetManager betManager;
    private EventManager eventManager;

    protected AbstractExchange(int exchangeId, String name, String username, PricesManager pricesManager,
        BetManager betManager, EventManager raceManager) {

        this.exchangeId = exchangeId;
        this.name = name;
        this.username = username;
        this.pricesManager = pricesManager;
        this.betManager = betManager;
        this.eventManager = raceManager;
    }

    @Override
    public int getExchangeId() {
        return exchangeId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public PricesManager getPricesManager() {
        return pricesManager;
    }

    @Override
    public BetManager getBetManager() {
        return betManager;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }
}
