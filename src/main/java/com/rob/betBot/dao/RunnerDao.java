package com.rob.betBot.dao;

import com.rob.betBot.model.RunnerData;

public interface RunnerDao extends GenericDao<RunnerData> {

    public RunnerData getRunnerByRunnerId (int exchangeId, long runnerId);
}
