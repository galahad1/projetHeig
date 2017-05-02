package database.controllers;

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

    private static final Logger LOGGER;

    static {
        try {
            ConfigurationManager.initialize();
            LOGGER = Logger.getLogger(DatabaseAccess.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static <T> T get(Class<T> tClass, Integer id) {
        T t = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            t = session.get(tClass, id);

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, String.format(
                ConfigurationManager.getString("databaseAccess.results"),
                t != null ? 1 : 0,
                tClass.getSimpleName()));

        return t;
    }

    public static <T> List<T> get(Class<T> tClass) {
        List<T> tList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(tClass);
            criteriaQuery.from(tClass);
            tList = Hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, String.format(
                ConfigurationManager.getString("databaseAccess.results"),
                tList != null ? tList.size() : 0,
                tClass.getSimpleName()));

        return tList;
    }

    public static <T> void save(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
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
            session = Hibernate.getSession();
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
            session = Hibernate.getSession();
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
            session = Hibernate.getSession();
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
            session = Hibernate.getSession();
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
            session = Hibernate.getSession();
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
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void close(Session session) throws HibernateException {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new HibernateException(e);
            }
        }
    }

    public static void close() {
        Hibernate.close();
    }

    private static void transactionMessage(Transaction transaction) {
        String key;

        if (transaction != null && transaction.getStatus().equals(TransactionStatus.COMMITTED)) {
            key = "databaseAccess.transactionCommitted";
        } else {
            key = "databaseAccess.transactionRollbacked";
        }

        LOGGER.info(ConfigurationManager.getString(key));
    }
}
