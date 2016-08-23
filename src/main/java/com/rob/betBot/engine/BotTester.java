package com.rob.betBot.engine;

import java.util.ArrayList;
import java.util.List;

import com.rob.betBot.Event;
import com.rob.betBot.MarketPrices;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exchange.Exchange;

public class BotTester {

    private final Event race;
    private final EventBot raceBot;
    private final List<MarketPrices> prices = new ArrayList<>();

    public BotTester(Bot bot, Event race, Exchange testExchange) {

        this.race = race;
        raceBot = new EventBot(bot, testExchange);
        raceBot.setEventAndPrices(race, prices);
    }

    public void run() {
        try {
            while(!race.isFinished()) {
                raceBot.updateEvent(System.currentTimeMillis());
                raceBot.updated();
            }
        } catch (ExchangeException ex) {
            ex.printStackTrace();
        } catch (BetPlaceException ex) {
            ex.printStackTrace();
        }
    }

    public BotEventTimeline getTimeline() {
        return raceBot.getTimeline();
    }
}
