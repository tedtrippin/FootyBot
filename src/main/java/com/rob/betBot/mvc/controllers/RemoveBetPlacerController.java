package com.rob.betBot.mvc.controllers;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.bots.Bot;
import com.rob.betBot.bots.modules.ModuleFactory;
import com.rob.betBot.exception.InvalidBotException;
import com.rob.betBot.mvc.BotCookie;
import com.rob.betBot.mvc.ModelKeys;

@Controller
@RequestMapping("/removebetplacer")
public class RemoveBetPlacerController extends BaseController {

    private final Logger log = Logger.getLogger(RemoveBetPlacerController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getRemoveBetPlacer(@CookieValue(value = ModelKeys.BOT) String botString, @RequestParam("index") String index,
        HttpServletResponse response) {

        log.debug("getRemoveBetPlacer");

        try {
            int idx = Integer.parseInt(index);
            Bot bot = cookieToBot(botString);
            if (idx < bot.getBetPlacers().size()) {
                bot.getBetPlacers().remove(idx);
                String json = new ModuleFactory().toJsonString(bot);
                response.addCookie(new BotCookie(json));
            }
        } catch (InvalidBotException ex) {
            log.error("Couldnt convert cookie to Bot", ex);
            response.addCookie(new BotCookie(""));
        } catch (Exception ex) {
            log.error("Unable to remove module from bot", ex);
        }

        return new ModelAndView("redirect:/bot");
    }
}
