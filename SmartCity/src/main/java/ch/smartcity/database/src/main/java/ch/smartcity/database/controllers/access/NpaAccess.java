package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.Npa;
import ch.smartcity.database.models.Npa_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class NpaAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(NpaAccess.class.getName());
    }

    public static List<Npa> get(String numeroNpa) {
        List<Npa> npaList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
            CriteriaQuery<Npa> criteriaQuery = criteriaBuilder.createQuery(Npa.class);
            Root<Npa> npaRoot = criteriaQuery.from(Npa.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (numeroNpa != null) {
                predicateList.add(criteriaBuilder.equal(
                        npaRoot.get(Npa_.numeroNpa), numeroNpa.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            npaList = Hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        LOGGER.info(String.format(
                ConfigurationManager.getString("databaseAccess.results"),
                npaList != null ? npaList.size() : 0,
                Npa.class.getSimpleName()));

        return npaList;
    }

    public static void save(String numeroNpa) {
        DatabaseAccess.save(new Npa(numeroNpa));
    }

    public static void update(Integer idNpa, String numeroNpa) {
        Npa npa = DatabaseAccess.get(Npa.class, idNpa);

        if (npa != null) {
            setAll(npa, numeroNpa);
            DatabaseAccess.update(npa);
        }
    }

    public static void update(String oldNumeroNpa, String newNumeroNpa) {
        List<Npa> npaList = get(oldNumeroNpa);

        if (npaList != null) {
            for (Npa npa : npaList) {
                setAll(npa, newNumeroNpa);
            }

            DatabaseAccess.update(npaList);
        }
    }

    public static void delete(String numeroNpa) {
        DatabaseAccess.delete(get(numeroNpa));
    }

    private static void setAll(Npa npa, String numeroNpa) {
        if (numeroNpa != null) {
            npa.setNumeroNpa(numeroNpa);
        }
    }
}
