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

    private final ConfigurationManager configurationManager;
    private final Logger logger;
    private final Hibernate hibernate;

    private NpaAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
    }

    public static NpaAccess getInstance() {
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

    public static List<Npa> get(String numeroNpa) {
        List<Npa> npaList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = getHibernate().getCriteriaBuilder();
            CriteriaQuery<Npa> criteriaQuery = criteriaBuilder.createQuery(Npa.class);
            Root<Npa> npaRoot = criteriaQuery.from(Npa.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (numeroNpa != null && !numeroNpa.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        npaRoot.get(Npa_.numeroNpa), numeroNpa.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            npaList = getHibernate().createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        getLogger().info(String.format(
                getConfigurationManager().getString("databaseAccess.results"),
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

    private static class SingletonHolder {
        private static final NpaAccess instance = new NpaAccess();
    }
}
