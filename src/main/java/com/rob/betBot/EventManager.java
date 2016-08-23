package com.rob.betBot;

import java.util.Collection;

public interface EventManager {

    /**
     * Gets available races.
     *
     * @return
     */
    public Collection<Event> getEvents();

    /**
     * Gets a race by id.
     *
     * @return
     */
    public Event getEvent(long id);

    /**
     * Tells this event manager to load latest events.
     *
     * @param expectedStartTime
     */
    public void loadEvents();
}
