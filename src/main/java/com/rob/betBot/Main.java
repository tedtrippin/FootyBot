package com.rob.betBot;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rob.betBot.engine.BetEngine;

//import org.springframework.orm.hibernate4.support.;

public class Main {

    public static void main(String[] args) {

        AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
        context.registerShutdownHook();

//        BetEngine betEngine = new BetEngine(new EmulatedRaceManager(),
//            new EmulatedBetManager(), new EmulatedPricesManager(), null);
        BetEngine betEngine = (BetEngine)context.getBean("BetEngineBean");
        betEngine.start();
    }
}
