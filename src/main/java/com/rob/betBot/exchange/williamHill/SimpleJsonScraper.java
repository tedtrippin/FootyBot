package com.rob.betBot.exchange.williamHill;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rob.betBot.exception.ExpiredException;
import com.rob.betBot.util.JsonUtils;

public class SimpleJsonScraper {

    private final static Logger log = LogManager.getLogger(SimpleJsonScraper.class);

    protected String urlString;

    public SimpleJsonScraper(String urlString) {
        this.urlString = urlString;
    }

    /**
     * Attempts to read a JSON object from a URL. Starts recording when it
     * comes across {@code startStrings} and stops when it gets to {@code endString}.
     *
     * @param startStrings
     * @param endString
     * @param expiredString
     * @param clazz
     * @return
     * @throws IOException
     * @throws ExpiredException
     */
    public <T> T scrape(String endString, String expiredString, Class<T> clazz, String... startStrings)
        throws IOException, ExpiredException {

        List<T> resultList = doScrape(endString, expiredString, clazz, false, startStrings);
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    /**
     * Like {@link #scrape(String, String, String, Class)} except keeps reading
     * in case there are multiple entries.
     *
     * @param startStrings
     * @param endString
     * @param clazz
     * @return
     * @throws IOException
     */
    public <T> List<T> scrapeAll(String endString, Class<T> clazz, String... startStrings)
        throws IOException {

        try {
            return doScrape(endString, null, clazz, true, startStrings);
        } catch (ExpiredException ex) {
            return new ArrayList<>();
        }
    }

    protected InputStream getInputStream()
        throws IOException {

        URL url = new URL(urlString);
        return url.openConnection().getInputStream();
    }

    protected <T> List<T> doScrape(String endString, String expiredString, Class<T> clazz, boolean getAll, String... startStrings)
            throws IOException, ExpiredException {

        log.debug("Scraping[" + urlString + "] with startStrings[" + Arrays.toString(startStrings) + "]");

        List<T> resultList = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream()));) {
            resultList = doScrape(reader, endString, expiredString, clazz, getAll, startStrings);
        }

        if (resultList.isEmpty())
            log.warn("URL yielded no results");
        else {
            log.debug("found [" + resultList.size() + "] results");
        }

        return resultList;
    }

    protected <T> List<T> doScrape(BufferedReader in, String endString, String expiredString,
        Class<T> clazz, boolean getAll, String... startStrings)
            throws IOException, ExpiredException {

        List<T> resultList = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        StringBuilder pageContents = new StringBuilder();

        String line;
        boolean reading = false;
        while ((line = in.readLine()) != null) {

            pageContents.append(line).append('\n');

            if (expiredString != null && line.contains(expiredString))
                throw new ExpiredException();

            // Stop as soon as we get the end string.
            if (reading && line.contains(endString)) {

                // Try and convert what we have
                T obj = JsonUtils.readValue(buffer.toString(), clazz);
                resultList.add(obj);
                buffer = new StringBuilder();

                if (!getAll)
                    break;

                reading = false;
            }

            if (reading)
                buffer.append(line);

            // Start reading AFTER we come across the start string
            if (!reading) {
                for (String startString : startStrings) {
                    if (line.contains(startString))
                        reading = true;
                }
            }
        }

        return resultList;
    }
}
