package com.rob.betBot;

public class AbstractBet extends BetRequestImpl implements Bet {

    private final String betId;
    private final int exchangeId;
    protected double matchedSoFar;
    protected BetPricesExchangeInfo pricesInfo;
    protected boolean complete;
    protected boolean won;

    public AbstractBet(int exchangeId, String betId, BetRequest betRequest) {

        super(betRequest.getPrice(), betRequest.getExchangePrice(), betRequest.getAmount(), betRequest.getEventId(),
            betRequest.getEventName(), betRequest.getMarketId(), betRequest.getMarketType(), betRequest.getRunnerId(),
            betRequest.getRunnerName(), betRequest.getBetType());

        this.betId = betId;
        this.exchangeId = exchangeId;
    }

    @Override
    public int getExchangeId() {
        return exchangeId;
    }

    @Override
    public String getBetId() {
        return betId;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public boolean won() {
        return won;
    }

    @Override
    public double getMatchedSoFar() {
        return matchedSoFar;
    }

    public void setMatchedSoFar(double matchedSoFar) {
        this.matchedSoFar = matchedSoFar;
    }

    @Override
    public BetPricesExchangeInfo getPricesInfo() {
        return pricesInfo;
    }

    public void setPricesInfo(BetPricesExchangeInfo pricesInfo) {
        this.pricesInfo = pricesInfo;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("betId").append(betId)
            .append(",exchangeId=").append(exchangeId)
            .append(",complete=").append(complete)
            .append(",won=").append(won)
            .append(",pricesInfo=[").append(pricesInfo)
            .append("]").toString();
    }
}
