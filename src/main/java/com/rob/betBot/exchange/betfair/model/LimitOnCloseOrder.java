package com.rob.betBot.exchange.betfair.model;

public class LimitOnCloseOrder {
	private double liability;
	private double price;

	public double getLiability() {
		return liability;
	}

	public void setLiability(double liability) {
		this.liability = liability;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
