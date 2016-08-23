package com.rob.betBot.exchange.williamHill;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rob.betBot.BetBotConstants;
import com.rob.betBot.Event;
import com.rob.betBot.EventLoader;
import com.rob.betBot.EventManager;
import com.rob.betBot.exchange.Exchange;

@Component
public class WilliamHillFootyEventManager implements EventManager {

    private final static Logger log = LogManager.getLogger(WilliamHillFootyEventManager.class);

    private Map<Long, Event> idToEventMap = new HashMap<>();

    @Autowired
    private EventLoader eventLoader;

    public WilliamHillFootyEventManager() {
    }

    @Override
    public Collection<Event> getEvents() {
        return idToEventMap.values();
    }

    @Override
    public synchronized void loadEvents() {

        log.info("Loading events");
        long startTime = System.currentTimeMillis() - BetBotConstants.FOOTBALL_MATCH_DURATION_MS;
        List<Event> events = eventLoader.loadEvents(Exchange.WILLIAM_HILL_EXCHANGE_ID, startTime);
        Map<Long, Event> map = new HashMap<>();
        events.forEach(e -> map.put(e.getEventData().getId(), e));
        idToEventMap = map;
    }

    @Override
    public Event getEvent(long id) {
        return idToEventMap.get(id);
    }
}
