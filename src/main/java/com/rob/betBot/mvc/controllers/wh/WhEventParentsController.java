package com.rob.betBot.mvc.controllers.wh;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rob.betBot.dao.IdGenerator;
import com.rob.betBot.dao.WhEventParentDao;
import com.rob.betBot.exchange.williamHill.WilliamHillEventLoader;
import com.rob.betBot.model.wh.WhEventParentData;
import com.rob.betBot.mvc.ModelKeys;
import com.rob.betBot.mvc.controllers.BaseController;
import com.rob.betBot.mvc.model.EventParent;
import com.rob.betBot.mvc.model.ModelAndViewBuilder;

@Controller
@RequestMapping()
public class WhEventParentsController extends BaseController {

    private final Logger log = Logger.getLogger(WhEventParentsController.class);

    @Autowired
    private WhEventParentDao whEeventParentDao;

    @Autowired
    public IdGenerator idGenerator;

    @Autowired
    private WilliamHillEventLoader eventLoader;

    @ModelAttribute
    public EventParent getEventParent() {
        return new EventParent();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wh/eventparents")
    public ModelAndView getWhEventParents() {

        log.debug("getWhEventParents");

        ModelAndViewBuilder modelAndView = new ModelAndViewBuilder("wh/eventParents");
        Collection<WhEventParentData> eventParents = whEeventParentDao.getAll();
        modelAndView.add(ModelKeys.EVENT_PARENTS, eventParents);

        WhEventParentData updatingEventParent = eventLoader.getEventParent();
        if (updatingEventParent != null)
            addErrorMessage("Loading " + updatingEventParent.getName());

        return modelAndView.build();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/wh/eventparents")
    public ModelAndView postWhEventParent(@ModelAttribute("eventParent") EventParent eventParent) {

        try {
            long id = idGenerator.getNextId(WhEventParentData.class);
            WhEventParentData data = new WhEventParentData(id, eventParent.getName(), eventParent.getUrl());
            whEeventParentDao.saveOrUpdate(data);

        } catch (Exception ex) {
            log.error("Unable to save new WhEventParentData", ex);
            addErrorMessage("Couldn't save the event parent, check logs for details");
        }

        return getWhEventParents();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wh/deleteeventparents")
    public ModelAndView deleteWhEventParent(@RequestParam("id") String eventParentId) {

        try {
            WhEventParentData whEeventParent = whEeventParentDao.getById(Long.valueOf(eventParentId));
            whEeventParentDao.delete(whEeventParent);
        } catch (Exception ex) {
            log.error("Unable to remove WhEventParentData", ex);
            addErrorMessage("Couldn't remove the event parent, check logs for details");
        }

        return getWhEventParents();
    }
}
