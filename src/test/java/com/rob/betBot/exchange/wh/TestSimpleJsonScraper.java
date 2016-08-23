package com.rob.betBot.exchange.wh;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.rob.betBot.exception.ExpiredException;
import com.rob.betBot.exchange.williamHill.SimpleJsonScraper;
import com.rob.betBot.exchange.williamHill.model.WHEvent;

public class TestSimpleJsonScraper extends SimpleJsonScraper {

    public TestSimpleJsonScraper() {
        super("");
    }

    private String filename;

    @Test
    public void test_getMarkets()
        throws IOException, ExpiredException {

        filename = "markets.html";

        WHEvent event = this.scrape(";", "expired??", WHEvent.class, "document.event_data");
        System.out.println(event.getName());
        System.out.println(event.getMarkets().size());
    }

    @Test
    public void test_getEvent() throws IOException {

        filename = "events.html";

        List<WHEvent> events = this.scrapeAll(");", WHEvent.class, "document.uc_list.create_prebuilt_event(");
        for (WHEvent event : events) {
            System.out.println(event.getName());
        }
    }

    @Override
    protected InputStream getInputStream()
        throws IOException {

        return this.getClass().getResourceAsStream(filename);
    }
}
