package com.rob.betBot.exchange.williamHill;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rob.betBot.MarketType;
import com.rob.betBot.SportEnum;
import com.rob.betBot.conf.Property;
import com.rob.betBot.conf.PropertyKey;
import com.rob.betBot.dao.EventDao;
import com.rob.betBot.dao.IdGenerator;
import com.rob.betBot.dao.MarketDao;
import com.rob.betBot.dao.RunnerDao;
import com.rob.betBot.exception.ExpiredException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.williamHill.model.WHEvent;
import com.rob.betBot.exchange.williamHill.model.WHMarket;
import com.rob.betBot.exchange.williamHill.model.WHSelection;
import com.rob.betBot.model.EventData;
import com.rob.betBot.model.MarketData;
import com.rob.betBot.model.RunnerData;
import com.rob.betBot.model.wh.WhEventParentData;

@Component
public class WilliamHillEventLoader {

    private final static Logger log = LogManager.getLogger(WilliamHillEventLoader.class);
//  private final static String ZERO_OFFSET = ("0 00:00:00");
    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private RunnerDao runnerDao;

    @Autowired
    private MarketDao marketDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private WilliamHillFootyEventManager footyEventManager;

    private WhEventParentData updating;

    /**
     * Loads events for the requested event parent.
     *
     * @param eventParent
     */
    public void loadEvent(WhEventParentData eventParent) {

        synchronized (this) {
            if (updating != null) {
                log.debug("Not reloading events, already loading");
                return;
            }

            updating = eventParent;
        }

        final String url = eventParent.getConnectString();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    log.debug("Reloading events");
                    try {
                        loadLeague(url);
                        footyEventManager.loadEvents();
                    } finally {
                        updating = null;
                    }
                } catch (Exception ex) {
                    log.error("Error updating events", ex);
                }
            }
        });
        t.start();
    }

    public WhEventParentData getEventParent() {
        return updating;
    }

    private void loadLeague(String url)
        throws IOException, ParseException {

        log.debug("Scraping for events url[" + url + "]");

        // Get in-play and out-play events
        String[] startingStrings = new String[] {getEventsInPlayStartString(), getEventsOutPlayStartString()};
        WilliamHillScraper scraper = new WilliamHillScraper(url);
        List<WHEvent> events = scraper.scrapeAll(getEventsEndString(), WHEvent.class, startingStrings);
        if (events.isEmpty()) {
            log.debug("No events for[" + url + "], wait 3 seconds and try again");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
            }
            events = scraper.scrapeAll(getEventsEndString(), WHEvent.class, startingStrings);
        }

        for (WHEvent event : events) {
            loadEventWithMarkets(event, true);
        }
    }

    private void loadEventWithMarkets(WHEvent eventWithoutMarkets, boolean isInPlay)
        throws IOException, ParseException {

        log.debug("  loading markets for event[" + eventWithoutMarkets.getName() + "]");
        WilliamHillScraper scraper = new WilliamHillScraper(eventWithoutMarkets.getEvent_link());
        try {
            WHEvent event = scraper.scrape(getMarketsEndString(), getEventsExpiredString(), WHEvent.class, getMarketsStartString());
            if (event == null) {
                log.debug("Unable to get event with markets for event[" + eventWithoutMarkets.getName() + "] going to wait 3 seconds and try again");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                }
                event = scraper.scrape(getMarketsEndString(), getEventsExpiredString(), WHEvent.class, getMarketsStartString());
                if (event == null) {
                    log.debug("Still couldnt load, giving up");
                    return;
                }
            }

            convertAnddAddEvent(event, isInPlay);

        } catch (ExpiredException ex) {
            log.debug("  couldnt scrape, already expired");
        }
    }

    private void convertAnddAddEvent(WHEvent whEvent, boolean isInPlay)
        throws ParseException {

        EventData eventData = eventDao.getById(Exchange.WILLIAM_HILL_EXCHANGE_ID, whEvent.getEvent());
        if (eventData != null)
            return;

        if (whEvent.getMarkets() == null) {
            log.debug("Event[" + whEvent.getName() + "] has null markets");
            return;
        }

        Date date = DATE_FORMAT.parse(whEvent.getStart_time());
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(date);

        // TODO - not sure what the offset is, might account for half time
        //adjustDate(startTime, whEvent.getOffset());

        long startTimeMs = startTime.getTimeInMillis();
        long eventId = idGenerator.getNextId(EventData.class);
        eventData = new EventData(eventId, whEvent.getEvent(), Exchange.WILLIAM_HILL_EXCHANGE_ID, "",
            whEvent.getName(), SportEnum.FOOTBALL, startTimeMs);

        for (WHMarket whMarket : whEvent.getMarkets()) {

            MarketType marketType = WilliamHillHelper.getMarketType(whMarket.getMkt_name());
            if (marketType == null)
                continue;

            String marketId = whMarket.getEv_mkt_id();
            long id = idGenerator.getNextId(MarketData.class);
            MarketData marketData = new MarketData(id, eventId, marketId, whMarket.getMkt_name(), marketType);
            eventData.addMarket(marketData);

            for (WHSelection selection : whEvent.getSelections()) {
                if (!selection.getEv_mkt_id().equals(marketId))
                    continue;

                long runnerId = Long.valueOf(selection.getEv_oc_id());

                RunnerData runnerData = runnerDao.getRunnerByRunnerId(Exchange.WILLIAM_HILL_EXCHANGE_ID, runnerId);
                if (runnerData == null) {
                    id = idGenerator.getNextId(RunnerData.class);
                    runnerData = new RunnerData(id, Exchange.WILLIAM_HILL_EXCHANGE_ID, runnerId, selection.getName());
                    runnerDao.saveOrUpdate(runnerData);
                }
                marketData.addRunner(runnerData);
            }
            marketDao.saveOrUpdate(marketData);
        }

        eventDao.saveOrUpdate(eventData);
    }

/*
    private void adjustDate(Calendar date, String offset) {

        if (offset.equals(ZERO_OFFSET))
            return;

        try {
            String[] offsets = offset.trim().split("[ :]");
            date.add(Calendar.DAY_OF_MONTH, Integer.parseInt(offsets[0]));
            date.add(Calendar.HOUR, Integer.parseInt(offsets[1]));
            date.add(Calendar.MINUTE, Integer.parseInt(offsets[2]));
        } catch (NumberFormatException ex) {
            log.error("Unexpected offset format[" + offset + "]");
        }
    }
*/

    private String getEventsOutPlayStartString() {
        return Property.getProperty(PropertyKey.WILLIAM_HILL_EVENTS_OUTPLAY_START_STRING);
    }

    private String getEventsInPlayStartString() {
        return Property.getProperty(PropertyKey.WILLIAM_HILL_EVENTS_INPLAY_START_STRING);
    }

    private String getEventsEndString() {
        return Property.getProperty(PropertyKey.WILLIAM_HILL_EVENTS_END_STRING);
    }

    private String getEventsExpiredString() {
        return Property.getProperty(PropertyKey.WILLIAM_HILL_EVENTS_EXPIRED_STRING);
    }

    private String getMarketsStartString() {
        return Property.getProperty(PropertyKey.WILLIAM_HILL_MARKETS_START_STRING);
    }

    private String getMarketsEndString() {
        return Property.getProperty(PropertyKey.WILLIAM_HILL_MARKETS_END_STRING);
    }
}
