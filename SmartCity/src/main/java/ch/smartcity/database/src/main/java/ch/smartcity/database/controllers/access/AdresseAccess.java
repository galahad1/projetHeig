package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AdresseAccess {

    private static String nomRue;
    private static String numeroNpa;

    private final ConfigurationManager configurationManager;
    private final Logger logger;
    private final Hibernate hibernate;

    private AdresseAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
    }

    public static AdresseAccess getInstance() {
        return SingletonHolder.instance;
    }

    private static ConfigurationManager getConfigurationManager() {
        return getInstance().configurationManager;
    }

    private static Logger getLogger() {
        return getInstance().logger;
    }

    private static Hibernate getHibernate() {
        return getInstance().hibernate;
    }

    public static List<Adresse> get(Rue rue, String numeroDeRue, Npa npa) {
        checkNull(rue, npa);
        return get(nomRue, numeroDeRue, numeroNpa);
    }

    public static List<Adresse> get(String nomRue, String numeroDeRue, String numeroNpa) {
        List<Adresse> adresseList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = getHibernate().getCriteriaBuilder();
            CriteriaQuery<Adresse> criteriaQuery = criteriaBuilder.createQuery(Adresse.class);

            Root<Adresse> adresseRoot = criteriaQuery.from(Adresse.class);
            Join<Adresse, Rue> adresseRueJoin = adresseRoot.join(Adresse_.rue);
            Join<Adresse, Npa> adresseNpaJoin = adresseRoot.join(Adresse_.npa);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomRue != null && !nomRue.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        adresseRueJoin.get(Rue_.nomRue),
                        nomRue.toLowerCase()));
            }

            if (numeroDeRue != null && !numeroDeRue.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        adresseRoot.get(Adresse_.numeroDeRue),
                        numeroDeRue.toLowerCase()));
            }

            if (numeroNpa != null && !numeroNpa.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        adresseNpaJoin.get(Npa_.numeroNpa),
                        numeroNpa.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            adresseList = getHibernate().createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        getLogger().info(String.format(
                getConfigurationManager().getString("databaseAccess.results"),
                adresseList != null ? adresseList.size() : 0,
                Adresse.class.getSimpleName()));

        return adresseList;
    }

    public static void save(Rue rue, String numeroDeRue, Npa npa) {
        DatabaseAccess.save(new Adresse(rue, numeroDeRue, npa));
    }

    public static void update(Integer idAdresse, Rue rue, String numeroDeRue, Npa npa) {
        Adresse adresse = DatabaseAccess.get(Adresse.class, idAdresse);

        if (adresse != null) {
            setAll(adresse, rue, numeroDeRue, npa);
            DatabaseAccess.update(adresse);
        }
    }

    public static void update(Rue oldRue,
                              String oldNumeroDeRue,
                              Npa oldNpa,
                              Rue newRue,
                              String newNumeroDeRue,
                              Npa newNpa) {
        List<Adresse> adresseList = get(oldRue, oldNumeroDeRue, oldNpa);

        if (adresseList != null) {
            for (Adresse adresse : adresseList) {
                setAll(adresse, newRue, newNumeroDeRue, newNpa);
            }

            DatabaseAccess.update(adresseList);
        }
    }

    public static void delete(Rue rue, String numeroDeRue, Npa npa) {
        checkNull(rue, npa);
        delete(nomRue, numeroDeRue, numeroNpa);
    }

    public static void delete(String nomRue, String numeroDeRue, String numeroNpa) {
        DatabaseAccess.delete(get(nomRue, numeroDeRue, numeroNpa));
    }

    private static void setAll(Adresse adresse, Rue rue, String numeroDeRue, Npa npa) {
        if (rue != null) {
            adresse.setRue(rue);
        }

        if (numeroDeRue != null) {
            adresse.setNumeroDeRue(numeroDeRue);
        }

        if (npa != null) {
            adresse.setNpa(npa);
        }
    }

    private static void checkNull(Rue rue, Npa npa) {
        nomRue = rue != null ? rue.getNomRue() : null;
        numeroNpa = npa != null ? npa.getNumeroNpa() : null;
    }

    private static class SingletonHolder {
        private static final AdresseAccess instance = new AdresseAccess();
    }
}
