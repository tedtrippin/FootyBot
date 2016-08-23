package com.rob.betBot.exchange.betfair;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rob.betBot.Event;
import com.rob.betBot.EventLoader;
import com.rob.betBot.EventManager;
import com.rob.betBot.exchange.Exchange;

@Component
public class BetfairEventManager implements EventManager {

    /**
     * Events map<eventId, event>.
     */
    private Map<Long, Event> idToEventMap = new HashMap<>();

    @Autowired
    private EventLoader eventLoader;

    public BetfairEventManager() {
    }

    @Override
    public Collection<Event> getEvents() {
        return idToEventMap.values();
    }

    @Override
    public Event getEvent(long id) {
        return idToEventMap.get(id);
    }

    @Override
    public void loadEvents() {

        long startTime = System.currentTimeMillis();
        List<Event> events = eventLoader.loadEvents(Exchange.BETFAIR_EXCHANGE_ID, startTime);
        Map<Long, Event> map = new HashMap<>();
        events.forEach(e -> map.put(e.getEventData().getId(), e));
        idToEventMap = map;
    }
}
