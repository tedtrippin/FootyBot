package com.rob.betBot.mvc.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private final static SimpleDateFormat format = new SimpleDateFormat("dd MMM, HH:mm");

    public static String getDate(long dateLong) {
        return format.format(new Date(dateLong));
    }
}
