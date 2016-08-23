package com.rob.betBot.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.base.Strings;

public abstract class PropertiesLoader {

    private final static Logger log = LogManager.getLogger(PropertiesLoader.class);

    protected Properties properties = new Properties();
    private static final String CONFIG_PATH = "configPath";

    /**
     * Returns the name of the default properties file.
     * @return
     */
    abstract public String getDefaultFileName();

    /**
     * Returns the name of the properties file.
     * @return
     */
    abstract public String getFileName();

    /**
     * Loads the properties from CONFIG_PATH. If it doesn't
     * exist it is created from the defaults.
     */
    public void load() {

        log.debug("reloading " + getFileName());

        String configPath = System.getProperty(CONFIG_PATH);

        try {
            if (Strings.isNullOrEmpty(configPath)) {
                System.out.println("CONFIG_PATH not set");
                return;
            }

            File propertiesFile = new File(configPath, getFileName());
            if (!propertiesFile.exists())
                copyProperties(propertiesFile);

            Properties newProperties = new Properties();
            newProperties.load(new FileInputStream(propertiesFile));
            properties = newProperties;

        } catch (IOException ex) {
            System.out.println("Unable to load application properties");
            ex.printStackTrace();
        }
    }

    private void copyProperties(File target) {

        try {
            InputStream propertiesStream = Property.class.getClassLoader().getResourceAsStream(getDefaultFileName());
            Files.copy(propertiesStream, target.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
