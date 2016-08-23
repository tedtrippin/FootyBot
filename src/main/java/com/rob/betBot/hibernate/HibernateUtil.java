package com.rob.betBot.hibernate;


public class HibernateUtil {
/*
    private static SessionFactory sessionFactory;
    private static final Logger log = LogManager.getLogger(HibernateUtil.class);

    static {
        try {
            Configuration configuration = new Configuration().configure();
            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception ex) {
            log.error("Initial SessionFactory creation failed", ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public static Transaction beginTransaction() {
        return getSession().beginTransaction();
    }

    public static void commitTransaction() {
        getSession().getTransaction().commit();
    }

    public static void rollbackTransaction() {
        getSession().getTransaction().rollback();
    }

    public static void closeSession() {
        getSession().close();
    }*/
}
