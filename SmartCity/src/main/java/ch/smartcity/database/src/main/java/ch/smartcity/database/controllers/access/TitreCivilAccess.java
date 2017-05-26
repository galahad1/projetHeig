package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.TitreCivil;
import ch.smartcity.database.models.TitreCivil_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TitreCivilAccess {

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

    private TitreCivilAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
        databaseAccess = DatabaseAccess.getInstance();
    }

    public static TitreCivilAccess getInstance() {
        return SingletonHolder.instance;
    }

    public List<TitreCivil> get(String titre, String abreviation) {
        List<TitreCivil> titreCivilList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<TitreCivil> criteriaQuery = criteriaBuilder
                    .createQuery(TitreCivil.class);
            Root<TitreCivil> titreCivilRoot = criteriaQuery.from(TitreCivil.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (titre != null && !titre.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(titreCivilRoot.get(
                        TitreCivil_.titre),
                        titre.toLowerCase()));
            }

            if (abreviation != null && !abreviation.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(titreCivilRoot.get(
                        TitreCivil_.abreviation),
                        abreviation.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            titreCivilList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        } finally {
            databaseAccess.close(session);
        }

        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                titreCivilList != null ? titreCivilList.size() : 0,
                TitreCivil.class.getSimpleName()));

        return titreCivilList;
    }

    public void save(String titre, String abreviation) {
        databaseAccess.save(new TitreCivil(titre, abreviation));
    }

    public void update(Integer idTitreCivil, String titre, String abreviation) {
        TitreCivil titreCivil = databaseAccess.get(TitreCivil.class, idTitreCivil);

        if (titreCivil != null) {
            setAll(titreCivil, titre, abreviation);
            databaseAccess.update(titreCivil);
        }
    }

    public void update(String oldTitre,
                       String oldAbreviation,
                       String newTitre,
                       String newAbreviation) {
        List<TitreCivil> titreCivilList = get(oldTitre, oldAbreviation);

        if (titreCivilList != null) {
            for (TitreCivil titreCivil : titreCivilList) {
                setAll(titreCivil, newTitre, newAbreviation);
            }

            databaseAccess.update(titreCivilList);
        }
    }

    public void delete(String titre, String abreviation) {
        databaseAccess.delete(get(titre, abreviation));
    }

    private void setAll(TitreCivil titreCivil, String titre, String abreviation) {
        if (titre != null) {
            titreCivil.setTitre(titre);
        }

        if (abreviation != null) {
            titreCivil.setAbreviation(abreviation);
        }
    }

    private static class SingletonHolder {
        private static final TitreCivilAccess instance = new TitreCivilAccess();
    }
}
