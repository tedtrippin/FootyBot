package com.rob.betBot.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.rob.betBot.engine.EventBot;
import com.rob.betBot.engine.EventThread;
import com.rob.betBot.engine.EventThreadFactory;

@Path("bots")
public class Bots {

    @Autowired
    private EventThreadFactory raceThreadFactory;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventBot> getBots() {

        List<EventBot> eventBots = new ArrayList<>();
        for (EventThread eventThread : raceThreadFactory.getThreads()) {
            eventBots.addAll(eventThread.getBots());
        }
        return eventBots;
    }
}
