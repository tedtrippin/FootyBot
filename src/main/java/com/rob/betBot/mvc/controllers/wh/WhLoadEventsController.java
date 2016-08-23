package com.rob.betBot.mvc.controllers.wh;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.dao.WhEventParentDao;
import com.rob.betBot.exchange.williamHill.WilliamHillEventLoader;
import com.rob.betBot.model.wh.WhEventParentData;
import com.rob.betBot.mvc.controllers.BaseController;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;

@Controller
@RequestMapping("/wh/loadevents")
public class WhLoadEventsController extends BaseController {

    private final Logger log = Logger.getLogger(WhLoadEventsController.class);

    @Autowired
    private WhEventParentDao whEeventParentDao;

    @Autowired
    private WilliamHillEventLoader eventLoader;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getLoadEvent(@RequestParam("id") String eventParentId) {

        log.debug("getLoadEvent");

        WhEventParentData eventParent = whEeventParentDao.getById(Long.valueOf(eventParentId));

        WhEventParentData updatingEventParent = eventLoader.getEventParent();
        if (updatingEventParent != null)
            addErrorMessage("Still loading " + updatingEventParent.getName());
        else if (eventParent == null)
            addErrorMessage("Couldn't find that ID");
        else
            eventLoader.loadEvent(eventParent);

        return new ModelAndViewBuilder("redirect:eventparents").build();
    }
}
