package com.rob.betBot.exchange.betfair.model;

public class LimitOrder {

	private double size;
	private double price;
	private PersistenceTypeEnum persistenceType;

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public PersistenceTypeEnum getPersistenceType() {
		return persistenceType;
	}

	public void setPersistenceType(PersistenceTypeEnum persistenceType) {
		this.persistenceType = persistenceType;
	}
}
