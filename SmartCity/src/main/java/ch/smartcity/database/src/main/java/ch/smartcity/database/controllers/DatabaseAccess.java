package ch.smartcity.database.controllers;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseAccess {

    private final ConfigurationManager configurationManager;
    private final Logger logger;
    private final Hibernate hibernate;

    private DatabaseAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
    }

    public static DatabaseAccess getInstance() {
        return SingletonHolder.instance;
    }

    private static ConfigurationManager getConfigurationManager() {
        return getInstance().configurationManager;
    }

    private static Logger getLogger() {
        return getInstance().logger;
    }

    private static Hibernate getHibernate() {
        return getInstance().hibernate;
    }

    public static <T> T get(Class<T> tClass, Integer id) {
        T t = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            t = session.get(tClass, id);

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        getLogger().info(String.format(
                getConfigurationManager().getString("databaseAccess.results"),
                t != null ? 1 : 0,
                tClass.getSimpleName()));

        return t;
    }

    public static <T> List<T> get(Class<T> tClass) {
        List<T> tList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = getHibernate().getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(tClass);
            criteriaQuery.from(tClass);
            tList = getHibernate().createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        getLogger().info(String.format(
                getConfigurationManager().getString("databaseAccess.results"),
                tList != null ? tList.size() : 0,
                tClass.getSimpleName()));

        return tList;
    }

    public static <T> void save(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            session.save(t);

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void save(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            for (T t : tList) {
                session.save(t);
            }

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void update(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            session.update(t);

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void update(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            for (T t : tList) {
                session.update(t);
            }

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void delete(Class<T> tClass, int id) {
        delete(get(tClass, id));
    }

    public static <T> void delete(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            session.delete(t);

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void delete(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            for (T t : tList) {
                session.delete(t);
            }

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static void rollback(Exception e, Transaction transaction) {
        if (transaction != null) {
            transaction.rollback();
            getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void close(Session session) throws HibernateException {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, e.getMessage(), e);
                throw new HibernateException(e);
            }
        }
    }

    public static void close() throws HibernateException {
        getHibernate().close();
    }

    private static void transactionMessage(Transaction transaction) {
        String key;

        if (transaction != null && transaction.getStatus().equals(TransactionStatus.COMMITTED)) {
            key = "databaseAccess.transactionCommitted";
        } else {
            key = "databaseAccess.transactionRollbacked";
        }

        getLogger().info(getConfigurationManager().getString(key));
    }

    private static class SingletonHolder {
        private final static DatabaseAccess instance = new DatabaseAccess();
    }
}
