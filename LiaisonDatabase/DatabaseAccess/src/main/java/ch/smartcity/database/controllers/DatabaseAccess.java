package database.controllers;

import org.hibernate.Session;

import java.util.List;

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
    private final static DatabaseManager DATABASE_MANAGER;

    static {
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

        try {
            Session session = DATABASE_MANAGER.openSession();

            t = session.get(tClass, id);

            DATABASE_MANAGER.commit();
        } catch (Exception ex) {
            DATABASE_MANAGER.rollback(ex);
        } finally {
            DATABASE_MANAGER.close();
        }

        return t;
    }

    public static <T> List<T> get(Class<T> tClass) {
        List<T> tList = null;

        try {
            Session session = DATABASE_MANAGER.openSession();

            tList = session.createQuery("from " + tClass.getSimpleName(), tClass).list();

            DATABASE_MANAGER.commit();
        } catch (Exception ex) {
            DATABASE_MANAGER.rollback(ex);
        } finally {
            DATABASE_MANAGER.close();
        }

        return tList;
    }

    public static <T> void save(T t) {
        try {
            Session session = DATABASE_MANAGER.openSession();

            session.save(t);

            DATABASE_MANAGER.commit();
        } catch (Exception ex) {
            DATABASE_MANAGER.rollback(ex);
        } finally {
            DATABASE_MANAGER.close();
        }
    }

    public static <T> void save(List<T> tList) {
        try {
            Session session = DATABASE_MANAGER.openSession();

            for (T t : tList) {
                session.save(t);
            }

            DATABASE_MANAGER.commit();
        } catch (Exception ex) {
            DATABASE_MANAGER.rollback(ex);
        } finally {
            DATABASE_MANAGER.close();
        }
    }

    public static <T> void update(T t) {
        try {
            Session session = DATABASE_MANAGER.openSession();

            session.update(t);

            DATABASE_MANAGER.commit();
        } catch (Exception ex) {
            DATABASE_MANAGER.rollback(ex);
        } finally {
            DATABASE_MANAGER.close();
        }
    }

    public static <T> void update(List<T> tList) {
        try {
            Session session = DATABASE_MANAGER.openSession();

            for (T t : tList) {
                session.update(t);
            }

            DATABASE_MANAGER.commit();
        } catch (Exception ex) {
            DATABASE_MANAGER.rollback(ex);
        } finally {
            DATABASE_MANAGER.close();
        }
    }

    public static <T> void delete(Class<T> tClass, int id) {
        delete(get(tClass, id));
    }

    public static <T> void delete(T t) {
        try {
            Session session = DATABASE_MANAGER.openSession();

            session.delete(t);

            DATABASE_MANAGER.commit();
        } catch (Exception ex) {
            DATABASE_MANAGER.rollback(ex);
        } finally {
            DATABASE_MANAGER.close();
        }
    }

    public static <T> void delete(List<T> tList) {
        try {
            Session session = DATABASE_MANAGER.openSession();

            for (T t : tList) {
                session.delete(t);
            }

            DATABASE_MANAGER.commit();
        } catch (Exception ex) {
            DATABASE_MANAGER.rollback(ex);
        } finally {
            DATABASE_MANAGER.close();
        }
    }

    public static void close() {
        DATABASE_MANAGER.terminate();
    }
}
