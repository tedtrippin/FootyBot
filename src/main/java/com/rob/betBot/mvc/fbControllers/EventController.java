package com.rob.betBot.mvc.fbControllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;
import com.rob.betBot.EventManager;
import com.rob.betBot.engine.BetEngine;
import com.rob.betBot.engine.EventBot;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.williamHill.WilliamHillEventLoader;
import com.rob.betBot.model.wh.WhEventParentData;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.SessionKeys;
import com.rob.betBot.mvc.model.EventsConverter;
import com.rob.betBot.mvc.model.LoginDetails;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;
import com.rob.betBot.mvc.model.MvcEvent;

@Controller("FbEventController")
@RequestMapping("/fb/events")
public class EventController extends BaseController {

    private final Logger log = Logger.getLogger(EventController.class);

    @Autowired
    private WilliamHillEventLoader eventLoader;

    @Autowired
    private BetEngine betEngine;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getEvents(@CookieValue(value = ModelKeys.BOT,  required = false) String botString,
        @PathParam("refresh") boolean refresh, HttpServletRequest request) {

        log.debug("getEvents");

        if (Strings.isNullOrEmpty(botString))
            addErrorMessage("Add a bot before selecting an event");

        Exchange exchange = (Exchange) request.getSession().getAttribute(SessionKeys.EXCHANGE.toString());
        if (refresh)
            exchange.getEventManager().loadEvents();

        ModelAndViewBuilder modelAndViewBuilder = new ModelAndViewBuilder("footybot/events");

        WhEventParentData updatingEventParent = eventLoader.getEventParent();
        if (updatingEventParent != null)
            modelAndViewBuilder.add("loadingEvents", true);

        EventManager eventManager = exchange.getEventManager();
        Collection<MvcEvent> mvcEvents = new EventsConverter().convertEvents(eventManager.getEvents());
        modelAndViewBuilder.add(ModelKeys.EVENTS, mvcEvents);

        List<EventBot> eventBots = new ArrayList<>();
        for (EventBot bot : betEngine.getBots()) {
            eventBots.add(bot);
        }
        modelAndViewBuilder.add(ModelKeys.BOTS, eventBots);

        return modelAndViewBuilder.build();
    }


    private void addBotsAndBets(LoginDetails loginDetails, ModelAndViewBuilder model) {

    }
}
