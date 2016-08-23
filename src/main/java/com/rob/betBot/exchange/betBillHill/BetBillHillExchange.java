package com.rob.betBot.exchange.betBillHill;

import com.rob.betBot.exchange.AbstractExchange;
import com.rob.betBot.exchange.combined.WilliamHillBetfairFootballBetManager;
import com.rob.betBot.exchange.williamHill.WilliamHillFootyEventManager;
import com.rob.betBot.exchange.williamHill.WilliamHillPricesManager;

/**
 * Combines William Hill event and prices manager with betfairs bet manager.
 *
 */
public class BetBillHillExchange extends AbstractExchange {

    protected BetBillHillExchange(String username, WilliamHillPricesManager pricesManager,
        WilliamHillBetfairFootballBetManager betManager, WilliamHillFootyEventManager raceManager) {

        super(BETBILLHILL_EXCHANGE_ID, "BetBillHill", username, pricesManager, betManager, raceManager);
    }
}
