package com.rob.betBot;

import java.util.Collection;

import com.rob.betBot.engine.BetListener;
import com.rob.betBot.exception.BetCancelException;
import com.rob.betBot.exception.BetNotFoundException;
import com.rob.betBot.exception.BetPlaceException;
import com.rob.betBot.exception.ExchangeException;

public interface BetManager {

    /**
     * Checks a bet for updates on the exchange.
     *
     * @param bet
     */
    public void updateBets(Collection<Bet> bets)
        throws ExchangeException, BetNotFoundException;

    /**
     * Place a bet on a market.
     *
     * @param event
     * @param market
     * @param runner
     * @param price
     * @param amount
     * @param exchangePrice
     * @return
     * @throws ExchangeException
     * @throws BetPlaceException
     */
    public Collection<Bet> placeBets(Collection<BetRequest> bets)
        throws ExchangeException, BetPlaceException;

    /**
     * Attempts to cancel a bet. If it was part matched then returns
     * the part matched bet.
     *
     * @param bet Bet to cancel
     * @return Part-matched bet that couldn't be cancelled.
     * @throws ExchangeException
     * @throws BetPlaceException
     */
    public Collection<Bet> cancelBets(Collection<Bet> bets)
        throws ExchangeException, BetCancelException;

    /**
     * Adds a {@link BetListener} to this BetManager.
     *
     * @param betListener
     */
    public void addBetListener(BetListener betListener);
}
