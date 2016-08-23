package com.rob.betBot.mvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exception.LoginFailedException;
import com.rob.betBot.exception.NoSuchExchangeException;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.mvc.SessionKeys;
import com.rob.betBot.mvc.model.LoginDetails;

@Controller
@RequestMapping(value = "/login")
public class LoginController extends BaseController {

    private final Logger log = Logger.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.POST)
    public String postLogin(@ModelAttribute("loginDetails") LoginDetails loginDetails, HttpServletRequest request) {

        Exchange exchange;
        try {
            exchange = exchangeFactory.getExchange(
                loginDetails.getExchange(), loginDetails.getUsername(), loginDetails.getPassword());
            request.getSession().setAttribute(SessionKeys.EXCHANGE.toString(), exchange);
            request.getSession().setAttribute(SessionKeys.LOGIN_DETAILS.toString(), loginDetails);

        } catch (LoginFailedException ex) {
            addErrorMessage("login failed");
            log.debug("Login failed", ex);
            return "loginFailed";
        } catch (ExchangeException ex) {
            addErrorMessage("problem communicating with exchange");
            log.debug("Login failed, exchange exception", ex);
            return "loginFailed";
        } catch (NoSuchExchangeException ex) {
            addErrorMessage("no such exchange");
            return "loginFailed";
        }

        return "home";
    }
}
