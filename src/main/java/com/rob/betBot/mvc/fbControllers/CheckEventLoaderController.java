package com.rob.betBot.mvc.fbControllers;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rob.betBot.exchange.williamHill.WilliamHillEventLoader;
import com.rob.betBot.model.wh.WhEventParentData;

@Controller("FbCheckEventLoaderController")
@RequestMapping("/fb/eventloadingcheck")
public class CheckEventLoaderController extends BaseController {

    @Autowired
    private WilliamHillEventLoader eventLoader;

    @RequestMapping(method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getEventsStillLoading() {

        WhEventParentData updatingEventParent = eventLoader.getEventParent();
        boolean stillLoading = updatingEventParent != null;
        String response = "{\"stillLoading\": " + String.valueOf(stillLoading) + "}";
        return response;
    }
}
