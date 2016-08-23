package com.rob.betBot.mvc.fbControllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.rob.betBot.bots.Bot;
import com.rob.betBot.bots.modules.ModuleFactory;
import com.rob.betBot.exception.InvalidBotException;
import com.rob.betBot.exchange.ExchangeFactory;
import com.rob.betBot.exchange.ExchangeInfo;
import com.rob.betBot.mvc.model.BetBotCookie;
import com.rob.betBot.mvc.model.LoginDetails;
import com.rob.betBot.util.JsonUtils;

@Controller("FbBaseController")
public class BaseController {

    private final Logger log = Logger.getLogger(BaseController.class);

    private ThreadLocal<Collection<String>> errorMessageHolder = new ThreadLocal<>();

    @Autowired
    protected ExchangeFactory exchangeFactory;

    /**
     * Returns a new {@link LoginDetails} for use with the login box.
     *
     * @return
     */
    @ModelAttribute
    public LoginDetails getLoginDetails() {

        // TODO - get from session
        LoginDetails loginDetails = new LoginDetails();
        loginDetails.setUsername("username");
        loginDetails.setPassword("password");
        return new LoginDetails();
    }

    @ModelAttribute("exchanges")
    public Collection<ExchangeInfo> getExchanges() {

        return exchangeFactory.getExchanges();
    }

    @ModelAttribute("errorMessages")
    public Collection<String> getErrorMessages() {

        Collection<String> errorMessages = new ArrayList<>();
        errorMessageHolder.set(errorMessages);
        return errorMessages;
    }

    public void addErrorMessage(String errorMessage) {
        errorMessageHolder.get().add(errorMessage);
    }

    @PreDestroy
    public void teardown() {
        errorMessageHolder.remove();
    }

    protected Bot cookieToBot(String cookieJson)
        throws InvalidBotException {

        if (Strings.isNullOrEmpty(cookieJson))
            return new Bot();

        try {
            BetBotCookie cookie = JsonUtils.readValue(cookieJson, BetBotCookie.class);
            Bot bot = new Bot(cookie.getName());

            ModuleFactory moduleFactory = new ModuleFactory();

            for (JsonObject filterJson : cookie.getFilters()) {
                bot.addFilter(moduleFactory.createFilter(filterJson));
            }

            for (JsonObject betPlacerJson : cookie.getBetPlacers()) {
                bot.addBetPlacer(moduleFactory.createBetPlacer(betPlacerJson));
            }

            return bot;

        } catch (Exception ex) {
            log.error("Invalid bot cookie[" + cookieJson + "]", ex);
            addErrorMessage("Couldn't parse bot cookie");
            return new Bot();
        }
    }

    protected String getView(HttpServletRequest request, String view) {

        Object prefix = request.getSession().getAttribute("viewPrefix");
        if (prefix == null)
            return view;
        else
            return prefix + view;
    }
}
