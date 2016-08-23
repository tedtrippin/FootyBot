package com.rob.betBot.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rob.betBot.dao.IdGenerator;
import com.rob.betBot.dao.RaceTimeDao;
import com.rob.betBot.exchange.betfair.BetfairRaceTimeScraper;
import com.rob.betBot.model.RaceTimeData;

@RestController
public class Admin {

    private static Logger log = LogManager.getLogger(Admin.class);
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private RaceTimeDao raceTimeDao;

    @Autowired
    private IdGenerator idGenerator;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @RequestMapping("/rest/admin")
    public String defaultGet() {
        return "Admin section";
    }

    @GET
    @Path("/scrapetimes/{start}/{length}")
    @RequestMapping("/rest/admin/scrapetimes/{startDate}/{length}")
    @Produces(MediaType.APPLICATION_JSON)
    public ScrapeTimesResponse scrapeTimes(@PathVariable("startDate") String startDateString,
        @PathVariable("length") int length) {

        if (raceTimeDao == null)
            throw new RuntimeException("no race time dao");


        Calendar startDate = Calendar.getInstance();
        try {
            Date d = dateFormatter.parse(startDateString);
            startDate.setTime(d);
        } catch (ParseException ex) {
            throw new RuntimeException("Invalid date");
        }

        BetfairRaceTimeScraper scraper = new BetfairRaceTimeScraper();
        int newTimes = 0;
        int oldTimes = 0;

        try {
            while (length > 0) {
                length--;

                log.debug("Scraping times");
                List<RaceTimeData> times = scraper.scrape(startDate);

                startDate.add(Calendar.DAY_OF_YEAR, -1);

                log.debug("Saving times");
                try {
                    long id = idGenerator.getNextId(RaceTimeData.class);
                    for (RaceTimeData raceTime : times) {
                        if ( raceTimeDao.getRaceTime(raceTime.getRaceName()) != null) {
                            oldTimes++;
                            continue;
                        } else {
                            newTimes++;
                            raceTime.setId(id++);
                            raceTimeDao.saveOrUpdate(raceTime);
                        }
                    }
                } catch (Exception ex) {
                    log.error("Unable to saveOrUpdate race time", ex);
                    throw new RuntimeException("Unable to saveOrUpdate race time");
                }
            }
        } finally {
            scraper.shutdown();
        }

        return new ScrapeTimesResponse(newTimes, oldTimes);
    }


    class ScrapeTimesResponse {

        private final int numberOfNewTimes;
        private final int numberOfOldTimes;

        public ScrapeTimesResponse(int numberOfNewTimes, int numberOfOldTimes) {
            this.numberOfOldTimes = numberOfOldTimes;
            this.numberOfNewTimes = numberOfNewTimes;
        }

        public int getNumberOfOldTimes() {
            return numberOfOldTimes;
        }

        public int getNumberOfNewTimes() {
            return numberOfNewTimes;
        }
    }
}
