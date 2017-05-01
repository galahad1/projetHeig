package database.controllers;

import database.models.RubriqueEnfant;
import database.models.RubriqueEnfant_;
import database.models.RubriqueParent;
import database.models.RubriqueParent_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RubriqueEnfantAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(RubriqueEnfantAccess.class.getName());
    }

    private DatabaseManager databaseManager;

    RubriqueEnfantAccess(DatabaseManager databaseManager) {
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

    public List<RubriqueEnfant> get(RubriqueParent rubriqueParent, String nomRubriqueEnfant) {
        String nomRubriqueParent = rubriqueParent != null ?
                rubriqueParent.getNomRubriqueParent() : null;

        return get(nomRubriqueParent, nomRubriqueEnfant);
    }

    public List<RubriqueEnfant> get(String nomRubriqueParent, String nomRubriqueEnfant) {
        List<RubriqueEnfant> rubriqueEnfantList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<RubriqueEnfant> criteriaQuery = criteriaBuilder
                    .createQuery(RubriqueEnfant.class);
            Root<RubriqueEnfant> rubriqueEnfantRoot = criteriaQuery.from(RubriqueEnfant.class);
            Join<RubriqueEnfant, RubriqueParent> rubriqueEnfantRubriqueParentJoin =
                    rubriqueEnfantRoot.join(RubriqueEnfant_.rubriqueParent);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomRubriqueParent != null) {
                predicateList.add(criteriaBuilder.equal(rubriqueEnfantRubriqueParentJoin.get(
                        RubriqueParent_.nomRubriqueParent),
                        nomRubriqueParent.toLowerCase()));
            }

            if (nomRubriqueEnfant != null) {
                predicateList.add(criteriaBuilder.equal(rubriqueEnfantRoot.get(
                        RubriqueEnfant_.nomRubriqueEnfant),
                        nomRubriqueEnfant.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            rubriqueEnfantList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, rubriqueEnfantList != null ?
                rubriqueEnfantList.size() + " " +
                        databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return rubriqueEnfantList;
    }

    public void save(RubriqueParent rubriqueParent, String nomRubriqueEnfant) {
        DatabaseAccess.save(new RubriqueEnfant(rubriqueParent, nomRubriqueEnfant));
    }

    public void update(Integer idRubriqueEnfant,
                       RubriqueParent rubriqueParent,
                       String nomRubriqueEnfant) {
        RubriqueEnfant rubriqueEnfant = DatabaseAccess.get(RubriqueEnfant.class, idRubriqueEnfant);

        if (rubriqueEnfant != null) {
            if (rubriqueParent != null) {
                rubriqueEnfant.setRubriqueParent(rubriqueParent);
            }

            if (nomRubriqueEnfant != null) {
                rubriqueEnfant.setNomRubriqueEnfant(nomRubriqueEnfant);
            }

            DatabaseAccess.update(rubriqueEnfant);
        }
    }

    public void update(RubriqueParent oldRubriqueEnfant,
                       String oldNomRubriqueEnfant,
                       RubriqueParent newRubriqueParent,
                       String newNomRubriqueEnfant) {
        List<RubriqueEnfant> rubriqueEnfantList = get(oldRubriqueEnfant, oldNomRubriqueEnfant);

        if (rubriqueEnfantList != null) {
            for (RubriqueEnfant rubriqueEnfant : rubriqueEnfantList) {
                if (newRubriqueParent != null) {
                    rubriqueEnfant.setRubriqueParent(newRubriqueParent);
                }

                if (newNomRubriqueEnfant != null) {
                    rubriqueEnfant.setNomRubriqueEnfant(newNomRubriqueEnfant);
                }
            }

            DatabaseAccess.update(rubriqueEnfantList);
        }
    }

    public void delete(RubriqueParent rubriqueParent,
                       String nomRubriqueEnfant) {
        String nomRubriqueParent = rubriqueParent != null ?
                rubriqueParent.getNomRubriqueParent() : null;

        delete(nomRubriqueParent, nomRubriqueEnfant);
    }

    public void delete(String nomRubriqueParent,
                       String nomRubriqueEnfant) {
        DatabaseAccess.delete(get(nomRubriqueParent, nomRubriqueEnfant));
    }
}
