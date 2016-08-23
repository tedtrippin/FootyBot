package com.rob.betBot.engine;

import com.rob.betBot.Bet;

public interface BetListener {

    /**
     * Called when a bet has successfully been placed.
     *
     * @param bet
     */
    public void onBetPlaced(Bet bet);

    /**
     * Called when a bet has successfully been cancelled.
     * {@code originalBet} is the bet that was originally
     * placed and tried to cancel. If {@code partMatchedBet}
     * is not null then it means all/part of the bet was matched.
     * This bet says how much was matched.
     *
     * @param bet
     */
    public void onBetCancelled(Bet originalBet, Bet partMatchedBet);
}
