package com.rob.betBot.mvc.fbControllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.engine.BetEngine;

@Controller("FbCancelBotController")
@RequestMapping("/fb/cancelbot")
public class CancelBotController extends BaseController {

    private final Logger log = Logger.getLogger(CancelBotController.class);

    @Autowired
    private BetEngine betEngine;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getCancelBot(@RequestParam("id") String idString, HttpServletRequest request) {

        log.debug("getCancelBot");

        try {
            long botId = Long.valueOf(idString);
            betEngine.cancelBot(botId);
        } catch (NumberFormatException ex) {
            addErrorMessage("ID wasnt a number");
        }

        return new ModelAndView("forward:/fb/events");
    }

}
