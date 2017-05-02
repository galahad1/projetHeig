package database.controllers.access;

import database.controllers.ConfigurationManager;
import database.controllers.Hibernate;
import org.hibernate.HibernateException;
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

    static {
        ConfigurationManager.init();
        LOGGER = Logger.getLogger(DatabaseAccess.class.getName());

        try {
            ADRESSE_ACCESS = new AdresseAccess();
            COMMENTAIRE_ACCESS = new CommentaireAccess();
            CONFIANCE_ACCESS = new ConfianceAccess();
            EVENEMENT_ACCESS = new EvenementAccess();
            NATIONALITE_ACCESS = new NationaliteAccess();
            NPA_ACCESS = new NpaAccess();
            PRIORITE_ACCESS = new PrioriteAccess();
            RUBRIQUE_ENFANT_ACCESS = new RubriqueEnfantAccess();
            RUBRIQUE_PARENT_ACCESS = new RubriqueParentAccess();
            SEXE_ACCESS = new SexeAccess();
            RUE_ACCESS = new RueAccess();
            STATUT_ACCESS = new StatutAccess();
            TITRE_CIVIL_ACCESS = new TitreCivilAccess();
            UTILISATEUR_ACCESS = new UtilisateurAccess();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static <T> T get(Class<T> tClass, Integer id) {
        T t = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            t = session.get(tClass, id);

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO,
                ConfigurationManager.getString("databaseAccess.results"),
                t != null ? 1 : 0);

        return t;
    }

    public static <T> List<T> get(Class<T> tClass) {
        List<T> tList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(tClass);
            criteriaQuery.from(tClass);
            tList = Hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO,
                ConfigurationManager.getString("databaseAccess.results"),
                tList != null ? tList.size() : 0);

        return tList;
    }

    public static <T> void save(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            session.save(t);

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void save(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            for (T t : tList) {
                session.save(t);
            }

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void update(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            session.update(t);

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void update(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            for (T t : tList) {
                session.update(t);
            }

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
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
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            session.delete(t);

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static <T> void delete(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            for (T t : tList) {
                session.delete(t);
            }

            transaction.commit();
        } catch (Exception e) {
            rollback(e, transaction);
        } finally {
            close(session);
        }

        transactionMessage(transaction);
    }

    public static void close() {
        Hibernate.close();
    }

    public static void rollback(Exception e, Transaction transaction) {
        if (transaction != null) {
            transaction.rollback();
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void close(Session session) throws HibernateException {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new HibernateException(e);
            }
        }
    }

    private static void transactionMessage(Transaction transaction) {
        String key = "databaseAccess.transaction";

        if (transaction != null && transaction.getStatus().equals(TransactionStatus.COMMITTED)) {
            key += "Committed";
        } else {
            key += "Rollbacked";
        }

        LOGGER.info(ConfigurationManager.getString(key));
    }
}
