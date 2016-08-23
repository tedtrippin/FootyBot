package com.rob.betBot.conf;

import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class LoggingManager implements PropertiesListener {

    private final static Logger log = LogManager.getLogger(LoggingManager.class);
    private final String DEAFULT_PROPERTIES_FILE = "default_log4j.properties";
    private final String LOG_PROPERTIES_FILE = "log4j.properties";

    @Override
    public String getDefaultFileName() {
        return DEAFULT_PROPERTIES_FILE;
    }

    @Override
    public String getFileName() {
        return LOG_PROPERTIES_FILE;
    }

    @Override
    public void onLoad(Properties properties) {

        LogManager.resetConfiguration();
        PropertyConfigurator.configure(properties);
        log.debug("Logging configured");
    }
}
