package com.rob.betBot.mvc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.Event;
import com.rob.betBot.Market;
import com.rob.betBot.MarketPrices;
import com.rob.betBot.MarketType;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.NoSuchMarketException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.SessionKeys;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;
import com.rob.betBot.mvc.model.RunnerPrice;

@Controller
@RequestMapping("/prices")
public class Prices {

    private final Logger log = Logger.getLogger(Prices.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPrices(@RequestParam("id") String id, HttpServletRequest request) {

        log.debug("prices");

        Exchange exchange = (Exchange) request.getSession().getAttribute(SessionKeys.EXCHANGE.toString());
        Event event = exchange.getEventManager().getEvent(Long.valueOf(id));
        try {
            exchange.getPricesManager().updateEvent(event, System.currentTimeMillis());

            ModelAndViewBuilder modelAndViewBuilder = new ModelAndViewBuilder("prices");
            Market correctScoreMarket = event.getMarket(MarketType.WINNER);
            MarketPrices prices = correctScoreMarket.getLatestPrices();

            List<RunnerPrice> runnerPrices = new ArrayList<>();
            for (long runnerId : prices.getSortedRunners()) {
                String name = correctScoreMarket.getRunner(runnerId).getRunnerData().getRunnerName();
                double price = prices.getPrice(runnerId);
                runnerPrices.add(new RunnerPrice(name, price));
            }

            modelAndViewBuilder.add(ModelKeys.PRICES, runnerPrices);
            return modelAndViewBuilder.build();

        } catch (ExchangeException ex) {
            log.error("", ex);
        } catch (NoSuchMarketException ex) {
            log.error("", ex);
        }

        return null;
    }
}
