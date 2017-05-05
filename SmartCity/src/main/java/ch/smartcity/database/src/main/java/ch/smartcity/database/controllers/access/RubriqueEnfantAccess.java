package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.RubriqueEnfant;
import ch.smartcity.database.models.RubriqueEnfant_;
import ch.smartcity.database.models.RubriqueParent;
import ch.smartcity.database.models.RubriqueParent_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RubriqueEnfantAccess {

    private static String nomRubriqueParent;
    private final ConfigurationManager configurationManager;
    private final Logger logger;
    private final Hibernate hibernate;

    private RubriqueEnfantAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
    }

    public static RubriqueEnfantAccess getInstance() {
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

    public static List<RubriqueEnfant> get(RubriqueParent rubriqueParent, String nomRubriqueEnfant) {
        checkNull(rubriqueParent);
        return get(nomRubriqueParent, nomRubriqueEnfant);
    }

    public static List<RubriqueEnfant> get(String nomRubriqueParent, String nomRubriqueEnfant) {
        List<RubriqueEnfant> rubriqueEnfantList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = getHibernate().getCriteriaBuilder();
            CriteriaQuery<RubriqueEnfant> criteriaQuery = criteriaBuilder
                    .createQuery(RubriqueEnfant.class);
            Root<RubriqueEnfant> rubriqueEnfantRoot = criteriaQuery.from(RubriqueEnfant.class);
            Join<RubriqueEnfant, RubriqueParent> rubriqueEnfantRubriqueParentJoin =
                    rubriqueEnfantRoot.join(RubriqueEnfant_.rubriqueParent);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomRubriqueParent != null && !nomRubriqueParent.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(rubriqueEnfantRubriqueParentJoin.get(
                        RubriqueParent_.nomRubriqueParent),
                        nomRubriqueParent.toLowerCase()));
            }

            if (nomRubriqueEnfant != null && !nomRubriqueEnfant.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(rubriqueEnfantRoot.get(
                        RubriqueEnfant_.nomRubriqueEnfant),
                        nomRubriqueEnfant.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            rubriqueEnfantList = getHibernate().createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        getLogger().info(String.format(
                getConfigurationManager().getString("databaseAccess.results"),
                rubriqueEnfantList != null ? rubriqueEnfantList.size() : 0,
                RubriqueEnfant.class.getSimpleName()));

        return rubriqueEnfantList;
    }

    public static void save(RubriqueParent rubriqueParent, String nomRubriqueEnfant) {
        DatabaseAccess.save(new RubriqueEnfant(rubriqueParent, nomRubriqueEnfant));
    }

    public static void update(Integer idRubriqueEnfant,
                              RubriqueParent rubriqueParent,
                              String nomRubriqueEnfant) {
        RubriqueEnfant rubriqueEnfant = DatabaseAccess.get(RubriqueEnfant.class, idRubriqueEnfant);

        if (rubriqueEnfant != null) {
            setAll(rubriqueEnfant, rubriqueParent, nomRubriqueEnfant);
            DatabaseAccess.update(rubriqueEnfant);
        }
    }

    public static void update(RubriqueParent oldRubriqueEnfant,
                              String oldNomRubriqueEnfant,
                              RubriqueParent newRubriqueParent,
                              String newNomRubriqueEnfant) {
        List<RubriqueEnfant> rubriqueEnfantList = get(oldRubriqueEnfant, oldNomRubriqueEnfant);

        if (rubriqueEnfantList != null) {
            for (RubriqueEnfant rubriqueEnfant : rubriqueEnfantList) {
                setAll(rubriqueEnfant, newRubriqueParent, newNomRubriqueEnfant);
            }

            DatabaseAccess.update(rubriqueEnfantList);
        }
    }

    public static void delete(RubriqueParent rubriqueParent,
                              String nomRubriqueEnfant) {
        checkNull(rubriqueParent);
        delete(nomRubriqueParent, nomRubriqueEnfant);
    }

    public static void delete(String nomRubriqueParent,
                              String nomRubriqueEnfant) {
        DatabaseAccess.delete(get(nomRubriqueParent, nomRubriqueEnfant));
    }

    private static void setAll(RubriqueEnfant rubriqueEnfant,
                               RubriqueParent rubriqueParent,
                               String nomRubriqueEnfant) {
        if (rubriqueParent != null) {
            rubriqueEnfant.setRubriqueParent(rubriqueParent);
        }

        if (nomRubriqueEnfant != null) {
            rubriqueEnfant.setNomRubriqueEnfant(nomRubriqueEnfant);
        }
    }

    private static void checkNull(RubriqueParent rubriqueParent) {
        nomRubriqueParent = rubriqueParent != null ? rubriqueParent.getNomRubriqueParent() : null;
    }

    private static class SingletonHolder {
        private final static RubriqueEnfantAccess instance = new RubriqueEnfantAccess();
    }
}
