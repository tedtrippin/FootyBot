package com.rob.betBot.exchange;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rob.betBot.Bet;
import com.rob.betBot.BetManager;
import com.rob.betBot.BetRequest;
import com.rob.betBot.engine.BetListener;
import com.rob.betBot.exception.BetCancelException;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;

public abstract class AbstractBetManager implements BetManager {

    private static Logger log = LogManager.getLogger(AbstractBetManager.class);

    private final Collection<BetListener> listeners = new HashSet<>();

    @Override
    public void addBetListener(BetListener betListener) {
        listeners.add(betListener);
    }

    @Override
    public Collection<Bet> placeBets(Collection<BetRequest> betRequests)
        throws ExchangeException, BetPlaceException {

        Collection<Bet> bets = doPlaceBets(betRequests);
        Iterator<BetListener> itr = listeners.iterator();
        while (itr.hasNext()) {
            BetListener listener = itr.next();
            try {
                bets.forEach(b -> listener.onBetPlaced(b));
            } catch (Exception ex) {
                log.error("BetListener.onBetPlaced() threw an exception", ex);
            }
        }
        return bets;
    }

    @Override
    public Collection<Bet> cancelBets(Collection<Bet> bets)
        throws ExchangeException, BetCancelException {

        Collection<Bet> partMatchedBets = doCancelBets(bets);
        Map<String, Bet> partMatchedBetMap = new HashMap<>();
        partMatchedBets.forEach(b -> partMatchedBetMap.put(b.getBetId(), b));

        Iterator<BetListener> itr = listeners.iterator();
        while (itr.hasNext()) {
            BetListener listener = itr.next();
            try {
                bets.forEach(b -> listener.onBetCancelled(b, partMatchedBetMap.get(b.getBetId())));
            } catch (Exception ex) {
                log.error("BetListener.onBetCancelled() threw an exception", ex);
            }
        }
        return partMatchedBets;
    }

    protected abstract Collection<Bet> doPlaceBets(Collection<BetRequest> betRequests)
        throws ExchangeException, BetPlaceException;

    protected abstract Collection<Bet> doCancelBets(Collection<Bet> betRequests)
        throws ExchangeException, BetCancelException;
}
