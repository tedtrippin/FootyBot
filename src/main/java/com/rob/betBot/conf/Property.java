package com.rob.betBot.conf;

import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Robert.Haycock
 */
public class Property implements PropertiesListener {

    private static final Logger log = LogManager.getLogger(Property.class);
    private static final String PROPERTIES_FILE = "betbot.properties";
    private static Properties instanceProperties = new Properties();

    @Override
    public void onLoad(Properties properties) {

        instanceProperties = properties;
    }

    public static String getProperty(String key) {
        return instanceProperties.getProperty(key);
    }

    public static long getPropertyAsLong(String key) {

        String property = instanceProperties.getProperty(key);
        if (property == null)
            return -1;

        try {
            return Long.parseLong(property);
        } catch (NumberFormatException ex) {
            log.error("Invalid int[" + property + "]", ex);
            return -1;
        }
    }

    public static double getPropertyAsDouble(String key) {

        String property = instanceProperties.getProperty(key);
        try {
            return Double.parseDouble(property);
        } catch (NumberFormatException ex) {
            log.error("Invalid int[" + property + "]", ex);
            return -1;
        }
    }

    public static void setProperty(String name, String value) {
        instanceProperties.put(name, value);
    }

    @Override
    public String getDefaultFileName() {
        return PROPERTIES_FILE;
    }

    @Override
    public String getFileName() {
        return PROPERTIES_FILE;
    }
}
