package com.rob.betBot.mvc.fbControllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("FbLogoutController")
@RequestMapping(value = "/fb/logout")
public class LogoutController extends BaseController {

    @RequestMapping(method = RequestMethod.GET)
    public String postLogin(HttpServletRequest request) {

        request.getSession().setAttribute("loginDetails", null);
        request.getSession().setAttribute("LOGIN_DETAILS", null);

        return "home";
    }
}
