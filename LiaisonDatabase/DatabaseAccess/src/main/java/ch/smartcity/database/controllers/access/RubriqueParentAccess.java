package database.controllers.access;

import database.controllers.ConfigurationManager;
import database.controllers.DatabaseAccess;
import database.controllers.Hibernate;
import database.models.RubriqueParent;
import database.models.RubriqueParent_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RubriqueParentAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(RubriqueParentAccess.class.getName());
    }

    public static List<RubriqueParent> get(String nomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
            CriteriaQuery<RubriqueParent> criteriaQuery = criteriaBuilder
                    .createQuery(RubriqueParent.class);
            Root<RubriqueParent> rubriqueParentRoot = criteriaQuery.from(RubriqueParent.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomRubriqueParent != null) {
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

        LOGGER.log(Level.INFO, String.format(
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
}
