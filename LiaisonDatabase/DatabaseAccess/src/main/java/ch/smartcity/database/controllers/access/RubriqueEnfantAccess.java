package database.controllers.access;

import database.controllers.ConfigurationManager;
import database.controllers.Hibernate;
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

class RubriqueEnfantAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(RubriqueEnfantAccess.class.getName());
    }

    private String nomRubriqueParent;

    private void setAll(RubriqueEnfant rubriqueEnfant,
                        RubriqueParent rubriqueParent,
                        String nomRubriqueEnfant) {
        if (rubriqueParent != null) {
            rubriqueEnfant.setRubriqueParent(rubriqueParent);
        }

        if (nomRubriqueEnfant != null) {
            rubriqueEnfant.setNomRubriqueEnfant(nomRubriqueEnfant);
        }
    }

    private void checkNull(RubriqueParent rubriqueParent) {
        nomRubriqueParent = rubriqueParent != null ? rubriqueParent.getNomRubriqueParent() : null;
    }

    public List<RubriqueEnfant> get(RubriqueParent rubriqueParent, String nomRubriqueEnfant) {
        checkNull(rubriqueParent);
        return get(nomRubriqueParent, nomRubriqueEnfant);
    }

    public List<RubriqueEnfant> get(String nomRubriqueParent, String nomRubriqueEnfant) {
        List<RubriqueEnfant> rubriqueEnfantList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
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
            rubriqueEnfantList = Hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        LOGGER.log(Level.INFO,
                ConfigurationManager.getString("databaseAccess.results"),
                rubriqueEnfantList != null ? rubriqueEnfantList.size() : 0);

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
            setAll(rubriqueEnfant, rubriqueParent, nomRubriqueEnfant);
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
                setAll(rubriqueEnfant, newRubriqueParent, newNomRubriqueEnfant);
            }

            DatabaseAccess.update(rubriqueEnfantList);
        }
    }

    public void delete(RubriqueParent rubriqueParent,
                       String nomRubriqueEnfant) {
        checkNull(rubriqueParent);
        delete(nomRubriqueParent, nomRubriqueEnfant);
    }

    public void delete(String nomRubriqueParent,
                       String nomRubriqueEnfant) {
        DatabaseAccess.delete(get(nomRubriqueParent, nomRubriqueEnfant));
    }
}
