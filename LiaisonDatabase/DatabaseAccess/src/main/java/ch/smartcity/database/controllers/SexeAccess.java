package database.controllers;

import database.models.Sexe;
import database.models.Sexe_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SexeAccess {

    private final static Logger LOGGER;

    static {
        try {
            LogManager.getLogManager().readConfiguration(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(DatabaseManager.LOGGING_PROPERTIES_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER = Logger.getLogger(SexeAccess.class.getName());
    }

    private final DatabaseManager databaseManager;

    public SexeAccess(DatabaseManager databaseManager) {
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

    public List<Sexe> get(String nomSexe) {
        List<Sexe> sexeList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Sexe> criteriaQuery = criteriaBuilder
                    .createQuery(Sexe.class);
            Root<Sexe> sexeRoot = criteriaQuery.from(Sexe.class);

            if (nomSexe != null) {
                criteriaQuery.where(criteriaBuilder.equal(sexeRoot.get(
                        Sexe_.nomSexe),
                        nomSexe.toLowerCase()));
            }

            sexeList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return sexeList;
    }

    public void save(String nomSexe) {
        DatabaseAccess.save(new Sexe(nomSexe));
    }

    public void update(int idSexe, String nomSexe) {
        Sexe sexe = DatabaseAccess.get(Sexe.class, idSexe);
        sexe.setNomSexe(nomSexe);
        DatabaseAccess.update(sexe);
    }

    public void update(String oldNomSexe, String newNomSexe) {
        List<Sexe> sexeList = get(oldNomSexe);

        for (Sexe npa : sexeList) {
            npa.setNomSexe(newNomSexe);
        }

        DatabaseAccess.update(sexeList);
    }

    public void delete(String nomSexe) {
        DatabaseAccess.delete(get(nomSexe));
    }
}
