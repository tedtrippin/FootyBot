package com.rob.betBot.exchange.betfair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.rob.betBot.model.RaceTimeData;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

public class BetfairRaceTimeScraper {

    private static Logger log = LogManager.getLogger(BetfairRaceTimeScraper.class);

    private final static Pattern DISTANCE_PATTERN = Pattern.compile("([1-9]+m )?([0-9]+f)?( [0-9]+y)?");
    private final static Pattern TIME_PATTERN = Pattern.compile("([0-9]+)m ([0-9]+).([0-9]+)s");
    private final static String TIME = "Time: ";
    private final String DAY_PAGE_URL = "http://www.timeform.com/horse-racing/results/";
    private final String RESULT_STUB_URL = "http://www.timeform.com";
    private BrowserEngine webkit;

    public BetfairRaceTimeScraper() {
        webkit = BrowserFactory.getWebKit();
    }

    public void shutdown() {
        webkit.shutdown();
    }

    /**
     * Do the scrape. Subtracts {@code start} days from today.
     *
     * @param start When to start, how many days ago.
     * @return
     */
    public List<RaceTimeData> scrape(Calendar date) {

        List<RaceTimeData> datas = new ArrayList<>();
        scrapeDayPage(date, datas);
        return datas;
    }

    private void scrapeDayPage(Calendar calendar, List<RaceTimeData> datas ) {

        String yearString = String.valueOf(calendar.get(Calendar.YEAR));
        String monthString = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String dayString = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String dateString = String.format("%s-%s-%s", yearString, monthString, dayString);

        log.debug("Scraping day[" + dateString + "]");

        // Get the days card
        try ( Page page = webkit.navigate(DAY_PAGE_URL + dateString) ) {

            Document document = page.getDocument();
            List<Element> races = document.queryAll("div.w-results-holder");
            for (Element race : races) {

                // Get race course name
                Optional<Element> racecourseNameHeader = race.query("section.widget-refresh-header");
                if (!racecourseNameHeader.isPresent())
                    continue;

                String racecourseName = racecourseNameHeader.get().query("h2.results-header").get().getText().get();
                racecourseName = racecourseName.trim()
                    .toLowerCase()
                    .replace(" results", "")
                    .replace(" park", "");

                // Scrape races
                List<Element> links = race.queryAll("a.results-title");
                for (Element link : links) {
                    String href = link.getAttribute("href").get();
                    RaceTimeData raceTimeData = scrapeRaceResult(href, racecourseName);
                    datas.add(raceTimeData);
                }
            }
        }
    }

    private RaceTimeData scrapeRaceResult(String href, String racecourseName) {

        log.debug("scraping race result");

        try ( Page page = webkit.navigate(RESULT_STUB_URL + href) ) {

            // Get the description
            Document document = page.getDocument();
            Optional<Element> raceDescription = document.query("h3[title='Race title']");
            String description = raceDescription.get().getText().get();

            // Get distance string
            String distanceString = null;
            List<Element> spanElements = document.queryAll("span");
            for (Element span : spanElements) {
                if (!span.getText().isPresent())
                    continue;

                if (!span.getTitle().isPresent())
                    continue;

                String title = span.getTitle().get();
                if (title.startsWith("Distance expressed in miles")) {
                    distanceString = span.getText().get();
                    break;
                }
            }

            // Get race duration string
            String timeString = null;
            Element raceReport = document.query("section.rp-race-report").get();
            List<Element> pElements = raceReport.queryAll("p");
            for (Element p : pElements) {
                if (!p.getText().isPresent())
                    continue;

                String value = p.getText().get();
                if (value.contains(TIME)) {
                    timeString = value;
                    break;
                }
            }

            long cleanedTime = cleanTimeString(timeString);
            String cleanedDistance = cleanDistance(distanceString);
            String cleanedDescription = cleanDescription(description);
            StringBuilder raceName = new StringBuilder(racecourseName)
                .append(' ')
                .append(cleanedDistance)
                .append(' ')
                .append(cleanedDescription);

            if (log.isDebugEnabled()) {
                log.debug("raceName[" + raceName + "] from [" + racecourseName + ", " + distanceString + ", " + description + "]");
                log.debug("cleanedTime[" + cleanedTime + "]");
            }

            return new RaceTimeData(0, raceName.toString(), cleanedTime, System.currentTimeMillis());
        }
    }

    private String cleanDistance(String distance) {

        if (Strings.isNullOrEmpty(distance))
            return null;

        Matcher matcher = DISTANCE_PATTERN.matcher(distance);
        if (!matcher.find())
            return null;

        StringBuilder sb = new StringBuilder();
        for (int idx = 1; idx <= matcher.groupCount(); idx++) {
            String s = matcher.group(idx);
            if (s != null)
                sb.append(s);
        }

        return sb.toString().trim();
    }

    private long cleanTimeString(String timeString) {

        if (Strings.isNullOrEmpty(timeString))
            return 0;

        int startIdx = timeString.indexOf(TIME) + TIME.length();
        timeString = timeString.substring(startIdx).trim();

        Matcher matcher = TIME_PATTERN.matcher(timeString);
        if (!matcher.matches())
            return 0;

        int minutes = Integer.parseInt(matcher.group(1));
        int seconds = Integer.parseInt(matcher.group(2));
        int centiseconds = Integer.parseInt(matcher.group(3));

        seconds += minutes * 60;
        long time = (seconds * 1000) + (centiseconds * 10);
        return time;
    }

    private String cleanDescription(String description) {

        if (Strings.isNullOrEmpty(description))
            return null;

        description = description.toLowerCase();

        StringBuilder sb = new StringBuilder();

        if (description.contains("juvenile"))
            sb.append("juv ");

        if (description.contains("novice"))
            sb.append("nov ");

        if (description.contains("beginner"))
            sb.append("beg ");

        if (description.contains("handicap"))
            sb.append("hcap ");

        if (description.contains("chase"))
            sb.append("chs ");

        if (description.contains("nursery"))
            sb.append("nursery ");

        if (description.contains("maiden hurdle"))
            sb.append("mdn hrd ");
        else if (description.contains("hurdle"))
            sb.append("hrd ");

        if (description.contains("listed"))
            sb.append("listed ");

        if (description.contains("conditions stakes"))
            sb.append("cond stks ");

        if (description.contains("maiden stakes"))
            sb.append("mdn stks ");

        return sb.toString().trim();
    }
}
