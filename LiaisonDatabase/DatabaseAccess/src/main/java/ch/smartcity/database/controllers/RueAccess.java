package database.controllers;

import database.models.Rue;
import database.models.Rue_;
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

public class RueAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(RueAccess.class.getName());
    }

    private DatabaseManager databaseManager;

    RueAccess(DatabaseManager databaseManager) {
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

    public List<Rue> get(String nomRue) {
        List<Rue> rueList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Rue> criteriaQuery = criteriaBuilder
                    .createQuery(Rue.class);
            Root<Rue> rueRoot = criteriaQuery.from(Rue.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomRue != null) {
                predicateList.add(criteriaBuilder.equal(rueRoot.get(
                        Rue_.nomRue),
                        nomRue.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            rueList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
            databaseManager.close();
        }

        LOGGER.log(Level.INFO, rueList != null ?
                rueList.size() + " " + databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return rueList;
    }

    public void save(String nomRue) {
        DatabaseAccess.save(new Rue(nomRue));
    }

    public void update(Integer idRue, String nomRue) {
        Rue rue = DatabaseAccess.get(Rue.class, idRue);

        if (rue != null) {
            if (nomRue != null) {
                rue.setNomRue(nomRue);
            }

            DatabaseAccess.update(rue);
        }
    }

    public void update(String oldNomRue, String newNomRue) {
        List<Rue> rueList = get(oldNomRue);

        if (rueList != null) {
            for (Rue rue : rueList) {
                if (newNomRue != null) {
                    rue.setNomRue(newNomRue);
                }
            }

            DatabaseAccess.update(rueList);
        }
    }

    public void delete(String nomRue) {
        DatabaseAccess.delete(get(nomRue));
    }
}
