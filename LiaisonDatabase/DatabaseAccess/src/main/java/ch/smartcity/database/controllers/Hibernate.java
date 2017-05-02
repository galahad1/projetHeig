package database.controllers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hibernate {

    private static final Logger LOGGER;
    private static final SessionFactory sessionFactory;
    private static Session session;

    static {
        LOGGER = Logger.getLogger(ConfigurationManager.class.getName());

        try {
            sessionFactory = new Configuration()
                    .configure("database/resources/hibernate/hibernate.cfg.xml")
                    .buildSessionFactory();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static void init() {
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession();
        }

        return session;
    }

    public static void setSession(Session session) {
        Hibernate.session = session;
    }

    public static CriteriaBuilder getCriteriaBuilder() {
        return sessionFactory.getCriteriaBuilder();
    }

    public static <T> TypedQuery<T> createQuery(CriteriaQuery<T> tCriteriaQuery) {
        return getSession().createQuery(tCriteriaQuery);
    }

    public static void closeSession() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw e;
            }
        }
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            try {
                sessionFactory.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw e;
            }
        }
    }

    public static void close() {
        closeSession();
        closeSessionFactory();
    }
}
