package com.rob.betBot.mvc.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimelineEvent {

    private final long time;
    private final String description;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");

    public TimelineEvent(long time, String description) {
        this.time = time;
        this.description = description;
    }

    public long getTime() {
        return time;
    }

    public String getTimeString() {
        return dateFormatter.format(new Date(time));
    }

    public String getDescription() {
        return description;
    }
}
