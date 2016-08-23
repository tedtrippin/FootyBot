package com.rob.betBot.mvc;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.rob.betBot.conf.ConfigManager;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ConfigManager.instance().start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ConfigManager.instance().stop();
    }
}