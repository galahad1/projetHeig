package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.Rue;
import ch.smartcity.database.models.Rue_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RueAccess {

    private final ConfigurationManager configurationManager;
    private final Logger logger;
    private final Hibernate hibernate;

    private RueAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
    }

    public static RueAccess getInstance() {
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

    public static List<Rue> get(String nomRue) {
        List<Rue> rueList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = getHibernate().getCriteriaBuilder();
            CriteriaQuery<Rue> criteriaQuery = criteriaBuilder
                    .createQuery(Rue.class);
            Root<Rue> rueRoot = criteriaQuery.from(Rue.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomRue != null && !nomRue.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(rueRoot.get(
                        Rue_.nomRue),
                        nomRue.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            rueList = getHibernate().createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        getLogger().info(String.format(
                getConfigurationManager().getString("databaseAccess.results"),
                rueList != null ? rueList.size() : 0,
                Rue.class.getSimpleName()));

        return rueList;
    }

    public static void save(String nomRue) {
        DatabaseAccess.save(new Rue(nomRue));
    }

    public static void update(Integer idRue, String nomRue) {
        Rue rue = DatabaseAccess.get(Rue.class, idRue);

        if (rue != null) {
            setAll(rue, nomRue);
            DatabaseAccess.update(rue);
        }
    }

    public static void update(String oldNomRue, String newNomRue) {
        List<Rue> rueList = get(oldNomRue);

        if (rueList != null) {
            for (Rue rue : rueList) {
                setAll(rue, newNomRue);
            }

            DatabaseAccess.update(rueList);
        }
    }

    public static void delete(String nomRue) {
        DatabaseAccess.delete(get(nomRue));
    }

    private static void setAll(Rue rue, String nomRue) {
        if (nomRue != null) {
            rue.setNomRue(nomRue);
        }
    }

    private static class SingletonHolder {
        private final static RueAccess instance = new RueAccess();
    }
}
