package com.rob.betBot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rob.betBot.dao.EventDao;
import com.rob.betBot.dao.RaceTimeDao;
import com.rob.betBot.model.EventData;
import com.rob.betBot.model.MarketData;
import com.rob.betBot.model.RaceTimeData;
import com.rob.betBot.model.RunnerData;

@Component
public class EventLoader {

    private static Logger log = Logger.getLogger(EventLoader.class);

    @Autowired
    private EventDao eventDao;

    @Autowired
    private RaceTimeDao raceTimeDao;

    public List<Event> loadEvents(int exchangeId, long expectedStartTime) {

        List<Event> events = new ArrayList<>();
        List<EventData> eventDatas = eventDao.getByExpectedStartTime(exchangeId, expectedStartTime);
        for (EventData eventData : eventDatas) {
            events.add(loadEvent(eventData));
        }

        return events;
    }

    public Event loadEvent(EventData eventData) {

        Collection<Market> markets = new ArrayList<>();
        for (MarketData marketData : eventData.getMarkets()) {
            Collection<Runner> runners = new ArrayList<>();
            for (RunnerData r : marketData.getRunners()) {
                runners.add(new Runner(r));
            }
            markets.add(new Market(marketData, runners));
        }

        // TODO - refactor using factory pattern and RaceTimeLoader interface
        long estimatedDurationMs = 0;
        if (eventData.getSport() == SportEnum.HORSE_RACING) {
            try {
                RaceTimeData raceTime = raceTimeDao.getRaceTime(eventData.getEventName());
                if (raceTime != null)
                    estimatedDurationMs = raceTime.getDurationMs();

            } catch (Exception ex) {
                log.error("Error getting get race time for[" + eventData.getEventName() + "]", ex);
            }

        } else if (eventData.getSport() == SportEnum.FOOTBALL) {
            estimatedDurationMs = BetBotConstants.FOOTBALL_MATCH_DURATION_MS;
        }

        return new Event(eventData, markets, estimatedDurationMs);
    }
}
