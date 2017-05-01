package database.controllers;

import database.models.Statut;
import database.models.Statut_;
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

public class StatutAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(StatutAccess.class.getName());
    }

    private DatabaseManager databaseManager;

    StatutAccess(DatabaseManager databaseManager) {
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

    public List<Statut> get(String nomStatut) {
        List<Statut> statutList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Statut> criteriaQuery = criteriaBuilder
                    .createQuery(Statut.class);
            Root<Statut> statutRoot = criteriaQuery.from(Statut.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomStatut != null) {
                predicateList.add(criteriaBuilder.equal(statutRoot.get(
                        Statut_.nomStatut),
                        nomStatut.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            statutList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, statutList != null ?
                statutList.size() + " " + databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return statutList;
    }

    public void save(String nomStatut) {
        DatabaseAccess.save(new Statut(nomStatut));
    }

    public void update(Integer idStatut, String nomStatut) {
        Statut statut = DatabaseAccess.get(Statut.class, idStatut);

        if (statut != null) {
            if (nomStatut != null) {
                statut.setNomStatut(nomStatut);
            }

            DatabaseAccess.update(statut);
        }
    }

    public void update(String oldNomStatut, String newNomStatut) {
        List<Statut> statutList = get(oldNomStatut);

        if (statutList != null) {
            for (Statut statut : statutList) {
                if (newNomStatut != null) {
                    statut.setNomStatut(newNomStatut);
                }
            }

            DatabaseAccess.update(statutList);
        }
    }

    public void delete(String nomStatut) {
        DatabaseAccess.delete(get(nomStatut));
    }
}
