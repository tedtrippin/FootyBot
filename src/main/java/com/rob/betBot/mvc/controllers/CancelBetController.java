package com.rob.betBot.mvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.Bet;
import com.rob.betBot.engine.EventBot;
import com.rob.betBot.engine.EventThread;
import com.rob.betBot.engine.EventThreadFactory;
import com.rob.betBot.exception.BetCancelException;
import com.rob.betBot.exception.ExchangeException;

@Controller
@RequestMapping("/cancelbet")
public class CancelBetController extends BaseController {

    private final Logger log = Logger.getLogger(CancelBetController.class);

    @Autowired
    private EventThreadFactory raceThreadFactory;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getCancelBet(@RequestParam("id") String id, HttpServletRequest request) {

        log.debug("getCancelBet");

        threadLoop: for (EventThread eventThread : raceThreadFactory.getThreads()) {
            for (EventBot bot : eventThread.getBots()) {
                for (Bet bet : bot.getBets()) {
                    if (!bet.getBetId().equals(id))
                        continue;

                    try {
                        log.debug ("Cancelling bet[" + bet + "]");
                        bot.cancelBet(bet);
                    } catch (ExchangeException | BetCancelException ex) {
                        log.error("Couldnt cancel bet[id:" + id + "]", ex);
                        addErrorMessage("Unable to cancel bet[" + ex.getMessage() + "]");
                    }
                    break threadLoop;
                }
            }
        }

        return new ModelAndView("forward:/botsandbets");
    }

}
