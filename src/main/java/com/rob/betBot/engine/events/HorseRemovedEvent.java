package com.rob.betBot.engine.events;

import com.rob.betBot.engine.EventTypeEnum;

public class HorseRemovedEvent extends BotTimelineEvent {

    private final long runnerId;

    public HorseRemovedEvent(long runnerId, EventTypeEnum eventType, long timeOfEvent) {
        super(eventType, timeOfEvent);

        this.runnerId = runnerId;
    }

    public long getRunnerId() {
        return runnerId;
    }
}
