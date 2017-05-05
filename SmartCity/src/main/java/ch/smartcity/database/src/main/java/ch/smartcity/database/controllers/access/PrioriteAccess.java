package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.Priorite;
import ch.smartcity.database.models.Priorite_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PrioriteAccess {

    private final ConfigurationManager configurationManager;
    private final Logger logger;
    private final Hibernate hibernate;

    private PrioriteAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
    }

    public static PrioriteAccess getInstance() {
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

    public static List<Priorite> get(String nomPriorite, Integer niveau) {
        List<Priorite> prioriteList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = getHibernate().getCriteriaBuilder();
            CriteriaQuery<Priorite> criteriaQuery = criteriaBuilder.createQuery(Priorite.class);
            Root<Priorite> prioriteRoot = criteriaQuery.from(Priorite.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomPriorite != null && !nomPriorite.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        prioriteRoot.get(Priorite_.nomPriorite),
                        nomPriorite.toLowerCase()));
            }

            if (niveau != null) {
                predicateList.add(criteriaBuilder.equal(
                        prioriteRoot.get(Priorite_.niveau),
                        niveau));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            prioriteList = getHibernate().createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        getLogger().info(String.format(
                getConfigurationManager().getString("databaseAccess.results"),
                prioriteList != null ? prioriteList.size() : 0,
                Priorite.class.getSimpleName()));

        return prioriteList;
    }

    public static void save(String nomPriorite, Integer numero) {
        DatabaseAccess.save(new Priorite(nomPriorite, numero));
    }

    public static void update(Integer idPriorite, String nomPriorite, Integer niveau) {
        Priorite priorite = DatabaseAccess.get(Priorite.class, idPriorite);

        if (priorite != null) {
            setAll(priorite, nomPriorite, niveau);
            DatabaseAccess.update(priorite);
        }
    }

    public static void update(String oldNomPriorite,
                              Integer oldNiveau,
                              String newNomPriorite,
                              Integer newNiveau) {
        List<Priorite> prioriteList = get(oldNomPriorite, oldNiveau);

        if (prioriteList != null) {
            for (Priorite priorite : prioriteList) {
                setAll(priorite, newNomPriorite, newNiveau);
            }

            DatabaseAccess.update(prioriteList);
        }
    }

    public static void delete(String nomPriorite, Integer niveau) {
        DatabaseAccess.delete(get(nomPriorite, niveau));
    }

    private static void setAll(Priorite priorite, String nomPriorite, Integer niveau) {
        if (nomPriorite != null) {
            priorite.setNomPriorite(nomPriorite);
        }

        if (niveau != null) {
            priorite.setNiveau(niveau);
        }
    }

    private static class SingletonHolder {
        private final static PrioriteAccess instance = new PrioriteAccess();
    }
}
