package database.controllers;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseAccess {

    public static final AdresseAccess ADRESSE_ACCESS;
    public static final CommentaireAccess COMMENTAIRE_ACCESS;
    public static final ConfianceAccess CONFIANCE_ACCESS;
    public static final EvenementAccess EVENEMENT_ACCESS;
    public static final NationaliteAccess NATIONALITE_ACCESS;
    public static final NpaAccess NPA_ACCESS;
    public static final PrioriteAccess PRIORITE_ACCESS;
    public static final RubriqueEnfantAccess RUBRIQUE_ENFANT_ACCESS;
    public static final RubriqueParentAccess RUBRIQUE_PARENT_ACCESS;
    public static final RueAccess RUE_ACCESS;
    public static final SexeAccess SEXE_ACCESS;
    public static final StatutAccess STATUT_ACCESS;
    public static final TitreCivilAccess TITRE_CIVIL_ACCESS;
    public static final UtilisateurAccess UTILISATEUR_ACCESS;
    private static final Logger LOGGER;
    private static final DatabaseManager DATABASE_MANAGER;

    static {
        LOGGER = Logger.getLogger(DatabaseAccess.class.getName());
        DATABASE_MANAGER = new DatabaseManager();

        ADRESSE_ACCESS = new AdresseAccess(DATABASE_MANAGER);
        COMMENTAIRE_ACCESS = new CommentaireAccess(DATABASE_MANAGER);
        CONFIANCE_ACCESS = new ConfianceAccess(DATABASE_MANAGER);
        EVENEMENT_ACCESS = new EvenementAccess(DATABASE_MANAGER);
        NATIONALITE_ACCESS = new NationaliteAccess(DATABASE_MANAGER);
        NPA_ACCESS = new NpaAccess(DATABASE_MANAGER);
        PRIORITE_ACCESS = new PrioriteAccess(DATABASE_MANAGER);
        RUBRIQUE_ENFANT_ACCESS = new RubriqueEnfantAccess(DATABASE_MANAGER);
        RUBRIQUE_PARENT_ACCESS = new RubriqueParentAccess(DATABASE_MANAGER);
        SEXE_ACCESS = new SexeAccess(DATABASE_MANAGER);
        RUE_ACCESS = new RueAccess(DATABASE_MANAGER);
        STATUT_ACCESS = new StatutAccess(DATABASE_MANAGER);
        TITRE_CIVIL_ACCESS = new TitreCivilAccess(DATABASE_MANAGER);
        UTILISATEUR_ACCESS = new UtilisateurAccess(DATABASE_MANAGER);
    }

    public static <T> T get(Class<T> tClass, Integer id) {
        T t = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.getSession();
            transaction = session.beginTransaction();

            t = session.get(tClass, id);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, t != null ?
                DATABASE_MANAGER.getString("databaseAccess.oneResult")
                : DATABASE_MANAGER.getString("databaseAccess.noResults"));

        return t;
    }

    public static <T> List<T> get(Class<T> tClass) {
        List<T> tList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = DATABASE_MANAGER.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(tClass);
            criteriaQuery.from(tClass);
            tList = DATABASE_MANAGER.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, tList != null ?
                tList.size() + " " + DATABASE_MANAGER.getString("databaseAccess.results")
                : DATABASE_MANAGER.getString("databaseAccess.noResults"));

        return tList;
    }

    public static <T> void save(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.getSession();
            transaction = session.beginTransaction();

            session.save(t);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void save(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.getSession();
            transaction = session.beginTransaction();

            for (T t : tList) {
                session.save(t);
            }

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void update(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.getSession();
            transaction = session.beginTransaction();

            session.update(t);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void update(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.getSession();
            transaction = session.beginTransaction();

            for (T t : tList) {
                session.update(t);
            }

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void delete(Class<T> tClass, int id) {
        delete(get(tClass, id));
    }

    public static <T> void delete(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.getSession();
            transaction = session.beginTransaction();

            session.delete(t);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void delete(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.getSession();
            transaction = session.beginTransaction();

            for (T t : tList) {
                session.delete(t);
            }

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static void close() {
        DATABASE_MANAGER.close();
    }

    private static void transactionMessage(Transaction transaction) {

        String key;

        if (transaction != null && transaction.getStatus().equals(TransactionStatus.COMMITTED)) {
            key = "databaseAccess.transactionCommitted";
        } else {
            key = "databaseAccess.transactionRollbacked";
        }

        LOGGER.log(Level.INFO, DATABASE_MANAGER.getString(key));
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
}
