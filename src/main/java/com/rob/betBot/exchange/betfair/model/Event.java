package com.rob.betBot.exchange.betfair.model;

public class Event {

	private String id;
	private String name;
	private String countryCode;
	private String timezone;
	private String venue;
	private String openDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	@Override
	public int hashCode() {
	    return id.hashCode();
	}

	@Override
	public boolean equals(Object o) {

	    if (o == null)
	        return false;

	    if (!(o instanceof Event))
	        return false;

	    Event other = (Event)o;
	    return other.id.equals(id);
	}

	@Override
    public String toString() {
	    return new StringBuilder()
	        .append(",id=").append(id)
	        .append(",name=").append(name)
	        .append(",countryCode=").append(countryCode)
	        .append(",timezone=").append(timezone)
	        .append(",venue=").append(venue)
	        .append(",openDate=").append(openDate).toString();
	}
}
