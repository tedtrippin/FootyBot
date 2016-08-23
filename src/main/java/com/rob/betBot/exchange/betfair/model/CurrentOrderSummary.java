package com.rob.betBot.exchange.betfair.model;

import java.util.Date;

public class CurrentOrderSummary {

    private String betId;
    private String marketId;
    private long selectionId;
    private double handicap;
    private PriceSize priceSize;
    private double bspLiability;
    private SideEnum side;
    private OrderStatusEnum status;
    private PersistenceTypeEnum persistenceType;
    private OrderTypeEnum orderType;
    private Date placedDate;
    private Date matchedDate;
    private double averagePriceMatched;
    private double sizeMatched;
    private double sizeRemaining;
    private double sizeLapsed;
    private double sizeCancelled;
    private double sizeVoided;
    private String regulatorAuthCode;
    private String regulatorCode;

    public String getBetId() {
        return betId;
    }

    public void setBetId(String betId) {
        this.betId = betId;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public long getSelectionId() {
        return selectionId;
    }

    public void setSelectionId(long selectionId) {
        this.selectionId = selectionId;
    }

    public double getHandicap() {
        return handicap;
    }

    public void setHandicap(double handicap) {
        this.handicap = handicap;
    }

    public PriceSize getPriceSize() {
        return priceSize;
    }

    public void setPriceSize(PriceSize priceSize) {
        this.priceSize = priceSize;
    }

    public double getBspLiability() {
        return bspLiability;
    }

    public void setBspLiability(double bspLiability) {
        this.bspLiability = bspLiability;
    }

    public SideEnum getSide() {
        return side;
    }

    public void setSide(SideEnum side) {
        this.side = side;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public PersistenceTypeEnum getPersistenceType() {
        return persistenceType;
    }

    public void setPersistenceType(PersistenceTypeEnum persistenceType) {
        this.persistenceType = persistenceType;
    }

    public OrderTypeEnum getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }

    public Date getPlacedDate() {
        return placedDate;
    }

    public void setPlacedDate(Date placedDate) {
        this.placedDate = placedDate;
    }

    public Date getMatchedDate() {
        return matchedDate;
    }

    public void setMatchedDate(Date matchedDate) {
        this.matchedDate = matchedDate;
    }

    public double getAveragePriceMatched() {
        return averagePriceMatched;
    }

    public void setAveragePriceMatched(double averagePriceMatched) {
        this.averagePriceMatched = averagePriceMatched;
    }

    public double getSizeMatched() {
        return sizeMatched;
    }

    public void setSizeMatched(double sizeMatched) {
        this.sizeMatched = sizeMatched;
    }

    public double getSizeRemaining() {
        return sizeRemaining;
    }

    public void setSizeRemaining(double sizeRemaining) {
        this.sizeRemaining = sizeRemaining;
    }

    public double getSizeLapsed() {
        return sizeLapsed;
    }

    public void setSizeLapsed(double sizeLapsed) {
        this.sizeLapsed = sizeLapsed;
    }

    public double getSizeCancelled() {
        return sizeCancelled;
    }

    public void setSizeCancelled(double sizeCancelled) {
        this.sizeCancelled = sizeCancelled;
    }

    public double getSizeVoided() {
        return sizeVoided;
    }

    public void setSizeVoided(double sizeVoided) {
        this.sizeVoided = sizeVoided;
    }

    public String getRegulatorAuthCode() {
        return regulatorAuthCode;
    }

    public void setRegulatorAuthCode(String regulatorAuthCode) {
        this.regulatorAuthCode = regulatorAuthCode;
    }

    public String getRegulatorCode() {
        return regulatorCode;
    }

    public void setRegulatorCode(String regulatorCode) {
        this.regulatorCode = regulatorCode;
    }
}
