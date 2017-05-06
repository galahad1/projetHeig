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

    private final ConfigurationManager configurationManager;
    private final Logger logger;
    private final Hibernate hibernate;

    private TitreCivilAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
    }

    public static TitreCivilAccess getInstance() {
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

    public static List<TitreCivil> get(String titre, String abreviation) {
        List<TitreCivil> titreCivilList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = getHibernate().openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = getHibernate().getCriteriaBuilder();
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
            titreCivilList = getHibernate().createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        getLogger().info(String.format(
                getConfigurationManager().getString("databaseAccess.results"),
                titreCivilList != null ? titreCivilList.size() : 0,
                TitreCivil.class.getSimpleName()));

        return titreCivilList;
    }

    public static void save(String titre, String abreviation) {
        DatabaseAccess.save(new TitreCivil(titre, abreviation));
    }

    public static void update(Integer idTitreCivil, String titre, String abreviation) {
        TitreCivil titreCivil = DatabaseAccess.get(TitreCivil.class, idTitreCivil);

        if (titreCivil != null) {
            setAll(titreCivil, titre, abreviation);
            DatabaseAccess.update(titreCivil);
        }
    }

    public static void update(String oldTitre,
                              String oldAbreviation,
                              String newTitre,
                              String newAbreviation) {
        List<TitreCivil> titreCivilList = get(oldTitre, oldAbreviation);

        if (titreCivilList != null) {
            for (TitreCivil titreCivil : titreCivilList) {
                setAll(titreCivil, newTitre, newAbreviation);
            }

            DatabaseAccess.update(titreCivilList);
        }
    }

    public static void delete(String titre, String abreviation) {
        DatabaseAccess.delete(get(titre, abreviation));
    }

    private static void setAll(TitreCivil titreCivil, String titre, String abreviation) {
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
