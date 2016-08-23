package com.rob.betBot.dao.arangodb;

import java.util.Collection;
import java.util.List;

import javax.persistence.PersistenceException;

import com.rob.betBot.dao.PricesDao;
import com.rob.betBot.model.PricesData;

public class ArangoPricesDao implements PricesDao {

    @Override
    public List<PricesData> getPrices(int exchangeId, long raceId)
        throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    protected String getCollection() {
        return "prices";
    }

    protected Class<PricesData> getDataClass() {
        return PricesData.class;
    }

    public Collection<PricesData> getAll()
        throws PersistenceException {

        return null;
    }
}
