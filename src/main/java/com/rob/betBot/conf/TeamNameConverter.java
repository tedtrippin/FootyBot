package com.rob.betBot.conf;

import java.util.Properties;


public class TeamNameConverter implements PropertiesListener {

    private static final String TEAM_NAMES_FILE = "teamnames.properties";
    private static Properties instanceProperties;

    @Override
    public void onLoad(Properties properties) {

        instanceProperties = properties;
    }

    @Override
    public String getDefaultFileName() {
        return TEAM_NAMES_FILE;
    }

    @Override
    public String getFileName() {
        return TEAM_NAMES_FILE;
    }

    public static String getBetfairTeamName(String whTeamName) {

        whTeamName = whTeamName.replaceAll(" ", "_");
        return instanceProperties.getProperty(whTeamName);
    }
}
