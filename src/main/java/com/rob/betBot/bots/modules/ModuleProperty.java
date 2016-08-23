package com.rob.betBot.bots.modules;

public interface ModuleProperty {

    /**
     * Gets the name of this property.
     * @return
     */
    public String getName();

    /**
     * Gets the more readable name of this property.
     * @return
     */
    public String getDisplayName();

    /**
     * Gets the current value of this property
     * @return
     */
    public String getValue();

    /**
     * Set the value of this property.
     * @param value
     */
    public void setValue(String value);
}
