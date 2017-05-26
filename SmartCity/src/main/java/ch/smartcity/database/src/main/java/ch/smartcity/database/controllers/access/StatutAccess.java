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

    private StatutAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
        databaseAccess = DatabaseAccess.getInstance();
    }

    public static StatutAccess getInstance() {
        return SingletonHolder.instance;
    }

    public List<Statut> get(String nomStatut) {
        List<Statut> statutList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
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
            statutList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        } finally {
            databaseAccess.close(session);
        }

        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                statutList != null ? statutList.size() : 0,
                Statut.class.getSimpleName()));

        return statutList;
    }

    public void save(String nomStatut) {
        databaseAccess.save(new Statut(nomStatut));
    }

    public void update(Integer idStatut, String nomStatut) {
        Statut statut = databaseAccess.get(Statut.class, idStatut);

        if (statut != null) {
            setAll(statut, nomStatut);
            databaseAccess.update(statut);
        }
    }

    public void update(String oldNomStatut, String newNomStatut) {
        List<Statut> statutList = get(oldNomStatut);

        if (statutList != null) {
            for (Statut statut : statutList) {
                setAll(statut, newNomStatut);
            }

            databaseAccess.update(statutList);
        }
    }

    public void delete(String nomStatut) {
        databaseAccess.delete(get(nomStatut));
    }

    private void setAll(Statut statut, String nomStatut) {
        if (nomStatut != null) {
            statut.setNomStatut(nomStatut);
        }
    }

    private static class SingletonHolder {
        private static final StatutAccess instance = new StatutAccess();
    }
}
