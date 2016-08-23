package com.rob.betBot.exchange.betfair.model;

public class PlaceInstruction {

	private OrderTypeEnum orderType;
	private long selectionId;
	private double handicap;
	private SideEnum side;
	private LimitOrder limitOrder;
	private LimitOnCloseOrder limitOnCloseOrder;
	private MarketOnCloseOrder marketOnCloseOrder;

	public OrderTypeEnum getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderTypeEnum orderType) {
		this.orderType = orderType;
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

	public SideEnum getSide() {
		return side;
	}

	public void setSide(SideEnum side) {
		this.side = side;
	}

	public LimitOrder getLimitOrder() {
		return limitOrder;
	}

	public void setLimitOrder(LimitOrder limitOrder) {
		this.limitOrder = limitOrder;
	}

	public LimitOnCloseOrder getLimitOnCloseOrder() {
		return limitOnCloseOrder;
	}

	public void setLimitOnCloseOrder(LimitOnCloseOrder limitOnCloseOrder) {
		this.limitOnCloseOrder = limitOnCloseOrder;
	}

	public MarketOnCloseOrder getMarketOnCloseOrder() {
		return marketOnCloseOrder;
	}

	public void setMarketOnCloseOrder(MarketOnCloseOrder marketOnCloseOrder) {
		this.marketOnCloseOrder = marketOnCloseOrder;
	}
}
