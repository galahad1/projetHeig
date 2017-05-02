package database.controllers.access;

import database.controllers.ConfigurationManager;
import database.controllers.DatabaseAccess;
import database.controllers.Hibernate;
import database.models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfianceAccess {

    private static final Logger LOGGER;
    private static String nomUtilisateur;
    private static String nomRubriqueEnfant;

    static {
        LOGGER = Logger.getLogger(ConfianceAccess.class.getName());
    }

    public static List<Confiance> get(Utilisateur utilisateur,
                                      RubriqueEnfant rubriqueEnfant,
                                      Calendar creation) {
        checkNull(utilisateur, rubriqueEnfant);
        return get(nomUtilisateur, nomRubriqueEnfant, creation);
    }

    public static List<Confiance> get(String nomUtilisateur,
                                      String nomRubriqueEnfant,
                                      Calendar creation) {
        List<Confiance> confianceList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
            CriteriaQuery<Confiance> criteriaQuery = criteriaBuilder.createQuery(Confiance.class);

            Root<Confiance> confianceRoot = criteriaQuery.from(Confiance.class);
            Join<Confiance, IdConfiance> confianceIdConfianceJoin =
                    confianceRoot.join(Confiance_.idConfiance);
            Join<IdConfiance, Utilisateur> idConfianceUtilisateurJoin =
                    confianceIdConfianceJoin.join(IdConfiance_.utilisateur);
            Join<IdConfiance, RubriqueEnfant> idConfianceRubriqueEnfantJoin =
                    confianceIdConfianceJoin.join(IdConfiance_.rubriqueEnfant);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomUtilisateur != null) {
                predicateList.add(criteriaBuilder.equal(
                        idConfianceUtilisateurJoin.get(Utilisateur_.nomUtilisateur),
                        nomUtilisateur.toLowerCase()));
            }

            if (nomRubriqueEnfant != null) {
                predicateList.add(criteriaBuilder.equal(
                        idConfianceRubriqueEnfantJoin.get(RubriqueEnfant_.nomRubriqueEnfant),
                        nomRubriqueEnfant.toLowerCase()));
            }

            if (creation != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(
                        confianceRoot.get(Confiance_.creation), creation));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            confianceList = Hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        LOGGER.log(Level.INFO, String.format(
                ConfigurationManager.getString("databaseAccess.results"),
                confianceList != null ? confianceList.size() : 0,
                Confiance.class.getSimpleName()));

        return confianceList;
    }

    public static void save(Utilisateur utilisateur, RubriqueEnfant rubriqueEnfant) {
        DatabaseAccess.save(new Confiance(new IdConfiance(utilisateur, rubriqueEnfant)));
    }

    public static void update(Integer idConfiance,
                              Utilisateur utilisateur,
                              RubriqueEnfant rubriqueEnfant) {
        Confiance confiance = DatabaseAccess.get(Confiance.class, idConfiance);

        if (confiance != null) {
            setAll(confiance, utilisateur, rubriqueEnfant);
            DatabaseAccess.update(confiance);
        }
    }

    public static void update(Utilisateur oldUtilisateur,
                              RubriqueEnfant oldRubriqueEnfant,
                              Calendar creation,
                              Utilisateur newUtilisateur,
                              RubriqueEnfant newRubriqueEnfant) {
        List<Confiance> confianceList = get(oldUtilisateur, oldRubriqueEnfant, creation);

        if (confianceList != null) {
            for (Confiance confiance : confianceList) {
                setAll(confiance, newUtilisateur, newRubriqueEnfant);
            }

            DatabaseAccess.update(confianceList);
        }
    }

    public static void delete(Utilisateur utilisateur,
                              RubriqueEnfant rubriqueEnfant,
                              Calendar creation) {
        checkNull(utilisateur, rubriqueEnfant);
        delete(nomUtilisateur, nomRubriqueEnfant, creation);
    }

    public static void delete(String nomUtilisateur,
                              String nomRubriqueEnfant,
                              Calendar creation) {
        DatabaseAccess.delete(get(nomUtilisateur, nomRubriqueEnfant, creation));
    }

    private static void setAll(Confiance confiance,
                               Utilisateur utilisateur,
                               RubriqueEnfant rubriqueEnfant) {
        if (utilisateur != null) {
            confiance.setUtilisateur(utilisateur);
        }

        if (rubriqueEnfant != null) {
            confiance.setRubriqueEnfant(rubriqueEnfant);
        }
    }

    private static void checkNull(Utilisateur utilisateur, RubriqueEnfant rubriqueEnfant) {
        nomUtilisateur = utilisateur != null ? utilisateur.getNomUtilisateur() : null;
        nomRubriqueEnfant = rubriqueEnfant != null ? rubriqueEnfant.getNomRubriqueEnfant() : null;
    }
}
