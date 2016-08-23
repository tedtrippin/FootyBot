package com.rob.betBot.mvc.fbControllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.Event;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.engine.BetEngine;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.SessionKeys;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;

@Controller("FbAddBotToEventController")
@RequestMapping("/fb/addbottoevent")
public class AddBotToEventController extends BaseController {

    private final Logger log = Logger.getLogger(AddBotToEventController.class);

    @Autowired
    private BetEngine betEngine;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getTestRaceResult(@CookieValue(value = ModelKeys.BOT, required = false) String botString,
        @RequestParam("eventId") long eventId, HttpServletRequest request) {

        log.debug("getAddBotToEvent");

        ModelAndViewBuilder modelAndViewBuilder = new ModelAndViewBuilder("redirect:/fb/events");

        try {
            Bot bot = cookieToBot(botString);
            if (bot.getFilters().size() == 0 && bot.getBetPlacers().size() == 0) {
                log.debug("Bot has no filters or placers");
                addErrorMessage("Bot has no filters or placers");
                return new ModelAndView("events");
            }

            Exchange exchange = (Exchange) request.getSession().getAttribute(SessionKeys.EXCHANGE.toString());
            Event event = exchange.getEventManager().getEvent(eventId);
            betEngine.addBot(event, bot, exchange);

        } catch (Exception ex) {
            log.error("Couldnt add bot to event", ex);
            addErrorMessage("Couldnt add bot to event - " + ex.getMessage());
            return new ModelAndView("events");
        }

        return modelAndViewBuilder.build();
    }
}
