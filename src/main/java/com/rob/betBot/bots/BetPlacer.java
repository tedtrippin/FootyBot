package com.rob.betBot.bots;

import java.util.Collection;

import com.rob.betBot.Bet;
import com.rob.betBot.BetManager;
import com.rob.betBot.Event;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;

public interface BetPlacer extends Module {

    /**
     * If able, places a bet on the appropriate exchange.
     *
     * @return
     */
    public Collection<Bet> placeBets(Event race, BetManager betManager)
        throws ExchangeException, BetPlaceException;
}
