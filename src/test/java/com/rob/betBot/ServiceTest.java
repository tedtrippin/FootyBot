package com.rob.betBot;

import java.util.Arrays;

import org.junit.Test;

import com.rob.betBot.bots.modules.ModuleManager;


public class ServiceTest {

    @Test
    public void testStuff() {

        String[] names = ModuleManager.instance().getFilterNames();
        System.out.println(Arrays.toString(names));
    }

    public void testService() {
/*
        try {
            AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring/testApplicationContext.xml");
            context.registerShutdownHook();

            BetEngine betEngine = (BetEngine)context.getBean("BetEngineBean");
            betEngine.start();

            TestExchange exchange = new TestExchange();
            betEngine.loadRaces(exchange);
            Collection<Race> race = betEngine.getLoadedRaces(exchange);

        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
*/
        }
}
