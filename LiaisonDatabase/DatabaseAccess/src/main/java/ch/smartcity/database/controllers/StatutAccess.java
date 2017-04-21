package database.controllers;

import database.models.Statut;
import database.models.Statut_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class StatutAccess {

    private final static Logger LOGGER;

    static {
        try {
            LogManager.getLogManager().readConfiguration(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(DatabaseManager.LOGGING_PROPERTIES_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER = Logger.getLogger(StatutAccess.class.getName());
    }

    private final DatabaseManager databaseManager;

    public StatutAccess(DatabaseManager databaseManager) {
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

    public List<Statut> get(String nomStatut) {
        List<Statut> statutList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Statut> criteriaQuery = criteriaBuilder
                    .createQuery(Statut.class);
            Root<Statut> statutRoot = criteriaQuery.from(Statut.class);

            if (nomStatut != null) {
                criteriaQuery.where(criteriaBuilder.equal(statutRoot.get(
                        Statut_.nomStatut),
                        nomStatut.toLowerCase()));
            }

            statutList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return statutList;
    }

    public void save(String nomStatut) {
        DatabaseAccess.save(new Statut(nomStatut));
    }

    public void update(int idStatut, String nomStatut) {
        Statut statut = DatabaseAccess.get(Statut.class, idStatut);
        statut.setNomStatut(nomStatut);
        DatabaseAccess.update(statut);
    }

    public void update(String oldNomStatut, String newNomStatut) {
        List<Statut> statutList = get(oldNomStatut);

        for (Statut statut : statutList) {
            statut.setNomStatut(newNomStatut);
        }

        DatabaseAccess.update(statutList);
    }

    public void delete(String nomStatut) {
        DatabaseAccess.delete(get(nomStatut));
    }
}
