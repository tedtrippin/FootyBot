package com.rob.betBot.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.rob.betBot.exchange.williamHill.WilliamHillFootyEventManager;

@Component
public class SpringStartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private WilliamHillFootyEventManager footyEventManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        footyEventManager.loadEvents();
    }
}
