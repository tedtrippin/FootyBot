package com.rob.betBot.model.wh;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.rob.betBot.model.DataObject;

@Entity
@Table (name = "wh_event_parent")
public class WhEventParentData extends DataObject {

    @Column
    private String name;

    @Column(name = "connect_string")
    private String connectString;

    public WhEventParentData() {
    }

    public WhEventParentData(long id, String name, String connectString) {
        super(id);
        this.name = name;
        this.connectString = connectString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }
}
