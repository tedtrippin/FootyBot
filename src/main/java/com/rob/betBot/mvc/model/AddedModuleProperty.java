package com.rob.betBot.mvc.model;

import com.rob.betBot.bots.modules.ModuleProperty;

public class AddedModuleProperty implements ModuleProperty {

    private String name;
    private String displayName;
    private String value;

    public AddedModuleProperty() {
    }

    public AddedModuleProperty(ModuleProperty property) {
        name = property.getName();
        displayName = property.getDisplayName();
        value = property.getValue();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new StringBuilder('[')
            .append("name=").append(name)
            .append(",value=").append(value)
            .append(']').toString();
    }

}
