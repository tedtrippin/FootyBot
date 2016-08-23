package com.rob.betBot.exchange.combined;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.rob.betBot.Bet;
import com.rob.betBot.BetPricesExchangeInfo;
import com.rob.betBot.BetRequest;
import com.rob.betBot.BetRequestImpl;
import com.rob.betBot.Event;
import com.rob.betBot.Market;
import com.rob.betBot.MarketType;
import com.rob.betBot.Runner;
import com.rob.betBot.conf.TeamNameConverter;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.NoSuchEventException;
import com.rob.betBot.exception.NoSuchMarketException;
import com.rob.betBot.exception.NoSuchRunnerException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.betfair.BetfairBet;
import com.rob.betBot.exchange.betfair.BetfairBetManager;
import com.rob.betBot.exchange.betfair.BetfairFootballConverter;
import com.rob.betBot.exchange.betfair.BetfairHelper;
import com.rob.betBot.exchange.betfair.jsonrpc.BetfairJsonLoginCommunicator;
import com.rob.betBot.exchange.betfair.jsonrpc.BetfairJsonRpcCommunicator;
import com.rob.betBot.exchange.betfair.model.MarketCatalogue;
import com.rob.betBot.exchange.betfair.model.MarketFilter;

/**
 * Bet manager that expects William Hill event information but tries
 * to place bets on Betfair by looking up events/markets with same
 * name.
 */
public class WilliamHillBetfairFootballBetManager extends BetfairBetManager implements eventbot {

    private static Logger log = LogManager.getLogger(WilliamHillBetfairFootballBetManager.class);

    // Cache map of William Hill event IDs to Befair events
    private final static Cache<String, Event> whEVentIdToBetfairEventCache;

    private final static Pattern WHITESPACE_STRIPPER = Pattern.compile("\\s+");
    private final static String MIDDLE_BIT = " v ";

    private final BetfairHelper betfairHelper = new BetfairHelper();
    private final BetfairFootballConverter betfairConverter;

    static {
        whEVentIdToBetfairEventCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(59, TimeUnit.SECONDS)
            .build();
    }

    public WilliamHillBetfairFootballBetManager(BetfairJsonLoginCommunicator betfairSso, BetfairFootballConverter betfairConverter) {
        super(new BetfairJsonRpcCommunicator(betfairSso.getSessionToken()));
        this.betfairConverter = betfairConverter;
    }

    @Override
    public Collection<Bet> placeBets(Collection<BetRequest> whBetRequests)
        throws ExchangeException, BetPlaceException {

        // Keep map of betfair runner ID to the WH event/market/runner IDs
        Map<Long, BetPricesExchangeInfo> bfRunnerIdToWhBetRequestMap = new HashMap<>();

        Collection<BetRequest> betfairRequests = new ArrayList<>();
        for (BetRequest whRequest : whBetRequests) {
            try {
                BetDetails betfairDetails = getBetfairDetails(whRequest);
                BetRequest betfairBetRequest = new BetRequestImpl(whRequest.getPrice(), whRequest.getExchangePrice(),
                    whRequest.getAmount(), betfairDetails.eventId, whRequest.getEventName(), betfairDetails.marketId,
                    whRequest.getMarketType(), betfairDetails.runnerId, whRequest.getRunnerName(), whRequest.getBetType());
                betfairRequests.add(betfairBetRequest);

                BetPricesExchangeInfo whInfo = new BetPricesExchangeInfo(whRequest.getEventId(),
                    whRequest.getMarketId(), whRequest.getRunnerId());
                bfRunnerIdToWhBetRequestMap.put(betfairDetails.runnerId, whInfo);
            } catch (NoSuchEventException | NoSuchMarketException | NoSuchRunnerException ex) {
                log.warn("Couldnt find details on betfair", ex);
            }
        }

        Collection<Bet> betfairBets = super.placeBets(betfairRequests);

        // Update the bets with the corresponding WH data
        betfairBets.forEach(b -> ((BetfairBet)b).setPricesInfo(bfRunnerIdToWhBetRequestMap.get(b.getRunnerId())));

        return betfairBets;
    }

    private BetDetails getBetfairDetails(BetRequest whBetRequest)
        throws ExchangeException, NoSuchEventException, NoSuchMarketException, NoSuchRunnerException {

        Event betfairEvent = getBetfairEvent(whBetRequest.getEventId(), whBetRequest.getEventName());
        if (betfairEvent == null)
            throw new NoSuchEventException(Exchange.BETFAIR_EXCHANGE_ID, -1, whBetRequest.getEventName());

        Market betfairMarket = getBetfairMarket(whBetRequest.getMarketType(), betfairEvent);
        if (betfairMarket == null) {
            throw new NoSuchMarketException(Exchange.BETFAIR_EXCHANGE_ID, betfairEvent.getEventData().getExchangeEventId(),
                whBetRequest.getMarketType());
        }

        Runner betfairRunner = getBetfairRunner(whBetRequest.getRunnerName(), betfairMarket, whBetRequest.getEventName());
        if (betfairRunner == null) {
            throw new NoSuchRunnerException(Exchange.BETFAIR_EXCHANGE_ID, betfairEvent.getEventData().getExchangeEventId(),
                betfairMarket.getMarketData().getExchangeMarketId(), 0, whBetRequest.getRunnerName());
        }

        return new BetDetails(betfairEvent.getEventData().getExchangeEventId(),
            betfairMarket.getMarketData().getExchangeMarketId(), betfairRunner.getRunnerData().getExchangeRunnerId());
    }

    /**
     *  Tries to find the event on Betfair by searching for the event name.
     *  The event names on William Hill and Betfair look identical in general,
     *  just need to remove extraneous spaces
     *
     * @param event William Hill event
     * @return
     * @throws NoSuchEventException
     * @throws ExchangeException
     */
    private Event getBetfairEvent(String whEventId, String whEventName)
        throws ExchangeException {

        if (log.isDebugEnabled())
            log.debug("looking for Betfair event[" + whEventName + "]");

        Event betfairEvent = whEVentIdToBetfairEventCache.getIfPresent(whEventId);
        if (betfairEvent != null) {
            log.debug("  found in cache");
            return betfairEvent;
        }

        log.debug("  searching on Betfair");
        String searchString = buildSearchString(whEventName);

        MarketFilter filter = betfairHelper.getFootballFilter();
        filter.setTextQuery(searchString);
        List<MarketCatalogue> betfairMarkets = betfair.listMarketCatalogue(filter, 9);

        if (betfairMarkets == null || betfairMarkets.size() == 0)
            return null;

        Collection<Event> events = betfairConverter.convertEvents(betfairMarkets);

        if (events.size() == 1) {
            betfairEvent = events.iterator().next();

        } else {
            // If betfair returns more than one event then sort by Levenshtein distance
            if (log.isDebugEnabled())
                log.debug("  found multiple events, looking for closest event name");

            SortedMap<Integer, Event> sortedEventMap = new TreeMap<>();
            for (Event event : events) {
                String name = event.getEventData().getEventName();
                int distance = StringUtils.getLevenshteinDistance(searchString, name);
                log.debug("  " + name + " - distance " + distance);
                sortedEventMap.put(distance, event);
            }
            betfairEvent = sortedEventMap.values().iterator().next();
            log.debug("  using[" + betfairEvent.getEventData().getEventName() + "]");
        }

        whEVentIdToBetfairEventCache.put(whEventId, betfairEvent);

        return betfairEvent;
    }

    private Market getBetfairMarket(MarketType marketType, Event betfairEvent) {

        for (Market betfairMarket : betfairEvent.getMarkets()) {
            if (betfairMarket.getMarketData().getMarketType() == marketType)
                return betfairMarket;
        }
        return null;
    }

    private Runner getBetfairRunner(String whRunnerName, Market betfairMarket, String eventName) {

        String normalizedRunnerName = normalize(whRunnerName);
        if (betfairMarket.getMarketData().getMarketType() == MarketType.CORRECT_SCORE)
            normalizedRunnerName = normalizeCorrectScore(normalizedRunnerName, eventName);

        for (Runner betfairRunner : betfairMarket.getRunners()) {
            String betfairRunnerName = betfairRunner.getRunnerData().getRunnerName();
            betfairRunnerName = normalize(betfairRunnerName);
            if (betfairRunnerName.equals(normalizedRunnerName))
                return betfairRunner;
        }
        return null;
    }

    /**
     * Returns the runner name with extra spaces removed.
     *
     * @param s
     * @return
     */
    private String normalize(String s) {

        Matcher matcher = WHITESPACE_STRIPPER.matcher(s.toLowerCase());
        String normalised = matcher.replaceAll(" ");

        return normalised;
    }

    /**
     * For Arsenal v Aston Villa, Betfair would be eg "0 - 3" but WH
     * would be "Aston Villa 3-0".
     *
     * @param whRunnerName
     * @param eventName
     * @return
     */
    protected String normalizeCorrectScore(String whRunnerName, String eventName) {

        try {
            eventName = eventName.toLowerCase();

            // Get the home team
            int idx = eventName.indexOf(" v ");
            String runner1 = eventName.substring(0, idx);

            // Check if the score is reversed
            boolean swap = !whRunnerName.toLowerCase().startsWith(runner1);

            // Remove the team name
            idx = whRunnerName.lastIndexOf(' ');
            StringBuilder normalizedCorrectScore = new StringBuilder(whRunnerName.substring(idx+1));

            if (swap)
                normalizedCorrectScore.reverse();

            // Add spaces either side of the dash to make like betfair
            idx = normalizedCorrectScore.indexOf("-");
            normalizedCorrectScore.insert(idx + 1, " ").insert(idx, " ");

            return normalizedCorrectScore.toString();
        } catch (Exception ex) {
            log.warn("Couldn't normalise WH runner[" + whRunnerName + "] for event[" + eventName + "]", ex);
        }

        return "";
    }

    private String buildSearchString(String whEventName) {

        try {
            String eventName = normalize(whEventName);

            eventName = eventName.toLowerCase();

            // Get the home team
            int idx = eventName.indexOf(MIDDLE_BIT);
            String runner1 = eventName.substring(0, idx);
            String runner2 = eventName.substring(idx + MIDDLE_BIT.length());

            String bfRunner1 = TeamNameConverter.getBetfairTeamName(runner1);
            String bfRunner2 = TeamNameConverter.getBetfairTeamName(runner2);

            StringBuilder bfEventName = new StringBuilder(bfRunner1 == null ? runner1 : bfRunner1)
                .append(MIDDLE_BIT)
                .append(bfRunner2 == null ? runner2 : bfRunner2);

            return bfEventName.toString();

        } catch (Exception ex) {
            log.warn("Couldn't build search string out of[" + whEventName + "]", ex);
        }

        return whEventName;
    }


    static class BetDetails {

        public final String eventId;
        public final String marketId;
        public final long runnerId;

        public BetDetails(String eventId, String marketId, long runnerId) {
            this.eventId = eventId;
            this.marketId = marketId;
            this.runnerId = runnerId;
        }
    }
}
