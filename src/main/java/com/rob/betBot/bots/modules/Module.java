package com.rob.betBot.bots.modules;

import java.util.List;

import com.rob.betBot.Event;
import com.rob.betBot.Runner;
import com.rob.betBot.engine.BotEventTimeline;

public interface Module {

    /**
     * Gets the ID of this module.
     *
     * @return
     */
    public String getId();

    /**
     * Gets the name of this module.
     * @return
     */
    public String getName();

    /**
     * Gets a description of this module.
     *
     * @return
     */
    public String getDescription();

    /**
     * Gets this modules properties.
     *
     * @return
     */
    public List<ModuleProperty> getProperties();

    /**
     * Applies this module to a race ie. performs its function upon the race.
     *
     * @param race
     * @param runners
     * @param timeline
     * @param timeOfUpdate
     */
    public void apply(Event race, List<Runner> runners, BotEventTimeline timeline, long timeOfUpdate);
}
