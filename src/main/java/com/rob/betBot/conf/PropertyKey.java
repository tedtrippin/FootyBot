package com.rob.betBot.conf;

/**
 * Place property keys in here.
 *
 * @author Robert.Haycock
 */
public class PropertyKey {

    public static String SYSTEM_JSON_SCRAPER_PATH = "system.jsonScraperPath";
    public static String SYSTEM_LOAD_RACE_INTERVAL = "system.loadRaceInterval";
    public static String SYSTEM_JDBC_DRIVER = "system.jdbc.driver";
    public static String SYSTEM_JDBC_URL = "system.jdbc.url";
    public static String SYSTEM_JDBC_USERNAME = "system.jdbc.username";
    public static String SYSTEM_JDBC_PASSWORD = "system.jdbc.password";

    public static String NUMBER_OF_PRICES = "numberOfPrices";
    public static String PRICE_DIFFERENCE_FOR_WIN_BET = "priceDifferenceForWinBet";
    public static String PRICE_DIFFERENCE_FOR_PLACED_BET = "priceDifferenceForPlacedBet";
    public static String TIME_TO_START_BET_MS = "timeToStartBetMs";

    // ArangoDB properties
    public static String ARANGODB_URL = "arangodb.url";

    // Betfair properties
    public static String BETFAIR_APPLICATION_KEY = "betfair.applicationKey";
    public static String BETFAIR_FOOTBALL_EVENT_ID = "betfair.footballEventId";
    public static String BETFAIR_HORSE_EVENT_ID = "betfair.horseEventId";
    public static String BETFAIR_JSON_RPC_URL = "betfair.jsonRpcUrl";
    public static String BETFAIR_JSON_LOGIN_URL = "betfair.jsonLoginUrl";

    // William Hill properties
    public static String WILLIAM_HILL_EVENTS_URLS = "williamHill.events.urls";
    public static String WILLIAM_HILL_EVENTS_INPLAY_START_STRING = "williamHill.events.inPlay.startString";
    public static String WILLIAM_HILL_EVENTS_OUTPLAY_START_STRING = "williamHill.events.outPlay.startString";
    public static String WILLIAM_HILL_EVENTS_END_STRING = "williamHill.events.endString";
    public static String WILLIAM_HILL_EVENTS_EXPIRED_STRING = "williamHill.events.expiredString";
    public static String WILLIAM_HILL_MARKETS_START_STRING = "williamHill.markets.startString";
    public static String WILLIAM_HILL_MARKETS_END_STRING = "williamHill.markets.endString";
    public static String WILLIAM_HILL_EVENTS_UPDATE_INTERVAL = "williamHill.events.updateInterval";
    public static String WILLIAM_HILL_PRICES_URL_PREFIX = "williamHill.prices.urlPrefix";
    public static String WILLIAM_HILL_PRICES_INTERVAL = "williamHill.prices.interval";

    // Football properties
    public static String FOOTBALL_LAYING_WHEN_TO_STOP_MINUTES = "football.laying.whenToStopMinutes";

}
