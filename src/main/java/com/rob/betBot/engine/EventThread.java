package com.rob.betBot.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rob.betBot.Event;
import com.rob.betBot.EventManager;
import com.rob.betBot.MarketPrices;
import com.rob.betBot.dao.IdGenerator;
import com.rob.betBot.dao.RaceTimeDao;
import com.rob.betBot.exception.ExchangeException;

/**
 * Represents an actual race. Bots can be added to a Race. Any bets resulting from
 * the bots will be added to a {@link BetSettler} once the race has finished.
 *
 * @author robert.haycock
 *
 */
public class EventThread implements Runnable {

    private static Logger log = LogManager.getLogger(EventThread.class);
    private static Logger journal = LogManager.getLogger("journal");

//    private PricesDao pricesDao;

    private Thread thread;
    private final BetSettler betSettler;
    private final EventThreadListener eventThreadListener;
    private final RaceTimeDao raceTimeDao;
    private final IdGenerator idGenerator;
    private final List<MarketPrices> prices = new ArrayList<>();
//    private boolean recordPrices;
    private boolean alive;
    private List<EventBot> bots;
    private int exchangeId;
    protected final Event event;
    protected EventManager raceManager;

    public EventThread(Event event, EventBot initialBot, BetSettler betSettler,
        RaceTimeDao raceTimeDao, IdGenerator idGenerator, EventThreadListener eventThreadListener) {

        this.event = event;
        this.betSettler = betSettler;
        this.raceTimeDao = raceTimeDao;
        this.idGenerator = idGenerator;
        this.eventThreadListener = eventThreadListener;
        exchangeId = initialBot.getExchangeId();

        bots = new LinkedList<EventBot>();
        bots.add(initialBot);
        initialBot.setEvent(event);
    }

    public void addBot(EventBot bot) {
        bot.setEventAndPrices(event, prices);
        synchronized (bots) {
            bots.add(bot);
        }
    }

    public void cancelBot(EventBot bot) {

        if (!bots.contains(bot))
            return;

        bot.cancelBets();
        synchronized (bots) {
            bots.remove(bot);
        }
    }

    public List<EventBot> getBots() {
        return new ArrayList<>(bots);
    }

    public boolean isfinished() {
        return event.isFinished();
    }

//    public void setRecordPrices(boolean recordPrices) {
//        this.recordPrices = recordPrices;
//    }

    public int getExchangeId() {
        return exchangeId;
    }

    public Event getEvent() {
        return event;
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public void run() {

        alive = true;

        log.debug("EventThread[" + event.getEventData().getEventName() + "] started");
        journal.info("Started a thread for " + event.getEventData().getEventName());
        try {
            do {
                synchronized (this) {
                    wait(getInterval());
                }

                if (updateEvent()) {
                    List<EventBot> botsClone = new ArrayList<>(bots);
                    for (EventBot bot : botsClone) {
                        try {
                            bot.updated();
                        } catch (Exception ex) {
                            log.error("Unable to update bot", ex);
                        }
                    }

                    if (bots.isEmpty())
                        return;
                }
            } while (!event.isFinished());

            finished();

        } catch (InterruptedException ex) {
            log.error("Interupted", ex);
            return;
        } catch (Exception ex) {
            log.fatal("Unexpected exception, killing EventThread and adding bots to BetSettler", ex);
            betSettler.addRaceBots(bots);
            return;
        } finally {
            teardown();
        }

        log.debug("EventThread[" + event.getEventData().getId() + "] finished");
    }

    void start() {

        if (thread != null)
            return;

        thread = new Thread(this);
        thread.start();
    }

    private void finished() {

        log.debug("Finished");
        if (!event.isFinished())
            return;

        if (raceTimeDao == null)
            return;

        long startTime = event.getEventData().getActualStartTime();
        if (startTime <= 0)
            return;

        long finishTime = event.getEventData().getFinishedTime();
        long duration = finishTime - startTime;
        String eventName = event.getEventData().getEventName();
        log.debug("Race[" + eventName + "] finished, time[" + duration + "] previous time[" + event.getEstimatedDurationMS() +
            "] start[" + startTime + "] finish[" + finishTime + "]");

//
//        RaceTimeData raceTimeData = raceTimeDao.getRaceTime(eventName);
//        if (raceTimeData == null) {
//            log.debug("  inserting race time for [" + eventName + "], time[" + duration + "]");
//            long id = idGenerator.getNextId(RaceTimeData.class);
//            raceTimeData = new RaceTimeData(id, eventName, duration);
//
//        } else {
//            log.debug("  updating race time for [" + eventName + "], old[" + raceTimeData.getDurationMs() + "] new[" + duration + "]");
//            raceTimeData.setDurationMs(duration);
//        }
//
//        raceTimeDao.saveOrUpdate(raceTimeData);
    }

    private void teardown() {

        alive = false;

        log.info("Race finished, updating bets");
        betSettler.addRaceBots(bots);

        eventThreadListener.eventThreadFinished(this);

        thread = null;
    }

    private boolean updateEvent() {

        if (bots.isEmpty())
            return false;

        // Round robin, add the first bot to back of the list
        EventBot bot;
        synchronized (bots) {
            bot = bots.remove(0);
            bots.add(bot);
        }

        try {
            return bot.updateEvent(System.currentTimeMillis());
        } catch (ExchangeException ex) {
            log.error("Unable to update bot", ex);
            return false;
        }

//        if (recordPrices)
//            pricesDao.saveOrUpdate(racePrices.getPricesData());

    }

    /**
     * Work out how long to wait between updates.
     * @return
     */
    private long getInterval() {

        // Return 1s when in play
        if (event.isInPlay())
            return 1000;

        // Return 5s when a minute before start time
        long timeTillStart = event.getEventData().getExpectedStartTime() - System.currentTimeMillis();
        if (timeTillStart < 0)
            return 1000;

        if (timeTillStart < 60000)
            return 5000;

        // All other times return 30s
        return 30000;
    }
}
