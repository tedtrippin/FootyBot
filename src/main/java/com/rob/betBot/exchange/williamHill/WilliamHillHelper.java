package com.rob.betBot.exchange.williamHill;

import com.rob.betBot.MarketType;

public class WilliamHillHelper {

    private final static String CORRECT_SCORE = "Correct Score";
    private final static String LIVE_SCORE = "Live Score";
    private final static String MATCH_BETTING = "Match Betting";
    private final static String MATCH_BETTING_LIVE = "Match Betting Live";

    /**
     * Converts a William Hill market name to a betbot {@link MarketType}
     *
     * @param marketName
     * @return
     */
    public static MarketType getMarketType(String marketName) {

        if (marketName.equalsIgnoreCase(MATCH_BETTING))
            return MarketType.MATCH_ODDS;

        if (marketName.equalsIgnoreCase(MATCH_BETTING_LIVE)) // Match odds but in-play
            return MarketType.MATCH_ODDS;

        if (marketName.equalsIgnoreCase(CORRECT_SCORE))
            return MarketType.CORRECT_SCORE;

        if (marketName.equalsIgnoreCase(LIVE_SCORE)) // Correct score but in-play
            return MarketType.CORRECT_SCORE;

        return null;
    }
}
