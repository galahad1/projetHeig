package database.controllers;

import database.models.Npa;
import database.models.Npa_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class NpaAccess {

    private final static Logger LOGGER;

    static {
        try {
            LogManager.getLogManager().readConfiguration(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(DatabaseManager.LOGGING_PROPERTIES_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER = Logger.getLogger(NpaAccess.class.getName());
    }

    private final DatabaseManager databaseManager;

    public NpaAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    private static void rollback(Exception ex, Transaction transaction) {
        if (transaction != null) {
            try {
                transaction.rollback();
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            } catch (Exception _ex) {
                LOGGER.log(Level.SEVERE, _ex.getMessage(), _ex);
            }
        }
    }

    private static void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public List<Npa> get(String numeroNpa) {
        List<Npa> npaList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Npa> criteriaQuery = criteriaBuilder.createQuery(Npa.class);
            Root<Npa> npaRoot = criteriaQuery.from(Npa.class);

            if (numeroNpa != null) {
                criteriaQuery.where(criteriaBuilder.equal(
                        npaRoot.get(Npa_.numeroNpa), numeroNpa.toLowerCase()));
            }

            npaList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return npaList;
    }

    public void save(String numeroNpa) {
        DatabaseAccess.save(new Npa(numeroNpa));
    }

    public void update(int idNpa, String numeroNpa) {
        Npa npa = DatabaseAccess.get(Npa.class, idNpa);
        npa.setNumeroNpa(numeroNpa);
        DatabaseAccess.update(npa);
    }

    public void update(String oldNumeroNpa, String newNumeroNpa) {
        List<Npa> npaList = get(oldNumeroNpa);

        for (Npa npa : npaList) {
            npa.setNumeroNpa(newNumeroNpa);
        }

        DatabaseAccess.update(npaList);
    }

    public void delete(String numeroNpa) {
        DatabaseAccess.delete(get(numeroNpa));
    }
}
