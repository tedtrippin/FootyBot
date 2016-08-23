package com.rob.betBot.mvc.controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;
import com.rob.betBot.EventManager;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.SessionKeys;
import com.rob.betBot.mvc.model.EventsConverter;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;
import com.rob.betBot.mvc.model.MvcEvent;

@Controller
@RequestMapping(value = "/events")
public class EventController extends BaseController {

    private final Logger log = Logger.getLogger(EventController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getEvents(@CookieValue(value = ModelKeys.BOT,  required = false) String botString,
        @PathParam("refresh") boolean refresh, HttpServletRequest request) {

        log.debug("getEvents");

        if (Strings.isNullOrEmpty(botString))
            addErrorMessage("Add a bot before selecting an event");

        Exchange exchange = (Exchange) request.getSession().getAttribute(SessionKeys.EXCHANGE.toString());
        if (refresh)
            exchange.getEventManager().loadEvents();

        ModelAndViewBuilder modelAndViewBuilder = new ModelAndViewBuilder("events");
        if (exchange == null) {
            addErrorMessage("You must login to get a list of events");
        } else {
            EventManager eventManager = exchange.getEventManager();
            Collection<MvcEvent> mvcEvents = new EventsConverter().convertEvents(eventManager.getEvents());
            modelAndViewBuilder.add(ModelKeys.EVENTS, mvcEvents);
        }

        return modelAndViewBuilder.build();
    }
}
