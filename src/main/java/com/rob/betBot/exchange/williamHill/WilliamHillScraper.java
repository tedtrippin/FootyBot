package com.rob.betBot.exchange.williamHill;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rob.betBot.exception.ExpiredException;
import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.dom.Document;
import com.ui4j.api.dom.Element;

/**
 * Attempts to scrape event data/prices from William Hill using
 * a headless browser. It tries to detect if the response is a
 * "real user challenge" and overcome it.
 *
 * @author robert.haycock
 *
 */
public class WilliamHillScraper extends SimpleJsonScraper {

    private final static Logger log = LogManager.getLogger(WilliamHillScraper.class);
    private final String CHALLENGE_ON_LOAD_ATTRIBUTE = "challenge()";

    public WilliamHillScraper(String urlString) {
        super(urlString);
    }

    @Override
    protected <T> List<T> doScrape(String endString, String expiredString, Class<T> clazz, boolean getAll, String... startStrings)
        throws IOException, ExpiredException {

        log.debug("WH Scraping[" + urlString + "] with startStrings[" + Arrays.toString(startStrings) + "]");

        BrowserEngine webkit = BrowserFactory.getWebKit();
        try (Page page = webkit.navigate(urlString);) {
            Document document = page.getDocument();

            // Check for the challenge page by checking for @code <body onload="challenge()"
            String onLoadAttribute;
            try {
                onLoadAttribute = document.getBody().getAttribute("onLoad").get();
                if (onLoadAttribute.equals(CHALLENGE_ON_LOAD_ATTRIBUTE))  {
                    log.debug("Challenge detected");
                    Thread.sleep(500);
                    page.executeScript("document.forms[0].submit()");
                } else {
                    log.debug("No challenge");
                }
            } catch (NoSuchElementException | InterruptedException ex) {
            }

            List<T> results = checkForResults(document, endString, expiredString, clazz, getAll, startStrings);

            if (results.isEmpty()) {
                log.warn("No results, trying a document.forms[0].submit...");
                page.executeScript("document.forms[0].submit()");

                results = checkForResults(document, endString, expiredString, clazz, getAll, startStrings);
                if (results.isEmpty())
                    log.debug("still no results");
            }

            log.debug("found [" + results.size() + "] results");

            return results;
        }
    }

    private <T> List<T> checkForResults(Document document, String endString, String expiredString, Class clazz,
        boolean getAll, String... startStrings)
            throws ExpiredException, IOException {

        List<Element> scripts = document.queryAll("script");
        if (scripts == null || scripts.isEmpty())
            return Collections.emptyList();

        for (String startString : startStrings) {
            for (Element scriptElement : scripts) {
                if (scriptElement.getInnerHTML().indexOf(startString) < 0)
                    continue;

                BufferedReader reader = new BufferedReader(new StringReader(document.getBody().getInnerHTML()));
                List<T> results = doScrape(reader, endString, expiredString, clazz, getAll, startString);
                if (!results.isEmpty())
                    return results;
            }
        }
        return Collections.emptyList();
    }

    private void dumpDocumentToFile(Document document) {

        try {
            Path tmpFilePath = Files.createTempFile("wh_scrape_", ".txt");
            FileWriter writer = new FileWriter(tmpFilePath.toFile());
            writer.write(document.getBody().getInnerHTML());
            writer.close();
            log.debug("Dumped document to[" + tmpFilePath + "]");
        } catch (Exception ex) {
            log.error ("unable to dump file");
        }
    }
}
