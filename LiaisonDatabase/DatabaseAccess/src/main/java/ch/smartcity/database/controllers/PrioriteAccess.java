package database.controllers;

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
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PrioriteAccess {

    private final static Logger LOGGER;

    static {
        try {
            LogManager.getLogManager().readConfiguration(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(DatabaseManager.LOGGING_PROPERTIES_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER = Logger.getLogger(PrioriteAccess.class.getName());
    }

    private final DatabaseManager databaseManager;

    PrioriteAccess(DatabaseManager databaseManager) {
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

    public List<Priorite> get(String nomPriorite, Integer niveau) {
        List<Priorite> prioriteList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
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
            prioriteList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, prioriteList != null ?
                prioriteList.size() + " " + databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return prioriteList;
    }

    public void save(String nomPriorite, Integer numero) {
        DatabaseAccess.save(new Priorite(nomPriorite, numero));
    }

    public void update(Integer idPriorite, String nomPriorite, Integer niveau) {
        Priorite priorite = DatabaseAccess.get(Priorite.class, idPriorite);

        if (priorite != null) {
            if (nomPriorite != null) {
                priorite.setNomPriorite(nomPriorite);
            }

            if (niveau != null) {
                priorite.setNiveau(niveau);
            }

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
                if (newNomPriorite != null) {
                    priorite.setNomPriorite(newNomPriorite);
                }

                if (newNiveau != null) {
                    priorite.setNiveau(newNiveau);
                }
            }

            DatabaseAccess.update(prioriteList);
        }
    }

    public void delete(String nomPriorite, Integer niveau) {
        DatabaseAccess.delete(get(nomPriorite, niveau));
    }
}
