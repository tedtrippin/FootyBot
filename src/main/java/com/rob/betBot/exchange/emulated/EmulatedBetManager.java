package com.rob.betBot.exchange.emulated;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rob.betBot.Bet;
import com.rob.betBot.BetRequest;
import com.rob.betBot.exception.BetCancelException;
import com.rob.betBot.exception.BetNotFoundException;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;
import com.rob.betBot.exchange.AbstractBetManager;

public class EmulatedBetManager extends AbstractBetManager {

    private final static Logger log = LogManager.getLogger(EmulatedBetManager.class);

    @Override
    public void updateBets(Collection<Bet> bets)
        throws ExchangeException, BetNotFoundException {
    }

    @Override
    protected Collection<Bet> doPlaceBets(Collection<BetRequest> betRequests)
        throws ExchangeException, BetPlaceException {

        Collection<Bet> bets = new ArrayList<>();
        betRequests.forEach(b -> bets.add(new EmulatedBet(b)));

        bets.forEach(b -> log.debug("Placed emulated bet[" + b + "]"));

        return bets;
    }

    @Override
    protected Collection<Bet> doCancelBets(Collection<Bet> bets)
        throws ExchangeException, BetCancelException {

        return null;
    }
}
