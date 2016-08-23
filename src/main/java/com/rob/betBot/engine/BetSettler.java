package com.rob.betBot.engine;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Looks after the finished {@see RaceBot}s, waiting for them to settle
 * on the exchange.
 *
 * @author robert.haycock
 *
 */
@Component
public class BetSettler {

    private List<EventBot> finishedBots;

    public BetSettler() {
        finishedBots = new LinkedList<EventBot>();
    }

    public void addRaceBot(EventBot bot) {
        synchronized (finishedBots) {
            finishedBots.add(bot);
        }
    }

    public void addRaceBots(Collection<EventBot> bots) {
        synchronized (finishedBots) {
            finishedBots.addAll(bots);
        }
    }

    public void updateBets() {
        synchronized (finishedBots) {
            for (EventBot bot : finishedBots) {
                bot.updateBets();
            }
        }
    }
}
