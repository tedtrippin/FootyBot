package com.rob.betBot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.model.EventData;
import com.rob.betBot.model.MarketData;
import com.rob.betBot.model.RunnerData;

public class TestRaceFactory {

    public static int ESTIMATED_RACE_DURATION_MS = 1000 * 60;
    /**
     * Creates a test race with 6 runners and test data that
     * starts now and lasts a minute.
     */
    public static Event createRace() {

        Set<Long> runnerIds = new HashSet<>();
        runnerIds.add(1L);
        runnerIds.add(2L);
        runnerIds.add(3L);
        runnerIds.add(4L);
        runnerIds.add(5L);
        runnerIds.add(6L);

        Collection<Runner> runners = new ArrayList<>();
        runners.add(new Runner(new RunnerData(1, Exchange.TEST_EXCHANGE_ID, 1, "1")));
        runners.add(new Runner(new RunnerData(2, Exchange.TEST_EXCHANGE_ID, 2, "2")));
        runners.add(new Runner(new RunnerData(3, Exchange.TEST_EXCHANGE_ID, 3, "3")));
        runners.add(new Runner(new RunnerData(4, Exchange.TEST_EXCHANGE_ID, 4, "4")));
        runners.add(new Runner(new RunnerData(5, Exchange.TEST_EXCHANGE_ID, 5, "5")));
        runners.add(new Runner(new RunnerData(6, Exchange.TEST_EXCHANGE_ID, 6, "6")));

        MarketData marketData = new MarketData(1, 1, "testMarketId", "Test market", MarketType.WINNER);
        Market market = new Market(marketData, runners);

        Collection<Market> markets = Lists.newArrayList(market);

        long startTime = System.currentTimeMillis() / 10000 * 10000; // Any past time with zeros on end

        EventData raceData = new EventData(
            1,
            UUID.randomUUID().toString(),
            Exchange.TEST_EXCHANGE_ID,
            "MyMadeUpCourse",
            "Dummy race for testing",
            SportEnum.HORSE_RACING,
            startTime);

        return new Event(raceData, markets, 60 * 1000);
    }
}
