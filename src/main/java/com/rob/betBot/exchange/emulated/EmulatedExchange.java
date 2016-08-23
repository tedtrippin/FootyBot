package com.rob.betBot.exchange.emulated;

import com.rob.betBot.exchange.AbstractExchange;

public class EmulatedExchange extends AbstractExchange {

    public EmulatedExchange() {
        super(EMULATED_EXCHANGE_ID, "Emulated exchange", "emulatedUser", new EmulatedPriceManager(),
            new EmulatedBetManager(), new EmulatedRaceManager());
    }
}
