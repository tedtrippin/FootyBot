package com.rob.betBot.mvc.controllers.betfair;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.dao.WhEventParentDao;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.betfair.BetfairEventLoader;
import com.rob.betBot.exchange.betfair.BetfairExchange;
import com.rob.betBot.exchange.betfair.BetfairHorseRacingConverter;
import com.rob.betBot.exchange.betfair.jsonrpc.BetfairJsonRpcCommunicator;
import com.rob.betBot.mvc.SessionKeys;
import com.rob.betBot.mvc.controllers.BaseController;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;

@Controller
public class BetfairLoadEventsController extends BaseController {

    private final Logger log = Logger.getLogger(BetfairLoadEventsController.class);

    @Autowired
    private WhEventParentDao whEeventParentDao;

    @Autowired
    private BetfairHorseRacingConverter marketConverter;

    @RequestMapping(value = "/betfair/loadevents", method = RequestMethod.GET)
    public ModelAndView getLoadEvents(HttpServletRequest request) {
        return new ModelAndViewBuilder("betfair/betfairEvents").build();
    }

    @RequestMapping(value = "/betfair/horses/{countryCode}", method = RequestMethod.GET)
    public ModelAndView getLoadHorses(HttpServletRequest request, @PathVariable("countryCode") String countryCode) {

        log.debug("getLoadGbHorses");

        Exchange exchange = (Exchange) request.getSession().getAttribute(SessionKeys.EXCHANGE.toString());
        if (exchange == null)
            addErrorMessage("Not logged in");
        else if (!(exchange instanceof BetfairExchange))
            addErrorMessage("Not logged in to betfair");
        else
            loadHorses((BetfairExchange)exchange, countryCode);

        return new ModelAndViewBuilder("betfair/betfairEvents").build();
    }

    private void loadHorses(BetfairExchange exchange, String countryCode) {

        BetfairJsonRpcCommunicator betfair = exchange.getBetfair();
        BetfairEventLoader eventLoader = new BetfairEventLoader(betfair, marketConverter);
        try {
            eventLoader.loadHorses(countryCode);

            exchange.getEventManager().loadEvents();

        } catch (ExchangeException ex) {
            log.error("Unable to load events from Betfair", ex);
            addErrorMessage("Unable to load events from Betfair");
        }
    }
}
