package ch.smartcity.database.controllers;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Hibernate {

    private final String hibernateConfigurationXmlFile =
            "ch/smartcity/database/resources/hibernate/hibernate.cfg.xml";
    private final ConfigurationManager configurationManager;
    private final Logger logger;
    private final SessionFactory sessionFactory;
    private Session session;

    private Hibernate() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());

        try {
            sessionFactory = new Configuration()
                    .configure(hibernateConfigurationXmlFile)
                    .buildSessionFactory();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Hibernate getInstance() {
        return SingletonHolder.instance;
    }

    private static ConfigurationManager getConfigurationManager() {
        return getInstance().configurationManager;
    }

    private static Logger getLogger() {
        return getInstance().logger;
    }

    public static SessionFactory getSessionFactory() {
        return getInstance().sessionFactory;
    }

    public static Session getSession() {
        return getInstance().session;
    }

    public static void setSession(Session session) {
        getInstance().session = session;
    }

    public static Session openSession() {
        if (getSession() == null || !getSession().isOpen()) {
            setSession(getSessionFactory().openSession());
        }

        return getSession();
    }

    public static CriteriaBuilder getCriteriaBuilder() {
        return getSessionFactory().getCriteriaBuilder();
    }

    public static <T> TypedQuery<T> createQuery(CriteriaQuery<T> tCriteriaQuery) {
        return openSession().createQuery(tCriteriaQuery);
    }

    public static void closeSession() throws HibernateException {
        if (getSession() != null && getSession().isOpen()) {
            try {
                getSession().close();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, e.getMessage(), e);
                throw e;
            }
        }
    }

    public static void closeSessionFactory() throws HibernateException {
        if (getSessionFactory() != null && getSessionFactory().isOpen()) {
            try {
                getSessionFactory().close();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, e.getMessage(), e);
                throw e;
            }
        }
    }

    public static void close() throws HibernateException {
        closeSession();
        closeSessionFactory();
    }

    private static class SingletonHolder {
        private final static Hibernate instance = new Hibernate();
    }
}
