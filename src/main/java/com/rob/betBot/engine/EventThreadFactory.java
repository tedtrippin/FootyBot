package com.rob.betBot.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rob.betBot.Event;
import com.rob.betBot.dao.IdGenerator;
import com.rob.betBot.dao.RaceTimeDao;

/**
 * Factory class for creating {@link EventThread}s.
 *
 * @author robert.haycock
 *
 */
@Component
public class EventThreadFactory {

    @Autowired
    private BetSettler betSettler;

    @Autowired
    private RaceTimeDao raceTimeDao;

    @Autowired
    private IdGenerator idGenerator;

    private final WeakHashMap<EventThread, Event> threads = new WeakHashMap<EventThread, Event>();

    /**
     * Creates and starts a {@link EventThread}.
     *
     * @param race
     * @param raceBot
     * @param raceTimeDao
     * @return
     */
    public EventThread create(Event event, EventBot raceBot, EventThreadListener eventThreadListener) {

        EventThread eventThread;
        if (threads.containsValue(event)) {
            for (Entry<EventThread, Event> entry : threads.entrySet()) {
                if (!entry.getValue().equals(event))
                    continue;

                eventThread = entry.getKey();
                if (eventThread.isAlive())
                    return eventThread;

                break;
            }
        }

        eventThread = new EventThread(event, raceBot, betSettler, raceTimeDao, idGenerator, eventThreadListener);
        threads.put(eventThread, event);
        eventThread.start();
        return eventThread;
    }

    public Collection<EventThread> getThreads() {
        return new ArrayList<>(threads.keySet());
    }
}
