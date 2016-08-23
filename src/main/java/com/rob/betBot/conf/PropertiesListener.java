package com.rob.betBot.conf;

import java.util.Properties;

public interface PropertiesListener {

    /**
     * Returns the name of the default properties file.
     * @return
     */
    public String getDefaultFileName();

    /**
     * Returns the name of the properties file.
     * @return
     */
    public String getFileName();

    /**
     * Called when properties file has been reloaded.
     * @param properties
     */
    public void onLoad(Properties properties);
}
