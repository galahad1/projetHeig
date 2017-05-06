package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.Statut;
import ch.smartcity.database.models.Statut_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StatutAccess {

    private final ConfigurationManager configurationManager;
    private final Logger logger;
    private final Hibernate hibernate;

    private StatutAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
    }

    public static StatutAccess getInstance() {
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

    public static List<Statut> get(String nomStatut) {
        List<Statut> statutList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = getHibernate().getCriteriaBuilder();
            CriteriaQuery<Statut> criteriaQuery = criteriaBuilder
                    .createQuery(Statut.class);
            Root<Statut> statutRoot = criteriaQuery.from(Statut.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomStatut != null && !nomStatut.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(statutRoot.get(
                        Statut_.nomStatut),
                        nomStatut.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            statutList = getHibernate().createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        getLogger().info(String.format(
                getConfigurationManager().getString("databaseAccess.results"),
                statutList != null ? statutList.size() : 0,
                Statut.class.getSimpleName()));

        return statutList;
    }

    public static void save(String nomStatut) {
        DatabaseAccess.save(new Statut(nomStatut));
    }

    public static void update(Integer idStatut, String nomStatut) {
        Statut statut = DatabaseAccess.get(Statut.class, idStatut);

        if (statut != null) {
            setAll(statut, nomStatut);
            DatabaseAccess.update(statut);
        }
    }

    public static void update(String oldNomStatut, String newNomStatut) {
        List<Statut> statutList = get(oldNomStatut);

        if (statutList != null) {
            for (Statut statut : statutList) {
                setAll(statut, newNomStatut);
            }

            DatabaseAccess.update(statutList);
        }
    }

    public static void delete(String nomStatut) {
        DatabaseAccess.delete(get(nomStatut));
    }

    private static void setAll(Statut statut, String nomStatut) {
        if (nomStatut != null) {
            statut.setNomStatut(nomStatut);
        }
    }

    private static class SingletonHolder {
        private static final StatutAccess instance = new StatutAccess();
    }
}
