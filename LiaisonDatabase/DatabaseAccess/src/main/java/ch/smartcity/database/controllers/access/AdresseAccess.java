package database.controllers.access;

import database.controllers.ConfigurationManager;
import database.controllers.DatabaseAccess;
import database.controllers.Hibernate;
import database.models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdresseAccess {

    private static final Logger LOGGER;
    private static String nomRue;
    private static String numeroNpa;

    static {
        LOGGER = Logger.getLogger(AdresseAccess.class.getName());
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
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
            CriteriaQuery<Adresse> criteriaQuery = criteriaBuilder.createQuery(Adresse.class);

            Root<Adresse> adresseRoot = criteriaQuery.from(Adresse.class);
            Join<Adresse, Rue> adresseRueJoin = adresseRoot.join(Adresse_.rue);
            Join<Adresse, Npa> adresseNpaJoin = adresseRoot.join(Adresse_.npa);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomRue != null) {
                predicateList.add(criteriaBuilder.equal(
                        adresseRueJoin.get(Rue_.nomRue),
                        nomRue.toLowerCase()));
            }

            if (numeroDeRue != null) {
                predicateList.add(criteriaBuilder.equal(
                        adresseRoot.get(Adresse_.numeroDeRue),
                        numeroDeRue.toLowerCase()));
            }

            if (numeroNpa != null) {
                predicateList.add(criteriaBuilder.equal(
                        adresseNpaJoin.get(Npa_.numeroNpa),
                        numeroNpa.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            adresseList = Hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        LOGGER.log(Level.INFO, String.format(
                ConfigurationManager.getString("databaseAccess.results"),
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
}
