package com.rob.betBot.mvc.fbControllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("FbHomeController")
public class HomeController extends BaseController {

    private final Logger log = Logger.getLogger(HomeController.class);

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public ModelAndView getHome(Model model) {

        log.debug("getHome");
        ModelAndView homeModelAndView = new ModelAndView("footybot/login");
        return homeModelAndView;
    }
}
