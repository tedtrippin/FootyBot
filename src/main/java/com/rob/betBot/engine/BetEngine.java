package com.rob.betBot.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.rob.betBot.Event;
import com.rob.betBot.EventManager;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exchange.Exchange;

@Component
public class BetEngine implements Runnable, EventThreadListener {

    private static Logger log = LogManager.getLogger(BetEngine.class);

    @Autowired
    private BetSettler betSettler;

    @Autowired
    private EventThreadFactory raceThreadFactory;

    private Thread betEngineThread;
    private boolean running;
    private long interval = 24 * 60 * 60 * 1000; // 1 hour
    private Map<Long, EventBot> eventBotMap = new HashMap<>();

    // Map<ExchangeId, Map<RaceId, Race>> sorted by start time
    private Map<Integer, Map<String, Event>> raceMaps = new ConcurrentHashMap<>();

    private Map<String, EventThread> raceThreadMap = new ConcurrentHashMap<>();

    public BetEngine() {
        log.debug("Creating BetEngine");
    }

    @PostConstruct
    public void start() {

        if (betEngineThread != null)
            return;

        betEngineThread = new Thread(this);
        betEngineThread.start();
    }

    public void stop() {

        running = false;
        synchronized(betEngineThread) {
            betEngineThread.notifyAll();
        }
    }

    public Collection<EventBot> getBots() {
        return ImmutableList.<EventBot>builder().addAll(eventBotMap.values()).build();
    }

    public EventBot getBot(long id) {
        return eventBotMap.get(id);
    }

    @Override
    public void run() {

        log.debug("Started bet engine");

        try {
            while (!running) {

                checkForFinishedRaces();

                betSettler.updateBets();

                Thread.sleep(interval);
            }
        } catch (InterruptedException e) {
            log.warn("InterruptedException", e);
        }
    }

    /**
     * Adds a bot to a race so that it can "start working".
     *
     * @param event
     * @param bot
     * @param exchange
     */
    public void addBot(Event event, Bot bot, Exchange exchange) {

        EventThread raceThread = null;
        EventBot eventBot = new EventBot(bot, exchange);
        synchronized (eventBotMap) {
            eventBotMap.put(eventBot.getId(), eventBot);
        }

        synchronized (raceThreadMap) {
            String key = getKey(event);
            if (raceThreadMap.containsKey(key)) {
                EventThread eventThread = raceThreadMap.get(key);
                if (eventThread.isAlive()) {
                    eventThread.addBot(eventBot);
                    return;
                }
            }

            raceThread = raceThreadFactory.create(event, eventBot, this);
            raceThreadMap.put(key, raceThread);
        }
    }

    public void cancelBot(long botId) {

        log.debug("Cancelling bot[" + botId + "]");

        if (!eventBotMap.containsKey(botId)) {
            log.debug("  not found");
            return;
        }

        EventBot bot;
        synchronized (eventBotMap) {
            bot = eventBotMap.remove(botId);
        }

        for (EventThread eventThread : raceThreadFactory.getThreads()) {
            for (EventBot b : eventThread.getBots()) {
                if (b.getId() != botId)
                    continue;

                eventThread.cancelBot(bot);
                return;
            }
        }
    }

    public Collection<Event> getLoadedRaces(Exchange exchange) {

        Map<String, Event> raceMap = raceMaps.get(exchange.getExchangeId());
        if (raceMap == null)
            return new HashSet<Event>();

        return new ArrayList<Event>(raceMap.values());
    }

    /**
     * Load races for an exchange.
     *
     * @param exchange
     */
    public void loadRaces(Exchange exchange)
        throws ExchangeException {

        Map<String, Event> raceMap = raceMaps.get(exchange.getExchangeId());
        if (raceMap == null) {
            raceMap = new ConcurrentHashMap<String, Event>();
            raceMaps.put(exchange.getExchangeId(), raceMap);
        }

        EventManager raceManager = exchange.getEventManager();
        Collection<Event> races = raceManager.getEvents();
        for (Event race : races) {
            if (raceMap.containsKey(race.getEventData().getExchangeEventId()))
                continue;

            raceMap.put(race.getEventData().getExchangeEventId(), race);
        }
    }

    @Override
    public void eventThreadFinished(EventThread thread) {

        String key = getKey(thread.getEvent());
        log.debug("Event thread finished removing[" + key + "]");
        raceThreadMap.remove(key);

        synchronized (eventBotMap) {
            thread.getBots().forEach(b -> eventBotMap.remove(b.getId()));
        }
    }

    /**
     * Removes any finished races from exchangeRaceThreadMap.
     */
    private void checkForFinishedRaces() {

        Iterator<EventThread> raceThreads = raceThreadMap.values().iterator();
        while (raceThreads.hasNext()) {
            EventThread raceThread = raceThreads.next();
            if (raceThread.isfinished())
                raceThreads.remove();
        }
    }

    private String getKey(Event race) {
        return new StringBuilder()
            .append(race.getEventData().getExchangeId())
            .append('-')
            .append(race.getEventData().getExchangeEventId())
            .append('-')
            .append(race.getEventData().getExpectedStartTime()).toString();
    }
}
