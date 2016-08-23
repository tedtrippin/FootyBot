package com.rob.betBot.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.rob.betBot.Bet;
import com.rob.betBot.BetManager;
import com.rob.betBot.Event;
import com.rob.betBot.MarketPrices;
import com.rob.betBot.MarketType;
import com.rob.betBot.PricesManager;
import com.rob.betBot.SportEnum;
import com.rob.betBot.bots.BetPlacer;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.bots.Filter;
import com.rob.betBot.engine.events.BetPlacedEvent;
import com.rob.betBot.exception.BetCancelException;
import com.rob.betBot.exception.BetNotFoundException;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.NoSuchMarketException;
import com.rob.betBot.exchange.Exchange;

/**
 * An EventBot essentially represents a {@link Bot} that is in play, ie. belongs
 * to a {@link EventThread}. It contains the bot and the objects for updating/
 * tracking/placing an event/bets, ie. {@link PricesManager} and {@link BetManager}.
 *
 * @author robert.haycock
 *
 */
public class EventBot implements BetListener {

    private static Logger log = LogManager.getLogger(EventBot.class);

    private static AtomicLong ID_COUNTER = new AtomicLong(0);

    private long id;
    private final Exchange exchange;
    private final Bot bot;
    private BotEventTimeline timeline;
    private Event event;
    private Set<Bet> bets = new HashSet<>();
    private List<MarketPrices> prices =  new ArrayList<>(); // Initial bot needs this initialising

    public EventBot(Bot bot, Exchange exchange) {
        this.bot = bot;
        this.exchange = exchange;
        id = ID_COUNTER.getAndIncrement();
        timeline = new BotEventTimeline();
    }

    public long getId() {
        return id;
    }

    /**
     * Sets the event and prices. Called from {@link EventThread#addBot(EventBot)} to make
     * sure that all RaceBot's in a RaceThread have the same event object so we only have
     * to update one instance.
     *
     * @param event
     * @param prices
     */
    public void setEventAndPrices(Event event, List<MarketPrices> prices) {

        if (this.event != null)
            return;

        this.event = event;
        this.prices = prices;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public int getExchangeId() {
        return exchange.getExchangeId();
    }

    public BotEventTimeline getTimeline() {
        return timeline;
    }

    public String getUsername() {
        return exchange.getUsername();
    }

    public Collection<Bet> getBets() {
        return ImmutableList.<Bet>builder().addAll(bets).build();
    }

    public List<MarketPrices> getPrices() {
        return prices;
    }

    public boolean updateEvent(long currentTime)
        throws ExchangeException {

        return exchange.getPricesManager().updateEvent(event, currentTime);
    }

    public void updateBets() {

        synchronized (bets) {
            try {
                exchange.getBetManager().updateBets(bets);
            } catch (ExchangeException ex) {
                log.warn("Unable to update bets[exchange:" + exchange.getExchangeId() + "]", ex);
            } catch (BetNotFoundException ex) {
                log.warn("Bet not found", ex);
            }
        }
    }

    public void updated()
        throws BetPlaceException, ExchangeException {

        for (Filter filter : bot.getFilters())
            filter.apply(event, timeline);

        for (BetPlacer betPlacer : bot.getBetPlacers()) {
            Collection<Bet> latestBets = betPlacer.placeBets(event, exchange.getBetManager());
            synchronized (bets) {
                bets.addAll(latestBets);

                try {
                    long latestTime;
                    if (event.getEventData().getSport().equals(SportEnum.HORSE_RACING))
                        latestTime = event.getMarket(MarketType.WINNER).getLatestPrices().getPricesData().getTimeMs();
                    else
                        latestTime = event.getMarket(MarketType.MATCH_ODDS).getLatestPrices().getPricesData().getTimeMs();

                    latestBets.forEach(b -> timeline.addEvent(new BetPlacedEvent(latestTime)));
                } catch (NoSuchMarketException ex) {
                    log.error("Couldn't add bot event, couldn't find winner market", ex);
                }
            }
        }
    }

    public void cancelBet(Bet bet)
        throws ExchangeException, BetCancelException {

        if (!bets.contains(bet))
            return;

        exchange.getBetManager().cancelBets(Lists.newArrayList(bet));
    }

    public void cancelBets() {

        synchronized (bets) {
            try {
                log.debug("Cancelling bets");
                exchange.getBetManager().cancelBets(bets);
            } catch (ExchangeException | BetCancelException ex) {
                log.error("Unable to cancel bets", ex);
            }
        }
    }

    @Override
    public String toString() {
        return new StringBuilder("id:").append(id).toString();
    }

    @Override
    public void onBetPlaced(Bet bet) {
        // Bets already added by updated()
    }

    @Override
    public void onBetCancelled(Bet originalBet, Bet partMatchedBet) {

        synchronized (bets) {
            // Check the bet was part of this bot
            if (!bets.contains(originalBet))
                return;

            bets.remove(originalBet);

            if (partMatchedBet != null)
                bets.add(partMatchedBet);
        }
    }
}
