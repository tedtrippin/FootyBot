package com.rob.betBot.bots.betPlacers;

import java.util.Collection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.rob.betBot.Bet;
import com.rob.betBot.BetManager;
import com.rob.betBot.Event;
import com.rob.betBot.bots.modules.ModulePropertyImpl;
import com.rob.betBot.bots.modules.ModulePropertyType;
import com.rob.betBot.bots.modules.PropertyNames;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;

public class InPlayFavouriteBetPlacer extends AbstractBetPlacer {

    private final static Logger log = LogManager.getLogger(InPlayFavouriteBetPlacer.class);
    private final static String desc = "When in-play, places a bet on the favourite runner";

    public InPlayFavouriteBetPlacer() {
        super("101", "Bet on favourite, in play", desc,
            new ModulePropertyImpl(PropertyNames.BET_AMOUNT, ModulePropertyType.NUMBER, "Bet amount"));
    }

    @Override
    public Collection<Bet> placeBets(Event race, BetManager betManager)
        throws ExchangeException, BetPlaceException {

        if (!race.isInPlay())
            return ImmutableList.of();

        if (log.isDebugEnabled())
            log.debug("Placing to-be-placed bet on favourite [race:" + race + "]");
// TODO
//        EventPrices prices = race.getLatestPrices();
//        long favouriteId = prices.getFavouriteId();
//        Bet bet = betManager.placePlacedBet(race, favouriteId, getBetAmount());
//
//        return ImmutableList.of(bet);
        return ImmutableList.of();
    }

    private double getBetAmount() {
        return getPropertyAsDouble(PropertyNames.BET_AMOUNT);
    }
}
