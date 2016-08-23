package com.rob.betBot.exchange.emulated;

import java.util.UUID;

import com.rob.betBot.AbstractBet;
import com.rob.betBot.BetRequest;
import com.rob.betBot.exchange.Exchange;

public class EmulatedBet extends AbstractBet {

    EmulatedBet(BetRequest betRequest) {
        super(Exchange.EMULATED_EXCHANGE_ID, UUID.randomUUID().toString(), betRequest);
    }
}
