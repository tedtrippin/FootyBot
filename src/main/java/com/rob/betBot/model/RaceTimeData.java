package com.rob.betBot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "racetime")
public class RaceTimeData extends DataObject {

    private static final long serialVersionUID = 1L;

    @Column (name = "race_name")
    private String raceName;

    @Column (name = "duration_ms")
    private long durationMs;

    @Column (name = "timestamp")
    private long timestamp;

    protected RaceTimeData() {
    }

    public RaceTimeData(long id, String raceName, long durationMs, long timestamp) {

        super(id);

        this.raceName = raceName;
        this.durationMs = durationMs;
        this.timestamp = timestamp;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = durationMs;
    }
}
