package database.controllers;

import database.models.Nationalite;
import database.models.Nationalite_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    public NationaliteAccess(DatabaseManager databaseManager) {
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

    public List<Nationalite> get(String nomNationalite) {
        List<Nationalite> nationaliteList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Nationalite> criteriaQuery = criteriaBuilder
                    .createQuery(Nationalite.class);
            Root<Nationalite> nationaliteRoot = criteriaQuery.from(Nationalite.class);

            if (nomNationalite != null) {
                criteriaQuery.where(criteriaBuilder.equal(
                        nationaliteRoot.get(Nationalite_.nomNationalite),
                        nomNationalite.toLowerCase()));
            }

            nationaliteList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return nationaliteList;
    }

    public void save(String nomNationalite) {
        DatabaseAccess.save(new Nationalite(nomNationalite));
    }

    public void update(int idNationalite, String nomNationalite) {
        Nationalite nationalite = DatabaseAccess.get(Nationalite.class, idNationalite);
        nationalite.setNomNationalite(nomNationalite);
        DatabaseAccess.update(nationalite);
    }

    public void update(String oldNomNationalite, String newNomNationalite) {
        List<Nationalite> nationaliteList = get(oldNomNationalite);

        for (Nationalite nationalite : nationaliteList) {
            nationalite.setNomNationalite(newNomNationalite);
        }

        DatabaseAccess.update(nationaliteList);
    }

    public void delete(String nomNationalite) {
        DatabaseAccess.delete(get(nomNationalite));
    }
}
