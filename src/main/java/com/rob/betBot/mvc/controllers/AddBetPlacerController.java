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

import com.rob.betBot.bots.BetPlacer;
import com.rob.betBot.bots.Bot;
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
@RequestMapping("/addbetplacer")
public class AddBetPlacerController extends BaseController {

    private final Logger log = Logger.getLogger(AddBetPlacerController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAddBetPlacer(@RequestParam("id") String id) {

        log.debug("getAddBetPlacer");

        ModelAndViewBuilder modelAndViewBuilder = new ModelAndViewBuilder("addBetPlacer");

        BetPlacer betPlacer = ModuleManager.instance().getBetPlacer(id);
        if (betPlacer == null)
            throw new ModuleNotFoundException();

        modelAndViewBuilder.add(ModelKeys.BET_PLACER, new AddedModule(betPlacer));

        return modelAndViewBuilder.build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView postAddBetPlacer(@CookieValue(value = ModelKeys.BOT,  required = false) String botString,
        @ModelAttribute("addedModule") AddedModule addedModule, HttpServletResponse response) {

        log.debug("postAddBetPlacer");

        try {
            Bot bot = cookieToBot(botString);
            BetPlacer betPlacer = new ModuleFactory().createBetPlacer(addedModule.getId(), addedModule.getProperties());
            if (betPlacer != null) {
                bot.addBetPlacer(betPlacer);
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
