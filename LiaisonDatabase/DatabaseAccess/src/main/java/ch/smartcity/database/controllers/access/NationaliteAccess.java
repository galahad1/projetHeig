package database.controllers.access;

import database.controllers.ConfigurationManager;
import database.controllers.DatabaseAccess;
import database.controllers.Hibernate;
import database.models.Nationalite;
import database.models.Nationalite_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class NationaliteAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(NationaliteAccess.class.getName());
    }

    public static List<Nationalite> get(String nomNationalite) {
        List<Nationalite> nationaliteList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
            CriteriaQuery<Nationalite> criteriaQuery = criteriaBuilder
                    .createQuery(Nationalite.class);

            Root<Nationalite> nationaliteRoot = criteriaQuery.from(Nationalite.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomNationalite != null) {
                predicateList.add(criteriaBuilder.equal(
                        nationaliteRoot.get(Nationalite_.nomNationalite),
                        nomNationalite.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            nationaliteList = Hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        LOGGER.info(String.format(
                ConfigurationManager.getString("databaseAccess.results"),
                nationaliteList != null ? nationaliteList.size() : 0,
                Nationalite.class.getSimpleName()));

        return nationaliteList;
    }

    public static void save(String nomNationalite) {
        DatabaseAccess.save(new Nationalite(nomNationalite));
    }

    public static void update(Integer idNationalite, String nomNationalite) {
        Nationalite nationalite = DatabaseAccess.get(Nationalite.class, idNationalite);

        if (nationalite != null) {
            setAll(nationalite, nomNationalite);
            DatabaseAccess.update(nationalite);
        }
    }

    public static void update(String oldNomNationalite, String newNomNationalite) {
        List<Nationalite> nationaliteList = get(oldNomNationalite);

        if (nationaliteList != null) {
            for (Nationalite nationalite : nationaliteList) {
                setAll(nationalite, newNomNationalite);
            }

            DatabaseAccess.update(nationaliteList);
        }
    }

    public static void delete(String nomNationalite) {
        DatabaseAccess.delete(get(nomNationalite));
    }

    private static void setAll(Nationalite nationalite, String nomNationalite) {
        if (nomNationalite != null) {
            nationalite.setNomNationalite(nomNationalite);
        }
    }
}
