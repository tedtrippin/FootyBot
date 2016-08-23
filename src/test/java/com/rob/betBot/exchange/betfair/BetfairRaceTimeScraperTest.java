package com.rob.betBot.exchange.betfair;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class BetfairRaceTimeScraperTest {

    @Test
    public void test() {

        BetfairRaceTimeScraper betfairRaceTimeScraper = new BetfairRaceTimeScraper();
        betfairRaceTimeScraper.scrape(Calendar.getInstance());
    }

    @Test
    public void distanceTest() {

        Pattern distancePattern = Pattern.compile("([1-9]+m )?([0-9]+f)?( [0-9]+y)?");
        String distanceString = "1m 5f 213y";

        Matcher matcher = distancePattern.matcher(distanceString);
        if (!matcher.find()) {
            System.out.println("Not found");

        } else {
            StringBuilder sb = new StringBuilder();
            for (int idx = 1; idx <= matcher.groupCount(); idx++) {
                String s = matcher.group(idx);
                if (s != null)
                    sb.append(s);
            }
            System.out.println(sb.toString());
        }
    }

    @Test
    public void timeTest() {

        Pattern timePattern = Pattern.compile("([0-9]+)m ([0-9]+).([0-9]+)s");
        String timeString = "1m 9.37s";

        Matcher matcher = timePattern.matcher(timeString);
        if (!matcher.matches()) {
            System.out.println("Not matched");

        } else {

            int minutes = Integer.parseInt(matcher.group(1));
            int seconds = Integer.parseInt(matcher.group(2));
            int centiseconds = Integer.parseInt(matcher.group(3));

            System.out.println(String.format("%dm %ds %dc", minutes, seconds, centiseconds));
        }
    }
}
