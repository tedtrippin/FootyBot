package com.rob.betBot.mvc.fbControllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.base.Strings;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.bots.betPlacers.PercentageCorrectScoreLayPlacer;
import com.rob.betBot.bots.modules.ModuleFactory;
import com.rob.betBot.bots.modules.ModuleProperty;
import com.rob.betBot.bots.modules.PropertyNames;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.LoginFailedException;
import com.rob.betBot.exception.NoSuchExchangeException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.mvc.BotCookie;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.SessionKeys;
import com.rob.betBot.mvc.model.LoginDetails;

@Controller("FbLoginController")
@RequestMapping(value = "/fb/login")
public class LoginController extends BaseController {

    private final Logger log = Logger.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.POST)
    public String postLogin(@ModelAttribute("loginDetails") LoginDetails loginDetails,
        @CookieValue(value = ModelKeys.BOT,  required = false) String botString,
        HttpServletRequest request, HttpServletResponse response) {

        Exchange exchange;
        try {
            exchange = exchangeFactory.getExchange(Exchange.BETBILLHILL_EXCHANGE_ID,
                loginDetails.getUsername(), loginDetails.getPassword());
            request.getSession().setAttribute(SessionKeys.EXCHANGE.toString(), exchange);

        } catch (LoginFailedException ex) {
            addErrorMessage("login failed");
            log.debug("Login failed", ex);
            return "footybot/login";
        } catch (ExchangeException ex) {
            addErrorMessage("problem communicating with exchange");
            log.debug("Login failed, exchange exception", ex);
            return "footybot/login";
        } catch (NoSuchExchangeException ex) {
            addErrorMessage("no such exchange");
            return "footybot/login";
        }

        // If no bot exists create a default one
        if (Strings.isNullOrEmpty(botString)) {

            PercentageCorrectScoreLayPlacer placer = new PercentageCorrectScoreLayPlacer();
            for (ModuleProperty p : placer.getProperties()) {

                if (p.getName().equals(PropertyNames.LAY_AMOUNT))
                    p.setValue("2");
                else if (p.getName().equals(PropertyNames.MAX_PRICE))
                    p.setValue("20");
                else if (p.getName().equals(PropertyNames.PERCENTAGE))
                    p.setValue("90");
            }

            Bot bot = new Bot();
            bot.addBetPlacer(placer);
            String json = new ModuleFactory().toJsonString(bot);
            response.addCookie(new BotCookie(json));
        }

        return "redirect:/fb/events";
    }
}
