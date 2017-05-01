package database.controllers;

import database.models.RubriqueParent;
import database.models.RubriqueParent_;
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

public class RubriqueParentAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(RubriqueParentAccess.class.getName());
    }

    private DatabaseManager databaseManager;

    RubriqueParentAccess(DatabaseManager databaseManager) {
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

    public List<RubriqueParent> get(String nomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<RubriqueParent> criteriaQuery = criteriaBuilder
                    .createQuery(RubriqueParent.class);
            Root<RubriqueParent> rubriqueParentRoot = criteriaQuery.from(RubriqueParent.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomRubriqueParent != null) {
                predicateList.add(criteriaBuilder.equal(rubriqueParentRoot.get(
                        RubriqueParent_.nomRubriqueParent),
                        nomRubriqueParent.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            rubriqueParentList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, rubriqueParentList != null ?
                rubriqueParentList.size() + " " +
                        databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return rubriqueParentList;
    }

    public void save(String nomRubriqueParent) {
        DatabaseAccess.save(new RubriqueParent(nomRubriqueParent));
    }

    public void update(Integer idRubriqueParent, String nomRubriqueParent) {
        RubriqueParent rubriqueParent = DatabaseAccess.get(RubriqueParent.class, idRubriqueParent);

        if (rubriqueParent != null) {
            if (nomRubriqueParent != null) {
                rubriqueParent.setNomRubriqueParent(nomRubriqueParent);
            }

            DatabaseAccess.update(rubriqueParent);
        }
    }

    public void update(String oldNomRubriqueParent, String newNomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = get(oldNomRubriqueParent);

        if (rubriqueParentList != null) {
            for (RubriqueParent rubriqueParent : rubriqueParentList) {
                if (newNomRubriqueParent != null) {
                    rubriqueParent.setNomRubriqueParent(newNomRubriqueParent);
                }
            }

            DatabaseAccess.update(rubriqueParentList);
        }
    }

    public void delete(String nomRubriqueParent) {
        DatabaseAccess.delete(get(nomRubriqueParent));
    }
}
