package database.controllers.access;

import database.controllers.ConfigurationManager;
import database.controllers.Hibernate;
import database.models.Priorite;
import database.models.Priorite_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class PrioriteAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(PrioriteAccess.class.getName());
    }

    private void setAll(Priorite priorite, String nomPriorite, Integer niveau) {
        if (nomPriorite != null) {
            priorite.setNomPriorite(nomPriorite);
        }

        if (niveau != null) {
            priorite.setNiveau(niveau);
        }
    }

    public List<Priorite> get(String nomPriorite, Integer niveau) {
        List<Priorite> prioriteList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
            CriteriaQuery<Priorite> criteriaQuery = criteriaBuilder.createQuery(Priorite.class);
            Root<Priorite> prioriteRoot = criteriaQuery.from(Priorite.class);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomPriorite != null) {
                predicateList.add(criteriaBuilder.equal(
                        prioriteRoot.get(Priorite_.nomPriorite),
                        nomPriorite.toLowerCase()));
            }

            if (niveau != null) {
                predicateList.add(criteriaBuilder.equal(
                        prioriteRoot.get(Priorite_.niveau),
                        niveau));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            prioriteList = Hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        LOGGER.log(Level.INFO, String.format(
                ConfigurationManager.getString("databaseAccess.results"),
                prioriteList != null ? prioriteList.size() : 0,
                Priorite.class.getSimpleName()));

        return prioriteList;
    }

    public void save(String nomPriorite, Integer numero) {
        DatabaseAccess.save(new Priorite(nomPriorite, numero));
    }

    public void update(Integer idPriorite, String nomPriorite, Integer niveau) {
        Priorite priorite = DatabaseAccess.get(Priorite.class, idPriorite);

        if (priorite != null) {
            setAll(priorite, nomPriorite, niveau);
            DatabaseAccess.update(priorite);
        }
    }

    public void update(String oldNomPriorite,
                       Integer oldNiveau,
                       String newNomPriorite,
                       Integer newNiveau) {
        List<Priorite> prioriteList = get(oldNomPriorite, oldNiveau);

        if (prioriteList != null) {
            for (Priorite priorite : prioriteList) {
                setAll(priorite, newNomPriorite, newNiveau);
            }

            DatabaseAccess.update(prioriteList);
        }
    }

    public void delete(String nomPriorite, Integer niveau) {
        DatabaseAccess.delete(get(nomPriorite, niveau));
    }
}
