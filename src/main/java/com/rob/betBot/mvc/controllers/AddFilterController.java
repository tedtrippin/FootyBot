package com.rob.betBot.mvc.controllers;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.bots.Bot;
import com.rob.betBot.bots.Filter;
import com.rob.betBot.bots.modules.ModuleFactory;
import com.rob.betBot.bots.modules.ModuleManager;
import com.rob.betBot.exception.InvalidBotException;
import com.rob.betBot.exception.InvalidDataException;
import com.rob.betBot.exception.ModuleNotFoundException;
import com.rob.betBot.mvc.BotCookie;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.model.AddedModule;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;

@Controller
@RequestMapping("/addfilter")
public class AddFilterController extends BaseController {

    private final Logger log = Logger.getLogger(AddFilterController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAddFilter(@RequestParam("id") String id) {

        log.debug("getAddFilter");

        ModelAndViewBuilder modelAndViewBuilder = new ModelAndViewBuilder("addFilter");

        Filter filter = ModuleManager.instance().getFilter(id);
        if (filter == null)
            throw new ModuleNotFoundException();

        modelAndViewBuilder.add(ModelKeys.FILTER, new AddedModule(filter));

        return modelAndViewBuilder.build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView postAddFilter(@CookieValue(value = ModelKeys.BOT,  required = false) String botString,
        @ModelAttribute("addedFilter") AddedModule addedFilter, HttpServletResponse response) {

        log.debug("postAddFilter");

        try {
            Bot bot = cookieToBot(botString);
            Filter filter = new ModuleFactory().createFilter(addedFilter.getId(), addedFilter.getProperties());
            if (filter != null) {
                bot.addFilter(filter);
                String json = new ModuleFactory().toJsonString(bot);
                response.addCookie(new BotCookie(json));
            }
        } catch (InvalidDataException ex) {
            log.error("Unknown module ID");
        } catch (InvalidBotException ex) {
            log.error("Couldnt convert cookie to Bot", ex);
            response.addCookie(new BotCookie(""));
        }

        return new ModelAndView("redirect:/bot");
    }
}
