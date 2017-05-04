package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.RubriqueParent;
import ch.smartcity.database.models.RubriqueParent_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RubriqueParentAccess {

    static {
        ConfigurationManager.initialize();
    }

    private final Logger logger;

    private RubriqueParentAccess() {
        logger = Logger.getLogger(getClass().getName());
    }

    public static RubriqueParentAccess getInstance() {
        return SingletonHolder.instance;
    }

    private static Logger getLogger() {
        return getInstance().logger;
    }

    public static List<RubriqueParent> get(String nomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
            CriteriaQuery<RubriqueParent> criteriaQuery = criteriaBuilder
                    .createQuery(RubriqueParent.class);
            Root<RubriqueParent> rubriqueParentRoot = criteriaQuery.from(RubriqueParent.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomRubriqueParent != null && !nomRubriqueParent.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(rubriqueParentRoot.get(
                        RubriqueParent_.nomRubriqueParent),
                        nomRubriqueParent.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            rubriqueParentList = Hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        getLogger().info(String.format(
                ConfigurationManager.getString("databaseAccess.results"),
                rubriqueParentList != null ? rubriqueParentList.size() : 0,
                RubriqueParent.class.getSimpleName()));

        return rubriqueParentList;
    }

    public static void save(String nomRubriqueParent) {
        DatabaseAccess.save(new RubriqueParent(nomRubriqueParent));
    }

    public static void update(Integer idRubriqueParent, String nomRubriqueParent) {
        RubriqueParent rubriqueParent = DatabaseAccess.get(RubriqueParent.class, idRubriqueParent);

        if (rubriqueParent != null) {
            setAll(rubriqueParent, nomRubriqueParent);
            DatabaseAccess.update(rubriqueParent);
        }
    }

    public static void update(String oldNomRubriqueParent, String newNomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = get(oldNomRubriqueParent);

        if (rubriqueParentList != null) {
            for (RubriqueParent rubriqueParent : rubriqueParentList) {
                setAll(rubriqueParent, newNomRubriqueParent);
            }

            DatabaseAccess.update(rubriqueParentList);
        }
    }

    public static void delete(String nomRubriqueParent) {
        DatabaseAccess.delete(get(nomRubriqueParent));
    }

    private static void setAll(RubriqueParent rubriqueParent, String nomRubriqueParent) {
        if (nomRubriqueParent != null) {
            rubriqueParent.setNomRubriqueParent(nomRubriqueParent);
        }
    }

    private static class SingletonHolder {
        private final static RubriqueParentAccess instance = new RubriqueParentAccess();
    }
}
