package com.rob.betBot.exchange;

import com.rob.betBot.BetManager;
import com.rob.betBot.EventManager;
import com.rob.betBot.PricesManager;

public interface Exchange {

    // IDs for exchanges
    public final static int EMULATED_EXCHANGE_ID = 0;
    public final static int TEST_EXCHANGE_ID = 1;
    public final static int BETFAIR_EXCHANGE_ID = 2;
    public final static int WILLIAM_HILL_EXCHANGE_ID = 3;
    public final static int BETBILLHILL_EXCHANGE_ID = 99;

    /**
     * Gets this exchanges ID.
     *
     * @return
     */
    public int getExchangeId();

    /**
     * Gets a name for this exchange.
     *
     * @return
     */
    public String getName();

    /**
     * Gets the username for the user who logged onto this exchange.
     *
     * @return
     */
    public String getUsername();

    public PricesManager getPricesManager();

    public BetManager getBetManager();

    public EventManager getEventManager();
}
