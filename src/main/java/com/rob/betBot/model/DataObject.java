package com.rob.betBot.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DataObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column (name = "id")
    protected long id;

    protected DataObject() {
    }

    protected DataObject(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
