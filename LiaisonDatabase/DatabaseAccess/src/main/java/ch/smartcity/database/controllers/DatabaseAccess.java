package database.controllers;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class DatabaseAccess {

    public final static AdresseAccess ADRESSE_ACCESS;
    public final static EvenementAccess EVENEMENT_ACCESS;
    public final static NationaliteAccess NATIONALITE_ACCESS;
    public final static NpaAccess NPA_ACCESS;
    public final static PrioriteAccess PRIORITE_ACCESS;
    public final static RubriqueEnfantAccess RUBRIQUE_ENFANT_ACCESS;
    public final static RubriqueParentAccess RUBRIQUE_PARENT_ACCESS;
    public final static RueAccess RUE_ACCESS;
    public final static SexeAccess SEXE_ACCESS;
    public final static StatutAccess STATUT_ACCESS;
    public final static TitreCivilAccess TITRE_CIVIL_ACCESS;
    public final static UtilisateurAccess UTILISATEUR_ACCESS;
    private final static Logger LOGGER;
    private final static DatabaseManager DATABASE_MANAGER;

    static {
        try {
            LogManager.getLogManager().readConfiguration(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(DatabaseManager.LOGGING_PROPERTIES_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER = Logger.getLogger(DatabaseAccess.class.getName());
        DATABASE_MANAGER = new DatabaseManager();

        ADRESSE_ACCESS = new AdresseAccess(DATABASE_MANAGER);
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

    public static <T> T get(Class<T> tClass, int id) {
        T t = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.openSession();
            transaction = session.beginTransaction();

            t = session.get(tClass, id);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return t;
    }

    public static <T> List<T> get(Class<T> tClass) {
        List<T> tList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.openSession();
            transaction = session.beginTransaction();

            tList = session.createQuery("from " + tClass.getSimpleName(), tClass).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        return tList;
    }

    public static <T> void save(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.openSession();
            transaction = session.beginTransaction();

            session.save(t);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }
    }

    public static <T> void save(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.openSession();
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
    }

    public static <T> void update(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.openSession();
            transaction = session.beginTransaction();

            session.update(t);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }
    }

    public static <T> void update(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.openSession();
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
    }

    public static <T> void delete(Class<T> tClass, int id) {
        delete(get(tClass, id));
    }

    public static <T> void delete(T t) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.openSession();
            transaction = session.beginTransaction();

            session.delete(t);

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }
    }

    public static <T> void delete(List<T> tList) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = DATABASE_MANAGER.openSession();
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
    }

    public static void close() {
        DATABASE_MANAGER.close();
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
