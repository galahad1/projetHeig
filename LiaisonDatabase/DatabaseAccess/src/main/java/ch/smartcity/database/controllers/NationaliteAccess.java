package database.controllers;

import database.models.Nationalite;
import database.models.Nationalite_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class NationaliteAccess {

    private final static Logger LOGGER;

    static {
        try {
            LogManager.getLogManager().readConfiguration(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(DatabaseManager.LOGGING_PROPERTIES_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER = Logger.getLogger(NationaliteAccess.class.getName());
    }

    private final DatabaseManager databaseManager;

    NationaliteAccess(DatabaseManager databaseManager) {
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

    public List<Nationalite> get(String nomNationalite) {
        List<Nationalite> nationaliteList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Nationalite> criteriaQuery = criteriaBuilder
                    .createQuery(Nationalite.class);

            Root<Nationalite> nationaliteRoot = criteriaQuery.from(Nationalite.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomNationalite != null) {
                predicateList.add(criteriaBuilder.equal(
                        nationaliteRoot.get(Nationalite_.nomNationalite),
                        nomNationalite.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            nationaliteList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, nationaliteList != null ?
                nationaliteList.size() + " " +
                        databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return nationaliteList;
    }

    public void save(String nomNationalite) {
        DatabaseAccess.save(new Nationalite(nomNationalite));
    }

    public void update(Integer idNationalite, String nomNationalite) {
        Nationalite nationalite = DatabaseAccess.get(Nationalite.class, idNationalite);

        if (nationalite != null) {
            if (nomNationalite != null) {
                nationalite.setNomNationalite(nomNationalite);
            }

            DatabaseAccess.update(nationalite);
        }
    }

    public void update(String oldNomNationalite, String newNomNationalite) {
        List<Nationalite> nationaliteList = get(oldNomNationalite);

        if (nationaliteList != null) {
            for (Nationalite nationalite : nationaliteList) {
                if (newNomNationalite != null) {
                    nationalite.setNomNationalite(newNomNationalite);
                }
            }

            DatabaseAccess.update(nationaliteList);
        }
    }

    public void delete(String nomNationalite) {
        DatabaseAccess.delete(get(nomNationalite));
    }
}
