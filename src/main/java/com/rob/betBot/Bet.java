package com.rob.betBot;

public interface Bet extends BetRequest {

    /**
     * Gets the bets ID.
     *
     * @return
     */
    public String getBetId();

    /**
     * Returns true once this bet has lost/won.
     *
     * @return
     */
    public boolean isComplete();

    /**
     * Returns true if this bet won, ie. made a profit.
     *
     * @return
     */
    public boolean won();

    /**
     * The price at which this bet was made.
     *
     * @return
     */
    @Override
    public double getPrice();

    /**
     * The price on the exchange when this bet was made.
     *
     * @return
     */
    @Override
    public double getExchangePrice();

    /**
     * The original bet amount.
     *
     * @return
     */
    @Override
    public double getAmount();

    /**
     * The ID of exchange that made the bet.
     *
     * @return
     */
    public int getExchangeId();

    /**
     * The ID of the event bet on.
     *
     * @return
     */
    @Override
    public String getEventId();

    /**
     * the name of the event.
     *
     * @return
     */
    @Override
    public String getEventName();

    /**
     * The ID of the market bet on.
     *
     * @return
     */
    @Override
    public String getMarketId();

    /**
     * Name of the market.
     *
     * @return
     */
    @Override
    public MarketType getMarketType();

    /**
     * The ID of the selection bet on.
     *
     * @return
     */
    @Override
    public long getRunnerId();

    /**
     * Name of the runner.
     *
     * @return
     */
    @Override
    public String getRunnerName();

    /**
     * How much has been matched so far.
     *
     * @return
     */
    public double getMatchedSoFar();

    /**
     * When different exchanges are used for
     * prices/betting this holds the info from
     * the PricesManager.
     *
     * @return
     */
    public BetPricesExchangeInfo getPricesInfo();

    /**
     * Gets the type of this bet (BACK/LAY).
     *
     * @return
     */
    @Override
    public BetTypeEnum getBetType();
}
