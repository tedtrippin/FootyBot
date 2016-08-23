package com.rob.betBot.mvc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.Bet;
import com.rob.betBot.engine.BetEngine;
import com.rob.betBot.engine.EventBot;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.SessionKeys;
import com.rob.betBot.mvc.model.LoginDetails;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;

@Controller
@RequestMapping("/botsandbets")
public class BotsAndBetsController extends BaseController {

    private final Logger log = Logger.getLogger(BotsAndBetsController.class);

    @Autowired
    private BetEngine betEngine;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getBotsAndBets(HttpServletRequest request) {

        log.debug("getBotsAndBets");

        ModelAndViewBuilder modelAndViewBuilder = new ModelAndViewBuilder("botsAndBets");
        LoginDetails loginDetails = (LoginDetails) request.getSession().getAttribute(SessionKeys.LOGIN_DETAILS.toString());
        if (loginDetails != null)
            addBotsAndBets(loginDetails, modelAndViewBuilder);

        return modelAndViewBuilder.build();
    }

    private void addBotsAndBets(LoginDetails loginDetails, ModelAndViewBuilder model) {

        List<EventBot> eventBots = new ArrayList<>();
        List<Bet> bets = new ArrayList<>();

        for (EventBot bot : betEngine.getBots()) {
            if (bot.getExchangeId() != loginDetails.getExchange())
                continue;

            if (!bot.getUsername().equals(loginDetails.getUsername()))
                continue;

            eventBots.add(bot);
            bets.addAll(bot.getBets());
        }
        model.add(ModelKeys.BOTS, eventBots);
        model.add(ModelKeys.BETS, bets);
    }
}
