package com.rob.betBot.mvc.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.bots.BetPlacer;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.bots.Filter;
import com.rob.betBot.bots.modules.ModuleManager;
import com.rob.betBot.exception.InvalidBotException;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;

@Controller
public class BotController extends BaseController {

    private final Logger log = Logger.getLogger(BotController.class);

    @RequestMapping(value = "/bot", method = RequestMethod.GET)
    public ModelAndView getBot(@CookieValue(value = ModelKeys.BOT,  required = false) String botString) {

        log.debug("getBot");

        ModelAndViewBuilder modelAndViewBuilder = new ModelAndViewBuilder(ModelKeys.BOT);

        Filter[] filters = ModuleManager.instance().getFilters();
        modelAndViewBuilder.add(ModelKeys.FILTERS, filters);
        modelAndViewBuilder.add("numberOfFilters", filters.length);

        BetPlacer[] betPlacers = ModuleManager.instance().getBetPlacers();
        modelAndViewBuilder.add(ModelKeys.BET_PLACERS, betPlacers);
        modelAndViewBuilder.add("numberOfBetPlacers", betPlacers.length);

        try {
            Bot bot = cookieToBot(botString);
            modelAndViewBuilder.add(ModelKeys.BOT, bot);
        } catch (InvalidBotException ex) {
            log.error("Can't create bot from cookie", ex);
            return new ModelAndView("error");
        }

        return modelAndViewBuilder.build();
    }

    @RequestMapping(value = "/bot", method = RequestMethod.POST)
    public ModelAndView postBot(@CookieValue(value = ModelKeys.BOT,  required = false) String botString) {

        log.debug("postBot");

        return getBot(botString);
    }

}
