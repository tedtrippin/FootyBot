package com.rob.betBot.bots;

import java.util.Collection;

import com.rob.betBot.Bet;
import com.rob.betBot.BetManager;
import com.rob.betBot.Event;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;

public abstract class AbstractBetPlacer implements BetPlacer {

    protected Event race;
    protected BetManager betManager;
    protected double betAmount;

    public AbstractBetPlacer(Event race, BetManager betManager, double betAmount) {
        this.race = race;
        this.betManager = betManager;
        this.betAmount = betAmount;
    }

    public Event getRace() {
        return race;
    }

    public BetManager getBetManager() {
        return betManager;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public abstract boolean canPlaceBet();

    public abstract Collection<Bet> placeBets()
        throws ExchangeException, BetPlaceException;
}
