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

    /**
     * Utilisé pour accéder aux fichiers de propriétés
     */
    private final ConfigurationManager configurationManager;

    /**
     * Utilisé pour journaliser les actions effectuées
     */
    private final Logger logger;

    /**
     * Utilisé pour la connexion à la base de données
     */
    private final Hibernate hibernate;

    /**
     * Utilisé pour des accès génériques à la base de données
     */
    private final DatabaseAccess databaseAccess;

    private RubriqueParentAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
        databaseAccess = DatabaseAccess.getInstance();
    }

    public static RubriqueParentAccess getInstance() {
        return SingletonHolder.instance;
    }

    public List<RubriqueParent> get(String nomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
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
            rubriqueParentList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        } finally {
            databaseAccess.close(session);
        }

        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                rubriqueParentList != null ? rubriqueParentList.size() : 0,
                RubriqueParent.class.getSimpleName()));

        return rubriqueParentList;
    }

    public void save(String nomRubriqueParent) {
        databaseAccess.save(new RubriqueParent(nomRubriqueParent));
    }

    public void update(Integer idRubriqueParent, String nomRubriqueParent) {
        RubriqueParent rubriqueParent = databaseAccess.get(RubriqueParent.class, idRubriqueParent);

        if (rubriqueParent != null) {
            setAll(rubriqueParent, nomRubriqueParent);
            databaseAccess.update(rubriqueParent);
        }
    }

    public void update(String oldNomRubriqueParent, String newNomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = get(oldNomRubriqueParent);

        if (rubriqueParentList != null) {
            for (RubriqueParent rubriqueParent : rubriqueParentList) {
                setAll(rubriqueParent, newNomRubriqueParent);
            }

            databaseAccess.update(rubriqueParentList);
        }
    }

    public void delete(String nomRubriqueParent) {
        databaseAccess.delete(get(nomRubriqueParent));
    }

    private void setAll(RubriqueParent rubriqueParent, String nomRubriqueParent) {
        if (nomRubriqueParent != null) {
            rubriqueParent.setNomRubriqueParent(nomRubriqueParent);
        }
    }

    private static class SingletonHolder {
        private static final RubriqueParentAccess instance = new RubriqueParentAccess();
    }
}
