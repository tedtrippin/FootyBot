package com.rob.betBot.exchange.betfair;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.rob.betBot.BetBotConstants;
import com.rob.betBot.Event;
import com.rob.betBot.EventLoader;
import com.rob.betBot.Market;
import com.rob.betBot.MarketType;
import com.rob.betBot.Runner;
import com.rob.betBot.SportEnum;
import com.rob.betBot.dao.EventDao;
import com.rob.betBot.dao.IdGenerator;
import com.rob.betBot.dao.MarketDao;
import com.rob.betBot.dao.RunnerDao;
import com.rob.betBot.exception.NoSuchMarketException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.betfair.model.MarketCatalogue;
import com.rob.betBot.exchange.betfair.model.RunnerCatalog;
import com.rob.betBot.model.EventData;
import com.rob.betBot.model.MarketData;
import com.rob.betBot.model.RunnerData;

public class BetfairFootballConverter extends BetfairMarketConverter {

    private final Logger log = LogManager.getLogger(BetfairFootballConverter.class);

    @Autowired
    private RunnerDao runnerDao;

    @Autowired
    private MarketDao marketDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private IdGenerator idGenerator;

    /**
     * Converts a list of betfair markets to a list of corresponding
     * {@link Event}s. If the event can't be found then a new one is
     * created and persisted. A betfair
     *
     * @param markets
     * @return
     */
    public Collection<Event> convertEvents(List<MarketCatalogue> markets) {

        Collection<Event> events = new ArrayList<>();

        Multimap<com.rob.betBot.exchange.betfair.model.Event, MarketCatalogue> eventIdToMarketMap = ArrayListMultimap.create();
        for (MarketCatalogue market : markets) {
            eventIdToMarketMap.put(market.getEvent(), market);
        }

        EventLoader eventLoader = new EventLoader();

        for (com.rob.betBot.exchange.betfair.model.Event betfairEvent : eventIdToMarketMap.keySet()) {

//            EventData eventData = eventDao.getById(Exchange.BETFAIR_EXCHANGE_ID, betfairEvent.getId());
//            if (eventData != null) {
//                events.add(eventLoader.loadEvent(eventData));
//
//            } else {
                Collection<MarketCatalogue> marketCatalogues = eventIdToMarketMap.get(betfairEvent);
                long eventId = idGenerator.getNextId(EventData.class);
                try {
                    Event event = convertEvent(betfairEvent, eventId, SportEnum.FOOTBALL, marketCatalogues);
                    events.add(event);
                } catch (NoSuchMarketException ex) {
                    log.error("Unable to convert event, couldnt find all needed markers", ex);
                }
//            }
        }

        return events;
    }

    private Event convertEvent(com.rob.betBot.exchange.betfair.model.Event betfairEvent, long eventId,
        SportEnum sport, Collection<MarketCatalogue> marketCatalogues)
            throws NoSuchMarketException {

        String eventCourse = betfairEvent.getVenue();
        String eventName = betfairEvent.getName();
        long startTime = 0;
        try {
            startTime = betfairHelper.fromBetfairDateString(betfairEvent.getOpenDate()).getTime();
        } catch (ParseException ex) {
        }

        EventData eventData = new EventData(eventId, betfairEvent.getId(), Exchange.BETFAIR_EXCHANGE_ID,
            eventCourse, eventName, sport, startTime);

        Collection<Market> markets = new ArrayList<>();
        long marketId = 1;
        for (MarketCatalogue betfairMarket : marketCatalogues) {

            MarketType marketType = getMarketType(betfairMarket);
            if (marketType == null) // Null means unsupported. Should probably make supported markets a parameter
                continue;

            MarketData marketData = new MarketData(marketId++, eventId, betfairMarket.getMarketId(),
                betfairMarket.getMarketName(), marketType);
            eventData.addMarket(marketData);
            marketDao.saveOrUpdate(marketData);

            Collection<Runner> runners = new ArrayList<>();
            for (RunnerCatalog r : betfairMarket.getRunners()) {
                RunnerData runnerData = getRunnerData(r);
                marketData.addRunner(runnerData);
                runners.add(new Runner(runnerData));
            }

            Market market = new Market(marketData, runners);
            markets.add(market);
        }

        eventDao.saveOrUpdate(eventData);

        Event event = new Event(eventData, markets, BetBotConstants.FOOTBALL_MATCH_DURATION_MS);
        return event;
    }

    private MarketType getMarketType(MarketCatalogue betfairMarket) {

        switch (betfairMarket.getMarketName().toLowerCase()) {
            case "correct score" :
                return MarketType.CORRECT_SCORE;
            case "match odds" :
                return MarketType.WINNER;
            default :
                return null;
        }
    }
}

