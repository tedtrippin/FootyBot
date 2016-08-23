package com.rob.betBot.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rob.betBot.dao.PricesDao;
import com.rob.betBot.model.PricesData;

@Repository
public class HibernatePricesDao
    extends AbstractHibernateDao<PricesData>
    implements PricesDao {

    public HibernatePricesDao() {
        super(PricesData.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public List<PricesData> getPrices (int exchangeId, long raceId) {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(PricesData.class);
        criteria.add(Restrictions.eq("exchangeId", exchangeId));
        criteria.add(Restrictions.eq("raceId", raceId));
        criteria.addOrder(Order.asc("time"));
        return criteria.list();
    }
}
