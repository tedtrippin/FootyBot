package com.rob.betBot.mvc;

import javax.servlet.http.Cookie;

public class BotCookie extends Cookie {

    private static final long serialVersionUID = 1L;

    public final static String BOT_COOKIE_NAME = "bot";

    public BotCookie (String botString) {
        super (BOT_COOKIE_NAME, botString);
        setMaxAge(Integer.MAX_VALUE);
    }
}
