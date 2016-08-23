package com.rob.betBot;


/**
 * Lightweight {@link Bet} for making requests.
 *
 * @author robert.haycock
 *
 */
public class BetRequestImpl implements BetRequest {

    private final double exchangePrice;
    private final double amount;
    private final String eventId;
    protected final String eventName;
    private final String marketId;
    protected final MarketType marketType;
    private final long runnerId;
    protected final String runnerName;
    private final BetTypeEnum betType;
    protected double price;

    public BetRequestImpl(double price, double exchangePrice, double amount, String eventId, String eventName,
        String marketId, MarketType marketType, long runnerId, String runnerName, BetTypeEnum betType) {

        this.price = price;
        this.exchangePrice = exchangePrice;
        this.amount = amount;
        this.betType = betType;
        this.eventId = eventId;
        this.eventName = eventName;
        this.marketId = marketId;
        this.marketType = marketType;
        this.runnerId = runnerId;
        this.runnerName = runnerName;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public double getExchangePrice() {
        return exchangePrice;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public String getMarketId() {
        return marketId;
    }

    @Override
    public MarketType getMarketType() {
        return marketType;
    }

    @Override
    public long getRunnerId() {
        return runnerId;
    }

    @Override
    public String getRunnerName() {
        return runnerName;
    }

    @Override
    public BetTypeEnum getBetType() {
        return betType;
    }

    @Override
    public String toString() {
        return new StringBuilder("betType: ").append(betType)
            .append(",amount:").append(amount)
            .append(",price:").append(price)
            .append(",exchangePrice:").append(exchangePrice)
            .append(",event: ").append(eventName)
            .append(",market: ").append(marketType)
            .append(",runner: ").append(runnerName).toString();
    }
}
