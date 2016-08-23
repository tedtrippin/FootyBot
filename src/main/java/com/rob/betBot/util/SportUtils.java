package com.rob.betBot.util;

import java.util.Collection;

import com.rob.betBot.Event;
import com.rob.betBot.Market;
import com.rob.betBot.MarketType;
import com.rob.betBot.exception.NoSuchMarketException;

public class SportUtils {

    public static Market getMainMarket(Event event)
        throws NoSuchMarketException {

        Collection<Market> markets = event.getMarkets();
        for (Market market : markets) {
            if (market.getMarketData().getMarketType() == MarketType.WINNER)
                return market;
        }

        throw new NoSuchMarketException(event.getEventData().getExchangeId(),
            String.valueOf(event.getEventData().getId()), MarketType.WINNER);
    }

    public static MarketType getMainMarketType(Event event) {

        switch (event.getEventData().getSport()) {
            case FOOTBALL :
                return MarketType.CORRECT_SCORE;
            default:
                return MarketType.WINNER;
        }
    }
}
