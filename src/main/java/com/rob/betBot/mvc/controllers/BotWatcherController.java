package com.rob.betBot.mvc.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.Market;
import com.rob.betBot.engine.BetEngine;
import com.rob.betBot.engine.BotEventTimeline;
import com.rob.betBot.engine.EventBot;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.model.EventGraph;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;

@Controller
@RequestMapping("/botwatcher")
public class BotWatcherController extends BaseController {

    private final Logger log = Logger.getLogger(BotWatcherController.class);

    @Autowired
    private BetEngine betEngine;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getBotWatcher(@RequestParam("eventBotId") long eventBotId,
        @RequestParam("marketId") long marketId) {

        log.debug("getBotWatcher");

        ModelAndViewBuilder modelAndViewBuilder = new ModelAndViewBuilder("botTimeline");

        EventBot eventBot = betEngine.getBot(eventBotId);
        if (eventBot == null) {
            addErrorMessage("Couldnt find bot");
            return modelAndViewBuilder.build();
        }

        Market market = eventBot.getEvent().getMarket(marketId);
        if (market == null) {
            addErrorMessage("Couldnt find market");
            return modelAndViewBuilder.build();
        }

        BotEventTimeline timeline = new BotEventTimeline();
        EventGraph eventGraph = new EventGraph(timeline, market);

        modelAndViewBuilder.add(ModelKeys.EVENT_GRAPH, eventGraph);

        return modelAndViewBuilder.build();
    }
}
