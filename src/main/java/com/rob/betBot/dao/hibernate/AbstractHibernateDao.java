package com.rob.betBot.dao.hibernate;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractHibernateDao<T> {

    private Class<T> persistentClass;

    @Autowired
    SessionFactory sessionFactory;

    public AbstractHibernateDao(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public T getById(long id) {

        Session session = sessionFactory.getCurrentSession();
        return (T) session.get(getPersistentClass(), id);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<T> getAll() {

        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(getPersistentClass());
        return criteria.list();
    }

    @Transactional
    public void saveOrUpdate(T t) {

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(t);
    }

    @Transactional
    public void saveAll(Set<T> ts) {

        Session session = sessionFactory.getCurrentSession();
        for (T t : ts) {
            session.saveOrUpdate(t);
        }
    }

    @Transactional
    public void delete(T t) {

        Session session = sessionFactory.getCurrentSession();
        session.delete(t);
    }
}