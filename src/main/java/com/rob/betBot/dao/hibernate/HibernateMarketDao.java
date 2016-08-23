package com.rob.betBot.dao.hibernate;

import com.rob.betBot.dao.MarketDao;
import com.rob.betBot.model.MarketData;

public class HibernateMarketDao
    extends AbstractHibernateDao<MarketData>
    implements MarketDao {

    public HibernateMarketDao() {
        super(MarketData.class);
    }
}
