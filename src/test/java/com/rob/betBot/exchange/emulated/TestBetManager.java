package com.rob.betBot.exchange.emulated;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rob.betBot.Bet;
import com.rob.betBot.BetManager;
import com.rob.betBot.BetRequest;
import com.rob.betBot.engine.BetListener;
import com.rob.betBot.exception.BetCancelException;
import com.rob.betBot.exception.ExchangeException;


public class TestBetManager implements BetManager {

    private static Logger log = LogManager.getLogger(EmulatedBetManager.class);

    @Override
    public void updateBets(Collection<Bet> bets) {
        log.debug("Emulated update bet");
    }

    @Override
    public Collection<Bet> placeBets(Collection<BetRequest> betRequests) {

        log.debug("Placing emulated bet");
        Collection<Bet> bets = new ArrayList<>();
        betRequests.forEach(b -> bets.add(new EmulatedBet(b)));
        return bets;
    }

    @Override
    public Collection<Bet> cancelBets(Collection<Bet> bets)
        throws ExchangeException, BetCancelException {

        return null;
    }

    @Override
    public void addBetListener(BetListener betListener) {
    }
}
