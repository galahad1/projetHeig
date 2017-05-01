package database.controllers;

import database.models.Sexe;
import database.models.Sexe_;
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

public class SexeAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(SexeAccess.class.getName());
    }

    private DatabaseManager databaseManager;

    SexeAccess(DatabaseManager databaseManager) {
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

    public List<Sexe> get(String nomSexe) {
        List<Sexe> sexeList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Sexe> criteriaQuery = criteriaBuilder
                    .createQuery(Sexe.class);
            Root<Sexe> sexeRoot = criteriaQuery.from(Sexe.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomSexe != null) {
                predicateList.add(criteriaBuilder.equal(sexeRoot.get(
                        Sexe_.nomSexe),
                        nomSexe.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            sexeList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, sexeList != null ?
                sexeList.size() + " " + databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return sexeList;
    }

    public void save(String nomSexe) {
        DatabaseAccess.save(new Sexe(nomSexe));
    }

    public void update(Integer idSexe, String nomSexe) {
        Sexe sexe = DatabaseAccess.get(Sexe.class, idSexe);

        if (sexe != null) {
            if (nomSexe != null) {
                sexe.setNomSexe(nomSexe);
            }

            DatabaseAccess.update(sexe);
        }
    }

    public void update(String oldNomSexe, String newNomSexe) {
        List<Sexe> sexeList = get(oldNomSexe);

        if (sexeList != null) {
            for (Sexe sexe : sexeList) {
                if (newNomSexe != null) {
                    sexe.setNomSexe(newNomSexe);
                }
            }

            DatabaseAccess.update(sexeList);
        }
    }

    public void delete(String nomSexe) {
        DatabaseAccess.delete(get(nomSexe));
    }
}
