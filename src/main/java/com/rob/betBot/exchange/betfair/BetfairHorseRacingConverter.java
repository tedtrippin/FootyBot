package com.rob.betBot.exchange.betfair;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.rob.betBot.BetBotConstants;
import com.rob.betBot.Event;
import com.rob.betBot.EventLoader;
import com.rob.betBot.Market;
import com.rob.betBot.MarketType;
import com.rob.betBot.Runner;
import com.rob.betBot.SportEnum;
import com.rob.betBot.exception.NoSuchMarketException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.betfair.model.MarketCatalogue;
import com.rob.betBot.exchange.betfair.model.RunnerCatalog;
import com.rob.betBot.model.EventData;
import com.rob.betBot.model.MarketData;
import com.rob.betBot.model.RunnerData;

public class BetfairHorseRacingConverter extends BetfairMarketConverter {

    private final Logger log = LogManager.getLogger(BetfairHorseRacingConverter.class);

    private final String TO_BE_PLACED = "to be placed";

    /**
     * Converts a list of betfair markets to a list of corresponding
     * horse racing {@link Event}s. If the event can't be found then a new one is
     * created and persisted. A betfair event represents a day at a
     * track so we have to group markets based on event ID and start time.
     *
     * @param markets
     * @return
     */
    public Collection<Event> convertEvents(List<MarketCatalogue> markets) {

        Collection<Event> events = new ArrayList<>();

        Multimap<String, MarketCatalogue> eventIdToMarketMap = ArrayListMultimap.create();
        for (MarketCatalogue market : markets) {
            String key = new StringBuilder(market.getEvent().getVenue()).append(' ')
                .append(market.getMarketStartTime()).toString();
            eventIdToMarketMap.put(key, market);
        }

        EventLoader eventLoader = new EventLoader();

        for (String key : eventIdToMarketMap.keySet()) {

            Collection<MarketCatalogue> marketCatalogues = eventIdToMarketMap.get(key);
            MarketCatalogue randomMarket = marketCatalogues.iterator().next();

            if (marketCatalogues.size() != 2) {
                log.warn("Can't load event[" + randomMarket.getEvent().getName() + "], it has " + marketCatalogues.size() + " markets");
                continue;
            }

            com.rob.betBot.exchange.betfair.model.Event betfairEvent = randomMarket.getEvent();
            String startTimeString = randomMarket.getMarketStartTime();
            long startTime;
            try {
                startTime = betfairHelper.fromBetfairDateString(startTimeString).getTime();
            } catch (ParseException ex) {
                log.warn("Can't load event[" + betfairEvent.getName() + "], couldn't parse start time[" + startTimeString + "]", ex);
                continue;
            }

            String betfairEventId = betfairEvent.getId();
            EventData eventData = eventDao.getByIdAndStartTime(Exchange.BETFAIR_EXCHANGE_ID, betfairEventId, startTime);
            if (eventData != null) {
                events.add(eventLoader.loadEvent(eventData));

            } else {
                long eventId = idGenerator.getNextId(EventData.class);
                try {
                    Event event = convertEvent(betfairEvent, eventId, startTime, marketCatalogues);
                    events.add(event);
                } catch (NoSuchMarketException ex) {
                    log.error("Unable to convert event, couldnt find all needed markers", ex);
                }
            }
        }

        return events;
    }

    private Event convertEvent(com.rob.betBot.exchange.betfair.model.Event betfairEvent, long eventId,
        long startTime, Collection<MarketCatalogue> marketCatalogues)
            throws NoSuchMarketException {

        String eventCourse = betfairEvent.getVenue();
        String eventName = getEventName(betfairEvent, marketCatalogues);
        EventData eventData = new EventData(eventId, betfairEvent.getId(), Exchange.BETFAIR_EXCHANGE_ID,
            eventCourse, eventName, SportEnum.HORSE_RACING, startTime);

        Collection<Market> markets = new ArrayList<>();
        for (MarketCatalogue betfairMarket : marketCatalogues) {

            MarketType marketType = getMarketType(betfairMarket);
            if (marketType == null) // Null means unsupported. Should probably make supported markets a parameter
                continue;

            // Create market data, but dont save
            long marketId = idGenerator.getNextId(MarketData.class);
            MarketData marketData = new MarketData(marketId++, eventId, betfairMarket.getMarketId(),
                betfairMarket.getMarketName(), marketType);
            eventData.addMarket(marketData);

            // Fetch existing runners or persist missing ones
            Collection<Runner> runners = new ArrayList<>();
            for (RunnerCatalog r : betfairMarket.getRunners()) {
                RunnerData runnerData = getRunnerData(r);
                marketData.addRunner(runnerData);
                runners.add(new Runner(runnerData));
            }

            // Now we can save market once the runners have been added
            marketDao.saveOrUpdate(marketData);

            Market market = new Market(marketData, runners);
            markets.add(market);
        }

        eventDao.saveOrUpdate(eventData);

        Event event = new Event(eventData, markets, BetBotConstants.FOOTBALL_MATCH_DURATION_MS);
        return event;
    }

    private MarketType getMarketType(MarketCatalogue betfairMarket) {

        switch (betfairMarket.getMarketName().toLowerCase()) {
            case TO_BE_PLACED :
                return MarketType.TO_BE_PLACED;
            default :
                return MarketType.WINNER;
        }
    }

    private String getEventName(com.rob.betBot.exchange.betfair.model.Event betfairEvent,
        Collection<MarketCatalogue> marketCatalogues)
            throws NoSuchMarketException {

        for (MarketCatalogue marketCatalogue : marketCatalogues) {
            String marketName = marketCatalogue.getMarketName();
            if (!marketName.equalsIgnoreCase(TO_BE_PLACED)) {
                marketName = marketName.replaceFirst("R[0-9]+ ", "");
                return betfairEvent.getVenue() + " " + marketName;
            }
        }

        throw new NoSuchMarketException(Exchange.BETFAIR_EXCHANGE_ID, betfairEvent.getId(), MarketType.WINNER);
    }
}

