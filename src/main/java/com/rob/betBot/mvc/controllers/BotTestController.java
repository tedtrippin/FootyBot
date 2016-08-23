package com.rob.betBot.mvc.controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.Event;
import com.rob.betBot.EventManager;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.exception.InvalidBotException;
import com.rob.betBot.exchange.emulated.EmulatedRaceManager;
import com.rob.betBot.mvc.BotCookie;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;
import com.rob.betBot.mvc.model.MvcEvent;

@Controller
@RequestMapping(value = "/bottest")
public class BotTestController extends BaseController {

    private final Logger log = Logger.getLogger(BotTestController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getBotTest(@CookieValue(value = ModelKeys.BOT,  required = false) String botString, HttpServletResponse response) {

        log.debug("getBotTest");

        ModelAndViewBuilder modelAndViewBuilder = new ModelAndViewBuilder("botTest");

        try {
            Bot bot = cookieToBot(botString);
            modelAndViewBuilder.add(ModelKeys.BOT, bot);

            EventManager testRaceManager = new EmulatedRaceManager();
            Collection<Event> events = testRaceManager.getEvents();

            Collection<MvcEvent> mvcEvents = new ArrayList<>();
            for (Event event : events) {
                mvcEvents.add(new MvcEvent(event));
            }
            modelAndViewBuilder.add(ModelKeys.EVENTS, mvcEvents);

        } catch (InvalidBotException ex) {
            log.error("Couldnt convert cookie to Bot", ex);
            response.addCookie(new BotCookie(""));
        }

        return modelAndViewBuilder.build();
    }
}
