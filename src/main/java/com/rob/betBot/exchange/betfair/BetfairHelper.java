package com.rob.betBot.exchange.betfair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import com.rob.betBot.SportEnum;
import com.rob.betBot.conf.Property;
import com.rob.betBot.conf.PropertyKey;
import com.rob.betBot.exchange.betfair.model.MarketFilter;

public class BetfairHelper {

    public static final String MARKET_TYPE_CORRECT_SCORE = "CORRECT_SCORE";
    public static final String MARKET_TYPE_MATCH_ODDS = "MATCH_ODDS";
    public static final String MARKET_TYPE_PLACE = "PLACE";
    public static final String MARKET_TYPE_WIN = "WIN";
    public static final String ISO_8601_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String ISO_8601_TIMEZONE = "UTC";

    private DateFormat dateFormat;

    public BetfairHelper() {
        dateFormat = new SimpleDateFormat(ISO_8601_FORMAT_STRING);
        dateFormat.setTimeZone(TimeZone.getTimeZone(ISO_8601_TIMEZONE));
    }

    public MarketFilter getHorseFilter() {

        MarketFilter filter = new MarketFilter();
        filter.setMarketTypeCodes(getHorseMarketTypes());
        filter.setEventTypeIds(getHorseEventTypeIds());

        return filter;
    }

    public MarketFilter getFootballFilter() {

        MarketFilter filter = new MarketFilter();
        filter.setMarketTypeCodes(getFootballMarketTypes());
        filter.setEventTypeIds(getFootballEventTypeIds());

        return filter;
    }

    public Set<String> getHorseEventTypeIds() {

        Set<String> horseEventTypeIds = new HashSet<>();
        horseEventTypeIds.add(getBetfairHorseEventId());

        return horseEventTypeIds;
    }

    public Set<String> getFootballEventTypeIds() {

        Set<String> footyEventTypeIds = new HashSet<>();
        footyEventTypeIds.add(getBetfairFootballEventId());

        return footyEventTypeIds;
    }

    public Set<String> getHorseMarketTypes() {

        Set<String> horseMarketTypes = new HashSet<>();
        horseMarketTypes.add(MARKET_TYPE_PLACE);
        horseMarketTypes.add(MARKET_TYPE_WIN);

        return horseMarketTypes;
    }

    public Set<String> getFootballMarketTypes() {

        Set<String> footballMarketTypes = new HashSet<>();
        footballMarketTypes.add(MARKET_TYPE_CORRECT_SCORE);
        footballMarketTypes.add(MARKET_TYPE_MATCH_ODDS);

        return footballMarketTypes;
    }

    public SportEnum getSport(int eventType) {

        switch (eventType) {
            case 1:
                return SportEnum.FOOTBALL;
            case 7:
                return SportEnum.HORSE_RACING;
            default :
                return null;
        }
    }

    public String toBetfairDateString(Date date) {
        return dateFormat.format(date);
    }

    public Date fromBetfairDateString(String dateString)
        throws ParseException {

        return dateFormat.parse(dateString);
    }

    private String getBetfairFootballEventId() {
        return Property.getProperty(PropertyKey.BETFAIR_FOOTBALL_EVENT_ID);
    }

    private String getBetfairHorseEventId() {
        return Property.getProperty(PropertyKey.BETFAIR_HORSE_EVENT_ID);
    }
}
