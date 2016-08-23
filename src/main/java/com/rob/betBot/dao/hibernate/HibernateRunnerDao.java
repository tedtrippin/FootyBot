package com.rob.betBot.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rob.betBot.dao.RunnerDao;
import com.rob.betBot.model.RunnerData;

@Repository
public class HibernateRunnerDao
    extends AbstractHibernateDao<RunnerData>
    implements RunnerDao {

    public HibernateRunnerDao() {
        super(RunnerData.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public RunnerData getRunnerByRunnerId (int exchangeId, long exchangeRunnerId) {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(RunnerData.class);
        criteria.add(Restrictions.eq("exchangeRunnerId", exchangeRunnerId));
        criteria.add(Restrictions.eq("exchangeId", exchangeId));
        return (RunnerData) criteria.uniqueResult();
    }
}
