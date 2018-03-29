package com.rob.betBot.mvc.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handleAllException(Exception ex){
        System.err.println("Global exception handler - ");
        ex.printStackTrace(System.err);
    }
}
