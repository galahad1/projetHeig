package database.controllers;

import database.models.RubriqueEnfant;
import database.models.RubriqueEnfant_;
import database.models.RubriqueParent;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class RubriqueEnfantAccess {

    private final static Logger LOGGER;

    static {
        try {
            LogManager.getLogManager().readConfiguration(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(DatabaseManager.LOGGING_PROPERTIES_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER = Logger.getLogger(RubriqueEnfantAccess.class.getName());
    }

    private final DatabaseManager databaseManager;

    public RubriqueEnfantAccess(DatabaseManager databaseManager) {
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

    public List<RubriqueEnfant> get(String nomRubriqueEnfant) {
        List<RubriqueEnfant> rubriqueEnfantList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<RubriqueEnfant> criteriaQuery = criteriaBuilder
                    .createQuery(RubriqueEnfant.class);
            Root<RubriqueEnfant> rubriqueEnfantRoot = criteriaQuery.from(RubriqueEnfant.class);

            if (nomRubriqueEnfant != null) {
                criteriaQuery.where(criteriaBuilder.equal(rubriqueEnfantRoot.get(
                        RubriqueEnfant_.nomRubriqueEnfant),
                        nomRubriqueEnfant.toLowerCase()));
            }

            rubriqueEnfantList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return rubriqueEnfantList;
    }

    public void save(String nomRubriqueEnfant, RubriqueParent rubriqueParent) {
        DatabaseAccess.save(new RubriqueEnfant(rubriqueParent, nomRubriqueEnfant));
    }

    public void update(int idRubriqueEnfant, String nomRubriqueEnfant) {
        RubriqueEnfant rubriqueEnfant = DatabaseAccess.get(RubriqueEnfant.class, idRubriqueEnfant);
        rubriqueEnfant.setNomRubriqueEnfant(nomRubriqueEnfant);
        DatabaseAccess.update(rubriqueEnfant);
    }

    public void update(String oldNomRubriqueEnfant, String newNomRubriqueEnfant) {
        List<RubriqueEnfant> rubriqueEnfantList = get(oldNomRubriqueEnfant);

        for (RubriqueEnfant rubriqueEnfant : rubriqueEnfantList) {
            rubriqueEnfant.setNomRubriqueEnfant(newNomRubriqueEnfant);
        }

        DatabaseAccess.update(rubriqueEnfantList);
    }

    public void delete(String nomRubriqueEnfant) {
        DatabaseAccess.delete(get(nomRubriqueEnfant));
    }
}
