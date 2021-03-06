package com.rob.betBot.mvc.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {

    private final Logger log = Logger.getLogger(HomeController.class);

    @RequestMapping(value = {"/botbuilder"}, method = RequestMethod.GET)
    public ModelAndView getHome(Model model) {

        log.debug("getHome");
        ModelAndView homeModelAndView = new ModelAndView("home");
//        throw new MyException();
        return homeModelAndView;
    }

    @ExceptionHandler(MyException.class)
    public void handleMyException (MyException ex)
        throws MyException {

        throw ex;
    }

    public static class MyException extends Exception {

    }
}
