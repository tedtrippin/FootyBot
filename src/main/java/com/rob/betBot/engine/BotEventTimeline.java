package com.rob.betBot.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.rob.betBot.engine.events.BotTimelineEvent;

/**
 * Keeps track of prices and events for a RaceBot. When events are added
 * they are associated with the last prices that were added; for each call
 * to Race.updateRace(), prices should be added first and then resulting
 * events should be added after.
 *
 * @author robert.haycock
 *
 */
public class BotEventTimeline {

    private long startTime;
    private List<BotTimelineEvent> botEvents = new ArrayList<>();

    public BotEventTimeline() {
        startTime = System.currentTimeMillis();
    }

    public void addEvent(BotTimelineEvent event) {
        botEvents.add(event);
    }

    public List<BotTimelineEvent> getEvents() {
        return Collections.unmodifiableList(botEvents);
    }

    public long getStartTime() {
        return startTime;
    }
}
