package database.controllers;

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

    static {
        LOGGER = Logger.getLogger(ConfianceAccess.class.getName());
    }

    private DatabaseManager databaseManager;

    ConfianceAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    private void rollback(Exception ex, Transaction transaction) {
        if (transaction != null) {
            try {
                transaction.rollback();
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            } catch (Exception _ex) {
                LOGGER.log(Level.SEVERE, _ex.getMessage(), _ex);
            }
        }
    }

    private void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public List<Confiance> get(Utilisateur utilisateur,
                               RubriqueEnfant rubriqueEnfant,
                               Calendar creation) {
        String nomUtilisateur = utilisateur != null ?
                utilisateur.getNomUtilisateur() : null;

        String nomRubriqueEnfant = rubriqueEnfant != null ?
                rubriqueEnfant.getNomRubriqueEnfant() : null;

        return get(nomUtilisateur, nomRubriqueEnfant, creation);
    }

    public List<Confiance> get(String nomUtilisateur,
                               String nomRubriqueEnfant,
                               Calendar creation) {
        List<Confiance> confianceList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
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
            confianceList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, confianceList != null ?
                confianceList.size() + " " +
                        databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return confianceList;
    }

    public void save(Utilisateur utilisateur, RubriqueEnfant rubriqueEnfant) {
        DatabaseAccess.save(new Confiance(new IdConfiance(utilisateur, rubriqueEnfant)));
    }

    public void update(Integer idConfiance,
                       Utilisateur utilisateur,
                       RubriqueEnfant rubriqueEnfant) {
        Confiance confiance = DatabaseAccess.get(Confiance.class, idConfiance);

        if (confiance != null) {
            if (utilisateur != null) {
                confiance.setUtilisateur(utilisateur);
            }

            if (rubriqueEnfant != null) {
                confiance.setRubriqueEnfant(rubriqueEnfant);
            }

            DatabaseAccess.update(confiance);
        }
    }

    public void update(Utilisateur oldUtilisateur,
                       RubriqueEnfant oldRubriqueEnfant,
                       Calendar creation,
                       Utilisateur newUtilisateur,
                       RubriqueEnfant newRubriqueEnfant) {
        List<Confiance> confianceList = get(oldUtilisateur, oldRubriqueEnfant, creation);

        if (confianceList != null) {
            for (Confiance confiance : confianceList) {
                if (newUtilisateur != null) {
                    confiance.setUtilisateur(newUtilisateur);
                }

                if (newRubriqueEnfant != null) {
                    confiance.setRubriqueEnfant(newRubriqueEnfant);
                }
            }

            DatabaseAccess.update(confianceList);
        }
    }

    public void delete(Utilisateur utilisateur,
                       RubriqueEnfant rubriqueEnfant,
                       Calendar creation) {
        String nomUtilisateur = utilisateur != null ?
                utilisateur.getNomUtilisateur() : null;

        String nomRubriqueEnfant = rubriqueEnfant != null ?
                rubriqueEnfant.getNomRubriqueEnfant() : null;

        delete(nomUtilisateur, nomRubriqueEnfant, creation);
    }

    public void delete(String nomUtilisateur,
                       String nomRubriqueEnfant,
                       Calendar creation) {
        DatabaseAccess.delete(get(nomUtilisateur, nomRubriqueEnfant, creation));
    }
}
