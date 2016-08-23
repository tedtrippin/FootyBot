package com.rob.betBot.exchange.betfair;

import org.springframework.beans.factory.annotation.Autowired;

import com.rob.betBot.dao.EventDao;
import com.rob.betBot.dao.IdGenerator;
import com.rob.betBot.dao.MarketDao;
import com.rob.betBot.dao.RunnerDao;
import com.rob.betBot.exchange.Exchange;
import com.rob.betBot.exchange.betfair.model.RunnerCatalog;
import com.rob.betBot.model.RunnerData;

abstract class BetfairMarketConverter {

    @Autowired
    protected RunnerDao runnerDao;

    @Autowired
    protected MarketDao marketDao;

    @Autowired
    protected EventDao eventDao;

    @Autowired
    protected IdGenerator idGenerator;

    protected final BetfairHelper betfairHelper;

    public BetfairMarketConverter() {
        betfairHelper = new BetfairHelper();
    }

    protected RunnerData getRunnerData(RunnerCatalog r) {

        long selectionId = r.getSelectionId();
        RunnerData runnerData = runnerDao.getRunnerByRunnerId(Exchange.BETFAIR_EXCHANGE_ID, selectionId);
        if (runnerData != null)
            return runnerData;

        long id = idGenerator.getNextId(RunnerData.class);
        runnerData = new RunnerData(id, Exchange.BETFAIR_EXCHANGE_ID, selectionId, r.getRunnerName());
        runnerDao.saveOrUpdate(runnerData);

        return runnerData;
    }
}

