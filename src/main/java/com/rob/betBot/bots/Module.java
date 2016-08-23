package com.rob.betBot.bots;

import java.util.Collection;

import com.rob.betBot.bots.modules.ModuleProperty;

public interface Module {

    /**
     * Gets the ID of this module.
     *
     * @return
     */
    public String getId();

    /**
     * Gets the name of this module.
     * @return
     */
    public String getName();

    /**
     * Gets a description of this module.
     *
     * @return
     */
    public String getDescription();

    /**
     * Gets this modules properties.
     *
     * @return
     */
    public Collection<ModuleProperty> getProperties();
}
