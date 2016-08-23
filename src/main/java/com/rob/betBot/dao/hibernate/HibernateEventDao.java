package com.rob.betBot.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rob.betBot.dao.EventDao;
import com.rob.betBot.model.EventData;

@Repository
public class HibernateEventDao
    extends AbstractHibernateDao<EventData>
    implements EventDao {

    public HibernateEventDao() {
        super(EventData.class);
    }

    @Override
    @Transactional
    public long getMaxId() {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session
            .createCriteria(EventData.class)
            .setProjection(Projections.max("id"));
        Long maxId = (Long) criteria.uniqueResult();
        return maxId;
    }

    @Override
    @Transactional
    public EventData getById(int exchangeId, String exchangeEventId) {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session
            .createCriteria(EventData.class)
            .add(Restrictions.eq("exchangeId", exchangeId))
            .add(Restrictions.eq("exchangeEventId", exchangeEventId));
        return (EventData) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<EventData> getByExpectedStartTime(int exchangeId, long expectedStartTime) {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session
            .createCriteria(EventData.class)
            .add(Restrictions.eq("exchangeId", exchangeId))
            .add(Restrictions.gt("expectedStartTime", expectedStartTime));
        return criteria.list();
    }

    @Override
    @Transactional
    public EventData getByIdAndStartTime(int exchangeId, String exchangeEventId, long startTime) {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session
            .createCriteria(EventData.class)
            .add(Restrictions.eq("exchangeId", exchangeId))
            .add(Restrictions.eq("expectedStartTime", startTime))
        .add(Restrictions.eq("exchangeEventId", exchangeEventId));
        return (EventData) criteria.uniqueResult();
    }
}
