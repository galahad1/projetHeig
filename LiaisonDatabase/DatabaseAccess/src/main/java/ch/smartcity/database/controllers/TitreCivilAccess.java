package database.controllers;

import database.models.TitreCivil;
import database.models.TitreCivil_;
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

public class TitreCivilAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(TitreCivilAccess.class.getName());
    }

    private DatabaseManager databaseManager;

    TitreCivilAccess(DatabaseManager databaseManager) {
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

    public List<TitreCivil> get(String titre, String abreviation) {
        List<TitreCivil> titreCivilList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<TitreCivil> criteriaQuery = criteriaBuilder
                    .createQuery(TitreCivil.class);
            Root<TitreCivil> titreCivilRoot = criteriaQuery.from(TitreCivil.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (titre != null) {
                predicateList.add(criteriaBuilder.equal(titreCivilRoot.get(
                        TitreCivil_.titre),
                        titre.toLowerCase()));
            }

            if (abreviation != null) {
                predicateList.add(criteriaBuilder.equal(titreCivilRoot.get(
                        TitreCivil_.abreviation),
                        abreviation.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            titreCivilList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, titreCivilList != null ?
                titreCivilList.size() + " " +
                        databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return titreCivilList;
    }

    public void save(String titre, String abreviation) {
        DatabaseAccess.save(new TitreCivil(titre, abreviation));
    }

    public void update(Integer idTitreCivil, String titre, String abreviation) {
        TitreCivil titreCivil = DatabaseAccess.get(TitreCivil.class, idTitreCivil);

        if (titreCivil != null) {
            if (titre != null) {
                titreCivil.setTitre(titre);
            }

            if (abreviation != null) {
                titreCivil.setAbreviation(abreviation);
            }

            DatabaseAccess.update(titreCivil);
        }
    }

    public void update(String oldTitre,
                       String oldAbreviation,
                       String newTitre,
                       String newAbreviation) {
        List<TitreCivil> titreCivilList = get(oldTitre, oldAbreviation);

        if (titreCivilList != null) {
            for (TitreCivil titreCivil : titreCivilList) {
                if (newTitre != null) {
                    titreCivil.setTitre(newTitre);
                }

                if (newAbreviation != null) {
                    titreCivil.setAbreviation(newAbreviation);
                }
            }

            DatabaseAccess.update(titreCivilList);
        }
    }

    public void delete(String titre, String abreviation) {
        DatabaseAccess.delete(get(titre, abreviation));
    }
}
