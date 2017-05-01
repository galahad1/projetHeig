package database.controllers;

import database.models.Npa;
import database.models.Npa_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NpaAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(NpaAccess.class.getName());
    }

    private DatabaseManager databaseManager;

    NpaAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    private void rollback(Exception ex, Transaction transaction) {
        if (transaction != null) {
            try {
                transaction.rollback();
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            } catch (Exception _ex) {
                LOGGER.log(Level.SEVERE, _ex.getMessage(), _ex);
            }
        }
    }

    private void close(Session session) {
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
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Npa> criteriaQuery = criteriaBuilder.createQuery(Npa.class);
            Root<Npa> npaRoot = criteriaQuery.from(Npa.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (numeroNpa != null) {
                predicateList.add(criteriaBuilder.equal(
                        npaRoot.get(Npa_.numeroNpa), numeroNpa.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            npaList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, npaList != null ?
                npaList.size() + " " + databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return npaList;
    }

    public void save(String numeroNpa) {
        DatabaseAccess.save(new Npa(numeroNpa));
    }

    public void update(Integer idNpa, String numeroNpa) {
        Npa npa = DatabaseAccess.get(Npa.class, idNpa);

        if (npa != null) {
            if (numeroNpa != null) {
                npa.setNumeroNpa(numeroNpa);
            }

            DatabaseAccess.update(npa);
        }
    }

    public void update(String oldNumeroNpa, String newNumeroNpa) {
        List<Npa> npaList = get(oldNumeroNpa);

        if (npaList != null) {
            for (Npa npa : npaList) {
                if (newNumeroNpa != null) {
                    npa.setNumeroNpa(newNumeroNpa);
                }
            }

            DatabaseAccess.update(npaList);
        }
    }

    public void delete(String numeroNpa) {
        DatabaseAccess.delete(get(numeroNpa));
    }
}
