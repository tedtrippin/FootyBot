package com.rob.betBot;

import com.rob.betBot.exception.ExchangeException;

public interface PricesManager {

    /**
     * Updates an event with the latest prices or marks it as finished
     * when event is over. Returns false if not able to get latest prices.
     *
     * @param race
     */
    public boolean updateEvent(Event event, long currentTime)
        throws ExchangeException;
}
