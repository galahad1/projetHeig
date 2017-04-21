package database.controllers;

import database.models.RubriqueParent;
import database.models.RubriqueParent_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RubriqueParentAccess {

    private final static Logger LOGGER;

    static {
        try {
            LogManager.getLogManager().readConfiguration(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(DatabaseManager.LOGGING_PROPERTIES_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER = Logger.getLogger(RubriqueParentAccess.class.getName());
    }

    private final DatabaseManager databaseManager;

    public RubriqueParentAccess(DatabaseManager databaseManager) {
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

    public List<RubriqueParent> get(String nomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<RubriqueParent> criteriaQuery = criteriaBuilder
                    .createQuery(RubriqueParent.class);
            Root<RubriqueParent> rubriqueParentRoot = criteriaQuery.from(RubriqueParent.class);

            if (nomRubriqueParent != null) {
                criteriaQuery.where(criteriaBuilder.equal(rubriqueParentRoot.get(
                        RubriqueParent_.nomRubriqueParent),
                        nomRubriqueParent.toLowerCase()));
            }

            rubriqueParentList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return rubriqueParentList;
    }

    public void save(String nomRubriqueParent) {
        DatabaseAccess.save(new RubriqueParent(nomRubriqueParent));
    }

    public void update(int idRubriqueParent, String nomRubriqueParent) {
        RubriqueParent rubriqueParent = DatabaseAccess.get(RubriqueParent.class, idRubriqueParent);
        rubriqueParent.setNomRubriqueParent(nomRubriqueParent);
        DatabaseAccess.update(rubriqueParent);
    }

    public void update(String oldNomRubriqueParent, String newNomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = get(oldNomRubriqueParent);

        for (RubriqueParent rubriqueParent : rubriqueParentList) {
            rubriqueParent.setNomRubriqueParent(newNomRubriqueParent);
        }

        DatabaseAccess.update(rubriqueParentList);
    }

    public void delete(String nomRubriqueParent) {
        DatabaseAccess.delete(get(nomRubriqueParent));
    }
}
