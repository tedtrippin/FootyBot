package com.rob.betBot.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rob.betBot.dao.RaceTimeDao;
import com.rob.betBot.model.RaceTimeData;

@Repository
public class HibernateRaceTimeDao
    extends AbstractHibernateDao<RaceTimeData>
    implements RaceTimeDao {

    public HibernateRaceTimeDao() {
        super(RaceTimeData.class);
    }

    @Override
    @Transactional
    public RaceTimeData getRaceTime(String raceName) {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(RaceTimeData.class)
            .add( Restrictions.eq("raceName", raceName).ignoreCase() );

        return (RaceTimeData) criteria.uniqueResult();
    }
}
