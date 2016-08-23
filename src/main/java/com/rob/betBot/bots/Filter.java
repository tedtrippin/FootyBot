package com.rob.betBot.bots;

import com.rob.betBot.Event;
import com.rob.betBot.engine.BotEventTimeline;

public interface Filter extends Module {

    /**
     * Applies this module to a race ie. performs its function upon the race.
     *
     * @param race
     * @param runners
     * @param timeline
     */
    public void apply(Event race, BotEventTimeline timeline);
}
