package database.controllers;

import database.models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdresseAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(AdresseAccess.class.getName());
    }

    private DatabaseManager databaseManager;

    AdresseAccess(DatabaseManager databaseManager) {
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

    public List<Adresse> get(Rue rue, String numeroDeRue, Npa npa) {
        String nomRue = rue != null ? rue.getNomRue() : null;
        String numeroNpa = npa != null ? npa.getNumeroNpa() : null;

        return get(nomRue, numeroDeRue, numeroNpa);
    }

    public List<Adresse> get(String nomRue, String numeroDeRue, String numeroNpa) {
        List<Adresse> adresseList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Adresse> criteriaQuery = criteriaBuilder.createQuery(Adresse.class);

            Root<Adresse> adresseRoot = criteriaQuery.from(Adresse.class);
            Join<Adresse, Rue> adresseRueJoin = adresseRoot.join(Adresse_.rue);
            Join<Adresse, Npa> adresseNpaJoin = adresseRoot.join(Adresse_.npa);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomRue != null) {
                predicateList.add(criteriaBuilder.equal(
                        adresseRueJoin.get(Rue_.nomRue),
                        nomRue.toLowerCase()));
            }

            if (numeroDeRue != null) {
                predicateList.add(criteriaBuilder.equal(
                        adresseRoot.get(Adresse_.numeroDeRue),
                        numeroDeRue.toLowerCase()));
            }

            if (numeroNpa != null) {
                predicateList.add(criteriaBuilder.equal(
                        adresseNpaJoin.get(Npa_.numeroNpa),
                        numeroNpa.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            adresseList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, adresseList != null ?
                adresseList.size() + " " + databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return adresseList;
    }

    public void save(Rue rue, String numeroDeRue, Npa npa) {
        DatabaseAccess.save(new Adresse(rue, numeroDeRue, npa));
    }

    public void update(Integer idAdresse, Rue rue, String numeroDeRue, Npa npa) {
        Adresse adresse = DatabaseAccess.get(Adresse.class, idAdresse);

        if (adresse != null) {
            if (rue != null) {
                adresse.setRue(rue);
            }

            if (numeroDeRue != null) {
                adresse.setNumeroDeRue(numeroDeRue);
            }

            if (npa != null) {
                adresse.setNpa(npa);
            }

            DatabaseAccess.update(adresse);
        }
    }

    public void update(Rue oldRue,
                       String oldNumeroDeRue,
                       Npa oldNpa,
                       Rue newRue,
                       String newNumeroDeRue,
                       Npa newNpa) {
        List<Adresse> adresseList = get(oldRue, oldNumeroDeRue, oldNpa);

        if (adresseList != null) {
            for (Adresse adresse : adresseList) {
                if (newRue != null) {
                    adresse.setRue(newRue);
                }

                if (newNumeroDeRue != null) {
                    adresse.setNumeroDeRue(newNumeroDeRue);
                }

                if (newNpa != null) {
                    adresse.setNpa(newNpa);
                }
            }

            DatabaseAccess.update(adresseList);
        }
    }

    public void delete(Rue rue, String numeroDeRue, Npa npa) {
        String nomRue = rue != null ? rue.getNomRue() : null;
        String numeroNpa = npa != null ? npa.getNumeroNpa() : null;

        delete(nomRue, numeroDeRue, numeroNpa);
    }

    public void delete(String nomRue, String numeroDeRue, String numeroNpa) {
        DatabaseAccess.delete(get(nomRue, numeroDeRue, numeroNpa));
    }
}
