package com.rob.betBot.mvc.controllers.wh;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.Event;
import com.rob.betBot.EventLoader;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.controllers.BaseController;
import com.rob.betBot.mvc.model.EventsConverter;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;
import com.rob.betBot.mvc.model.MvcEvent;

@Controller
@RequestMapping("/wh/allevents")
public class WhEventsViewController extends BaseController {

    private final Logger log = Logger.getLogger(WhEventsViewController.class);

    @Autowired
    private EventLoader eventLoader;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getEvents(@PathParam("daysOld") Integer daysOld) {

        log.debug("getLoadEvent");

        ModelAndViewBuilder view = new ModelAndViewBuilder("data/eventsView");

        if (daysOld == null)
            daysOld = 0;

        long startTime = daysOld * 24 * 60 * 60 * 1000;
        List<Event> events = eventLoader.loadEvents(Exchange.WILLIAM_HILL_EXCHANGE_ID, startTime);
        Collection<MvcEvent> mvcEvents = new EventsConverter().convertEvents(events);
        view.add(ModelKeys.EVENTS, mvcEvents);

        return view.build();
    }
}
