package database.controllers;

import database.models.TitreCivil;
import database.models.TitreCivil_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TitreCivilAccess {

    private final static Logger LOGGER;

    static {
        try {
            LogManager.getLogManager().readConfiguration(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(DatabaseManager.LOGGING_PROPERTIES_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER = Logger.getLogger(TitreCivilAccess.class.getName());
    }

    private final DatabaseManager databaseManager;

    public TitreCivilAccess(DatabaseManager databaseManager) {
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

    public List<TitreCivil> get(String titre, String abreviation) {
        List<TitreCivil> titreCivilList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<TitreCivil> criteriaQuery = criteriaBuilder
                    .createQuery(TitreCivil.class);
            Root<TitreCivil> titreCivilRoot = criteriaQuery.from(TitreCivil.class);

            if (titre != null) {
                criteriaQuery.where(criteriaBuilder.equal(titreCivilRoot.get(
                        TitreCivil_.titre),
                        titre.toLowerCase()));
            }

            if (abreviation != null) {
                criteriaQuery.where(criteriaBuilder.equal(titreCivilRoot.get(
                        TitreCivil_.abreviation),
                        abreviation.toLowerCase()));
            }

            titreCivilList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return titreCivilList;
    }

    public void save(String titre, String abreviation) {
        DatabaseAccess.save(new TitreCivil(titre, abreviation));
    }

    public void update(int idTitreCivil, String titre, String abreviation) {
        TitreCivil titreCivil = DatabaseAccess.get(TitreCivil.class, idTitreCivil);
        titreCivil.setTitre(titre);
        titreCivil.setAbreviation(abreviation);
        DatabaseAccess.update(titreCivil);
    }

    public void update(String oldTitre,
                       String oldAbreviation,
                       String newTitre,
                       String newAbreviation) {
        List<TitreCivil> titreCivilList = get(oldTitre, oldAbreviation);

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

    public void delete(String titre, String abreviation) {
        DatabaseAccess.delete(get(titre, abreviation));
    }
}
