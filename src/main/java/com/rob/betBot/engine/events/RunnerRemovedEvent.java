package com.rob.betBot.engine.events;

import com.rob.betBot.engine.EventTypeEnum;

public class RunnerRemovedEvent extends BotTimelineEvent {

    private final long runnerId;
    private final double price;

    public RunnerRemovedEvent(long runnerId, double price, EventTypeEnum eventType, long timeOfEvent) {
        super(eventType, timeOfEvent);

        this.runnerId = runnerId;
        this.price = price;
    }

    public long getRunnerId() {
        return runnerId;
    }

    public double getPrice() {
        return price;
    }
}
