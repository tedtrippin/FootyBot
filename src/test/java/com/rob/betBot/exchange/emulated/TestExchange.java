package com.rob.betBot.exchange.emulated;

import com.rob.betBot.exchange.AbstractExchange;

public class TestExchange extends AbstractExchange {

    public TestExchange() {
        super(1, "TestExchange", "testUser", new TestPricesManager(), new TestBetManager(), new TestRaceManager());
    }
}
