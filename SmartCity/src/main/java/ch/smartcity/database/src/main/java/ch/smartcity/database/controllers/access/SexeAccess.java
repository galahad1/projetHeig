package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.Sexe;
import ch.smartcity.database.models.Sexe_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SexeAccess {

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

    private SexeAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
        databaseAccess = DatabaseAccess.getInstance();
    }

    public static SexeAccess getInstance() {
        return SingletonHolder.instance;
    }

    public List<Sexe> get(String nomSexe) {
        List<Sexe> sexeList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<Sexe> criteriaQuery = criteriaBuilder
                    .createQuery(Sexe.class);
            Root<Sexe> sexeRoot = criteriaQuery.from(Sexe.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomSexe != null && !nomSexe.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(sexeRoot.get(
                        Sexe_.nomSexe),
                        nomSexe.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            sexeList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        } finally {
            databaseAccess.close(session);
        }

        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                sexeList != null ? sexeList.size() : 0,
                Sexe.class.getSimpleName()));

        return sexeList;
    }

    public void save(String nomSexe) {
        databaseAccess.save(new Sexe(nomSexe));
    }

    public void update(Integer idSexe, String nomSexe) {
        Sexe sexe = databaseAccess.get(Sexe.class, idSexe);

        if (sexe != null) {
            setAll(sexe, nomSexe);
            databaseAccess.update(sexe);
        }
    }

    public void update(String oldNomSexe, String newNomSexe) {
        List<Sexe> sexeList = get(oldNomSexe);

        if (sexeList != null) {
            for (Sexe sexe : sexeList) {
                setAll(sexe, newNomSexe);
            }

            databaseAccess.update(sexeList);
        }
    }

    public void delete(String nomSexe) {
        databaseAccess.delete(get(nomSexe));
    }

    private void setAll(Sexe sexe, String nomSexe) {
        if (nomSexe != null) {
            sexe.setNomSexe(nomSexe);
        }
    }

    private static class SingletonHolder {
        private static final SexeAccess instance = new SexeAccess();
    }
}
