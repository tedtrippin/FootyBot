package com.rob.betBot.engine.events;

import com.rob.betBot.engine.EventTypeEnum;

public abstract class BotTimelineEvent {

    private EventTypeEnum eventType;
    private long timeOfEvent;

    public BotTimelineEvent(EventTypeEnum eventType, long timeOfEvent) {
        this.eventType = eventType;
        this.timeOfEvent = timeOfEvent;
    }

    public EventTypeEnum getEventType() {
        return eventType;
    }

    public long getTimeOfEvent() {
        return timeOfEvent;
    }
}
