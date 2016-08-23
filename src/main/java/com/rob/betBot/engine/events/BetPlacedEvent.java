package com.rob.betBot.engine.events;

import com.rob.betBot.engine.EventTypeEnum;

public class BetPlacedEvent extends BotTimelineEvent {

    public BetPlacedEvent(long timeOfEvent) {
        super(EventTypeEnum.BET_PLACED, timeOfEvent);
    }
}
