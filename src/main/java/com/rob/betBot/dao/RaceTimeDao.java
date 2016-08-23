package com.rob.betBot.dao;

import javax.persistence.PersistenceException;

import com.rob.betBot.model.RaceTimeData;

public interface RaceTimeDao extends GenericDao<RaceTimeData> {

    /**
     * Gets the race time with matching name.
     *
     * @param eventName eg. "Font 2m4f Nov Hrd"
     * @return
     */
    public RaceTimeData getRaceTime(String raceName)
        throws PersistenceException;
}
