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

    private RueAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
        databaseAccess = DatabaseAccess.getInstance();
    }

    public static RueAccess getInstance() {
        return SingletonHolder.instance;
    }

    public List<Rue> get(String nomRue) {
        List<Rue> rueList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
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
            rueList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        } finally {
            databaseAccess.close(session);
        }

        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                rueList != null ? rueList.size() : 0,
                Rue.class.getSimpleName()));

        return rueList;
    }

    public void save(String nomRue) {
        databaseAccess.save(new Rue(nomRue));
    }

    public void update(Integer idRue, String nomRue) {
        Rue rue = databaseAccess.get(Rue.class, idRue);

        if (rue != null) {
            setAll(rue, nomRue);
            databaseAccess.update(rue);
        }
    }

    public void update(String oldNomRue, String newNomRue) {
        List<Rue> rueList = get(oldNomRue);

        if (rueList != null) {
            for (Rue rue : rueList) {
                setAll(rue, newNomRue);
            }

            databaseAccess.update(rueList);
        }
    }

    public void delete(String nomRue) {
        databaseAccess.delete(get(nomRue));
    }

    private void setAll(Rue rue, String nomRue) {
        if (nomRue != null) {
            rue.setNomRue(nomRue);
        }
    }

    private static class SingletonHolder {
        private static final RueAccess instance = new RueAccess();
    }
}
