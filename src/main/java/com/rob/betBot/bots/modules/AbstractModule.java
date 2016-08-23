package com.rob.betBot.bots.modules;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.rob.betBot.bots.Module;

public abstract class AbstractModule implements Module {

    private static final Logger log = Logger.getLogger(AbstractModule.class);

    private final ModuleProperty[] properties;
    private final String id;
    private final String name;
    private final String description;
    protected int timesRun = 0;

    protected AbstractModule(String id, String name, String description, ModuleProperty... properties) {

        if (Strings.isNullOrEmpty(name))
            throw new IllegalArgumentException("'name' can not be empty");

        if (Strings.isNullOrEmpty(id))
            throw new IllegalArgumentException("'id' can not be empty");

        this.id = id;
        this.name = name;
        this.description = description;

        if (properties == null) {
            this.properties = new ModuleProperty[0];
            return;
        }

        this.properties = properties;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<ModuleProperty> getProperties() {
        return Arrays.asList(properties);
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("[id=").append(id)
            .append(",name=").append(name)
            .append(']').toString();
    }

    protected double getPropertyAsDouble(String property) {
        String value = null;
        try {
            value = getProperty(property).getValue();
            if (Strings.isNullOrEmpty(value))
                return 0;

            return Double.parseDouble(value);
        } catch (Exception ex) {
            log.error("Invalid double[" + property + ":" + value + "]", ex);
            return 0;
        }
    }

    protected int getPropertyAsInt(String property) {
        String value = null;
        try {
            value = getProperty(property).getValue();
            if (Strings.isNullOrEmpty(value))
                return 0;

            return Integer.parseInt(value);
        } catch (Exception ex) {
            log.error("Invalid int[" + property + ":" + value + "]", ex);
            return 0;
        }
    }

    private ModuleProperty getProperty(String name) {

        for (ModuleProperty property : properties) {
            if (property.getName().equals(name))
                return property;
        }
        return null;
    }
}
