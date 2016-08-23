package com.rob.betBot;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.rob.betBot.exchange.williamHill.WilliamHillFootyEventManager;

@Component
public class Startup implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger log = Logger.getLogger(Startup.class);

    @Autowired
    private WilliamHillFootyEventManager williamHillFootyEventManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        try {
            ApplicationContext context = event.getApplicationContext();
//            WilliamHillFootyEventManager whEventManager = context.getBean(WilliamHillFootyEventManager.class);
//            whEventManager.loadEvents();
        } catch (Exception ex) {
            log.error("Error loading william hill events", ex);
        }
    }
}
