package com.rob.betBot.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rob.betBot.dao.IdGenerator;

@Repository
public class HibernateIdGenerator implements IdGenerator {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    @Transactional
    public synchronized long getNextId(Class<?> clazz) {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session
            .createCriteria(clazz)
            .setProjection(Projections.max("id"));
        Long maxId = (Long) criteria.uniqueResult();

        long l = maxId == null ? 0 : maxId.longValue();
        return l + 1;
    }
}
