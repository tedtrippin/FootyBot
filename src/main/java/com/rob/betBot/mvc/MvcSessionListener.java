package com.rob.betBot.mvc;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class MvcSessionListener implements HttpSessionListener {

    private final int ONE_HOUR_IN_SECONDS = 60 * 60 * 24;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(ONE_HOUR_IN_SECONDS);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
    }
}
