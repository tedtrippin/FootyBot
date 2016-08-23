package com.rob.betBot.exchange.emulated;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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


public class TestRaceManager implements EventManager {

    @Override
    public Collection<Event> getEvents() {

        List<Event> races = new LinkedList<Event>();

        List<Runner> runners1 = new LinkedList<Runner>();
        runners1.add(new Runner(new RunnerData(1, Exchange.TEST_EXCHANGE_ID, 1, "Horse A")));
        runners1.add(new Runner(new RunnerData(2, Exchange.TEST_EXCHANGE_ID, 2, "Horse B")));
        runners1.add(new Runner(new RunnerData(3, Exchange.TEST_EXCHANGE_ID, 3, "Horse C")));
        runners1.add(new Runner(new RunnerData(4, Exchange.TEST_EXCHANGE_ID, 4, "Horse D")));
        runners1.add(new Runner(new RunnerData(5, Exchange.TEST_EXCHANGE_ID, 5, "Horse E")));
        MarketData marketData = new MarketData(1, 1, "1", "market 1", MarketType.WINNER);
        Market market = new Market(marketData, runners1);
        long startTime = System.currentTimeMillis() + 1000;
        EventData raceData = new EventData(1, "0", Exchange.TEST_EXCHANGE_ID, "Newbury", "1st", SportEnum.HORSE_RACING, startTime);
        races.add(new Event(raceData, Lists.newArrayList(market), 60000));

        List<Runner> runners2 = new LinkedList<Runner>();
        runners2.add(new Runner(new RunnerData(7, Exchange.TEST_EXCHANGE_ID, 6, "Horse 1")));
        runners2.add(new Runner(new RunnerData(8, Exchange.TEST_EXCHANGE_ID, 7, "Horse 2")));
        runners2.add(new Runner(new RunnerData(9, Exchange.TEST_EXCHANGE_ID, 8, "Horse 3")));
        runners2.add(new Runner(new RunnerData(10, Exchange.TEST_EXCHANGE_ID, 9, "Horse 4")));
        runners2.add(new Runner(new RunnerData(11, Exchange.TEST_EXCHANGE_ID, 10, "Horse 5")));
        marketData = new MarketData(2, 2, "2", "market 2", MarketType.WINNER);
        market = new Market(marketData, runners2);
        startTime = System.currentTimeMillis() + 1000;
        raceData = new EventData(2, "0", 2, "Newbury", "2nd", SportEnum.HORSE_RACING, startTime);
        races.add(new Event(raceData, Lists.newArrayList(market), 60000));

        List<Runner> runners3 = new LinkedList<Runner>();
        runners3.add(new Runner(new RunnerData(12, Exchange.TEST_EXCHANGE_ID, 11, "Horse a1")));
        runners3.add(new Runner(new RunnerData(13, Exchange.TEST_EXCHANGE_ID, 12, "Horse b2")));
        runners3.add(new Runner(new RunnerData(14, Exchange.TEST_EXCHANGE_ID, 13, "Horse c3")));
        runners3.add(new Runner(new RunnerData(15, Exchange.TEST_EXCHANGE_ID, 14, "Horse d4")));
        runners3.add(new Runner(new RunnerData(16, Exchange.TEST_EXCHANGE_ID, 15, "Horse e5")));
        marketData = new MarketData(3, 3, "3", "market 3", MarketType.WINNER);
        market = new Market(marketData, runners3);
        startTime = System.currentTimeMillis() + 1000;
        raceData = new EventData(3, "0", 3, "Newbury", "3rd", SportEnum.HORSE_RACING, startTime);
        races.add(new Event(raceData, Lists.newArrayList(market), 60000));

        return races;
    }

    @Override
    public Event getEvent(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void loadEvents() {
    }

}
