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

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(SexeAccess.class.getName());
    }

    public static List<Sexe> get(String nomSexe) {
        List<Sexe> sexeList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.openSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
            CriteriaQuery<Sexe> criteriaQuery = criteriaBuilder
                    .createQuery(Sexe.class);
            Root<Sexe> sexeRoot = criteriaQuery.from(Sexe.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomSexe != null) {
                predicateList.add(criteriaBuilder.equal(sexeRoot.get(
                        Sexe_.nomSexe),
                        nomSexe.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            sexeList = Hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        LOGGER.info(String.format(
                ConfigurationManager.getString("databaseAccess.results"),
                sexeList != null ? sexeList.size() : 0,
                Sexe.class.getSimpleName()));

        return sexeList;
    }

    public static void save(String nomSexe) {
        DatabaseAccess.save(new Sexe(nomSexe));
    }

    public static void update(Integer idSexe, String nomSexe) {
        Sexe sexe = DatabaseAccess.get(Sexe.class, idSexe);

        if (sexe != null) {
            setAll(sexe, nomSexe);
            DatabaseAccess.update(sexe);
        }
    }

    public static void update(String oldNomSexe, String newNomSexe) {
        List<Sexe> sexeList = get(oldNomSexe);

        if (sexeList != null) {
            for (Sexe sexe : sexeList) {
                setAll(sexe, newNomSexe);
            }

            DatabaseAccess.update(sexeList);
        }
    }

    public static void delete(String nomSexe) {
        DatabaseAccess.delete(get(nomSexe));
    }

    private static void setAll(Sexe sexe, String nomSexe) {
        if (nomSexe != null) {
            sexe.setNomSexe(nomSexe);
        }
    }
}
