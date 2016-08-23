package com.rob.betBot.exchange.emulated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.rob.betBot.Event;
import com.rob.betBot.EventManager;
import com.rob.betBot.Market;
import com.rob.betBot.MarketType;
import com.rob.betBot.Runner;
import com.rob.betBot.SportEnum;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.model.EventData;
import com.rob.betBot.model.MarketData;
import com.rob.betBot.model.RunnerData;

public class EmulatedRaceManager implements EventManager {

    @Override
    public Collection<Event> getEvents() {
        return ImmutableList.of(createRace());
    }

    @Override
    public Event getEvent(long id) {
        return createRace();
    }

    private Event createRace() {

        List<Runner> runners = new ArrayList<>(6);
        runners.add(new Runner(new RunnerData(1, Exchange.TEST_EXCHANGE_ID, 1, "Horse 1")));
        runners.add(new Runner(new RunnerData(2, Exchange.TEST_EXCHANGE_ID, 2, "Horse 2")));
        runners.add(new Runner(new RunnerData(3, Exchange.TEST_EXCHANGE_ID, 3, "Horse 3")));
        runners.add(new Runner(new RunnerData(4, Exchange.TEST_EXCHANGE_ID, 4, "Horse 4")));
        runners.add(new Runner(new RunnerData(5, Exchange.TEST_EXCHANGE_ID, 5, "Horse 5")));
        runners.add(new Runner(new RunnerData(6, Exchange.TEST_EXCHANGE_ID, 6, "Horse 6")));

        Set<Long> runnerIds = new HashSet<>();
        runnerIds.add(1L);
        runnerIds.add(2L);
        runnerIds.add(3L);
        runnerIds.add(4L);
        runnerIds.add(5L);
        runnerIds.add(6L);

        MarketData winnerMarketData = new MarketData(1, 1, "testMarketId", "Test winner market", MarketType.WINNER);
        Market winnerMarket = new Market(winnerMarketData, runners);

        MarketData toBePlacedMarketData = new MarketData(2, 2, "testMarketId", "Test to-be-placed market", MarketType.TO_BE_PLACED);
        Market toBePlacedMarket = new Market(toBePlacedMarketData, runners);

        Collection<Market> markets = Lists.newArrayList(winnerMarket, toBePlacedMarket);

        long startTime = System.currentTimeMillis() / 10000 * 10000; // Any past time with zeros on end

        EventData raceData = new EventData(
            -1,
            UUID.randomUUID().toString(),
            Exchange.TEST_EXCHANGE_ID,
            "MyMadeUpCourse",
            "Dummy race for testing",
            SportEnum.HORSE_RACING,
            startTime);

        Event event = new Event(raceData, markets, 60 * 1000);
        event.started(startTime);

        return event;
    }

    @Override
    public void loadEvents() {
    }
}
