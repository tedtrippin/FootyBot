package com.rob.betBot.dao;

import java.util.List;

import com.rob.betBot.model.PricesData;

public interface PricesDao {

    /**
     * Gets the prices for a race sorted by time.
     *
     * @param exchangeId
     * @param raceId
     * @return
     */
    public List<PricesData> getPrices (int exchangeId, long raceId);
}
