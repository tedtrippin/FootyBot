package com.rob.betBot.bots.betPlacers;

import java.util.Collection;

import com.rob.betBot.Bet;
import com.rob.betBot.BetManager;
import com.rob.betBot.Event;
import com.rob.betBot.bots.BetPlacer;
import com.rob.betBot.bots.modules.AbstractModule;
import com.rob.betBot.bots.modules.ModuleProperty;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;

public abstract class AbstractBetPlacer extends AbstractModule implements BetPlacer {

    public AbstractBetPlacer(String id, String name, String description, ModuleProperty... properties) {
        super(id, name, description, properties);
    }

    @Override
    public abstract Collection<Bet> placeBets(Event race, BetManager betManager)
        throws ExchangeException, BetPlaceException;

    /**
     * Rounds price down to the nearest valid price.
     * Valid prices...
     * 0.01 increments up to 2
     * 0.02 increments up to 3.0
     * 0.05 increments up to 4
     * 0.1 increments up to 6
     * 0.2 increments up to 10
     * 0.5 increments up to 20
     * 1 increments up to 30
     * 2 increments up to 50
     * 5 increments up to 100
     * over 100, increments of 10
     *
     * @param price
     * @return
     */
    public double roundPrice(double price) {
        double interval = getOddsInterval(price);
        int rounded = (int) (interval * (  (int) (price/interval)  ) * 100);
        return ((double) rounded) / 100;
    }

    /**
     * Takes a rounded price and steps down to the next
     * valid value. 1.01 can't be stepped down, just returns
     * 1.01.
     *
     * Eg.
     *   100 -> 95
     *   10.05 -> 10
     *   10 -> 9.8
     *
     * @param price
     * @param stepModifier
     * @return
     */
    public double stepDown(double price) {

        if (price == 1.01)
            return 1.01;

        return price - getOddsInterval(price);
    }

    private double getOddsInterval(double price) {

        if (price > 100)
            return 10;

        if (price > 50)
            return 5;

        if (price > 30)
            return 2;

        if (price > 20)
            return 1;

        if (price > 10)
            return 0.5;

        if (price > 6)
            return 0.2;

        if (price > 4)
            return 0.1;

        if (price > 3)
            return 0.05;

        if (price > 2)
            return 0.02;

        return 0.01;
    }
}
