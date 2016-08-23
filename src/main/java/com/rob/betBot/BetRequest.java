package com.rob.betBot;

public interface BetRequest {


    /**
     * The price for this bet.
     *
     * @return
     */
    public double getPrice();
    // TODO - do we need a requestedPrice and acceptedPrice

    /**
     * The price on the exchange when this bet was made.
     *
     * @return
     */
    public double getExchangePrice();

    /**
     * The original bet amount.
     *
     * @return
     */
    public double getAmount();

    /**
     * The ID of the event bet on.
     *
     * @return
     */
    public String getEventId();

    /**
     * the name of the event.
     *
     * @return
     */
    public String getEventName();

    /**
     * The ID of the market bet on.
     *
     * @return
     */
    public String getMarketId();

    /**
     * Name of the market.
     *
     * @return
     */
    public MarketType getMarketType();

    /**
     * The ID of the selection bet on.
     *
     * @return
     */
    public long getRunnerId();

    /**
     * Name of the runner.
     *
     * @return
     */
    public String getRunnerName();

    /**
     * Gets the type of this bet (BACK/LAY).
     *
     * @return
     */
    public BetTypeEnum getBetType();

}
