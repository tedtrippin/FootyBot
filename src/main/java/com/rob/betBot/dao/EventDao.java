package com.rob.betBot.dao;

import java.util.List;

import com.rob.betBot.model.EventData;

public interface EventDao extends GenericDao<EventData> {

    /**
     * Gets the largest event ID in the table. Used
     * to initialise the {@link EventIdGenerator}.
     *
     * @return
     */
    public long getMaxId();

    /**
     * Gets an event based on exchange ID and the exchange
     * event ID.
     *
     * @param exchangeId
     * @param eventId
     * @return
     */
    public EventData getById(int exchangeId, String eventId);

    /**
     * Gets an event based on exchange ID, exchange event ID
     * and start time.
     *
     * @param exchangeId
     * @param eventId
     * @param startTime
     * @return
     */
    public EventData getByIdAndStartTime(int exchangeId, String eventId, long startTime);

    /**
     * Gets all events after with an expected start time
     * greater than the one passed in. The returned list is
     * ordered by start date.
     *
     * @param exchangeId
     * @param expectedStartTime
     * @return Unfinished events in order of start date
     */
    public List<EventData> getByExpectedStartTime(int exchangeId, long expectedStartTime);

}
