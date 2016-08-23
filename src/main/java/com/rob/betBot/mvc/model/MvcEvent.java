package com.rob.betBot.mvc.model;

import com.rob.betBot.Event;

/**
 * Simplified version of {@link Event} for use in
 * MVC views.
 *
 * @author robert.haycock
 *
 */
public class MvcEvent {

    private final long id;
    private final String name;
    private final String startDate;
    private final long duration;
    private final boolean correctScore;

    public MvcEvent(Event event) {
        id = event.getEventData().getId();
        name = event.getEventData().getEventName();
        startDate = DateUtils.getDate(event.getEventData().getExpectedStartTime());
        duration = event.getEstimatedDurationMS() / 1000;
        correctScore = event.getHasCorrectScore();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isCorrectScore() {
        return correctScore;
    }
}
