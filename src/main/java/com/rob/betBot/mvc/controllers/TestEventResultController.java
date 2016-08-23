package com.rob.betBot.mvc.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.Event;
import com.rob.betBot.Market;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.engine.BotEventTimeline;
import com.rob.betBot.engine.BotTester;
import com.rob.betBot.exception.InvalidBotException;
import com.rob.betBot.exception.NoSuchMarketException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.emulated.EmulatedExchange;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.model.EventGraph;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;
import com.rob.betBot.util.SportUtils;

@Controller
@RequestMapping(value = "/testeventresult")
public class TestEventResultController extends BaseController {

    private final Logger log = Logger.getLogger(TestEventResultController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getTestRaceResult(@CookieValue(value = "bot", required = false) String botString,
        @RequestParam("eventId") long eventId, HttpServletResponse response) {

        log.debug("getTestRaceResult");

        ModelAndViewBuilder modelAndViewBuilder = new ModelAndViewBuilder("botTimeline");

        try {
            Bot bot = cookieToBot(botString);

            Exchange emulatedExchange = new EmulatedExchange();
            Event event = emulatedExchange.getEventManager().getEvent(eventId);

            BotTester botTester = new BotTester(bot, event, emulatedExchange);
            botTester.run();

            BotEventTimeline timeline = botTester.getTimeline();
            Market market = SportUtils.getMainMarket(event);
            EventGraph raceGraph = new EventGraph(timeline, market);

            modelAndViewBuilder.add(ModelKeys.EVENT_GRAPH, raceGraph);

        } catch (InvalidBotException ex) {
            log.error("Couldnt convert cookie to Bot", ex);
            response.addCookie(new Cookie("bot", ""));
        } catch (NoSuchMarketException ex) {
            log.error("Couldnt find market", ex);
            this.addErrorMessage("Can't run test - couldn't find winner market");
        }

        return modelAndViewBuilder.build();
    }
}
