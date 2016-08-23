package com.rob.betBot.exchange.betfair.model;

public class EventType {

	private int id;
	private String name;

	public EventType() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public String toString() {
        return new StringBuilder()
            .append("id=").append(id)
            .append(",name=").append(name).toString();
	}
}
