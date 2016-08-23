package com.rob.betBot.model;

import javax.persistence.Column;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Table;

@DynamicUpdate
@Table (appliesTo = "RacecourseEvent")
public class RacecourseEvent extends DataObject {

    private static final long serialVersionUID = 1L;

    @Column (name = "name")
    private String name;

    @Column (name = "duration1")
    private int duration1;

    @Column (name = "duration2")
    private int duration2;

    @Column (name = "duration3")
    private int duration3;

    public RacecourseEvent(long id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration1() {
        return duration1;
    }

    public void setDuration1(int duration1) {
        this.duration1 = duration1;
    }

    public int getDuration2() {
        return duration2;
    }

    public void setDuration2(int duration2) {
        this.duration2 = duration2;
    }

    public int getDuration3() {
        return duration3;
    }

    public void setDuration3(int duration3) {
        this.duration3 = duration3;
    }
}
