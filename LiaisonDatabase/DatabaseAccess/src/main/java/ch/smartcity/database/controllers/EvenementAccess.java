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

public class EvenementAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(EvenementAccess.class.getName());
    }

    private DatabaseManager databaseManager;

    EvenementAccess(DatabaseManager databaseManager) {
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

    public List<Evenement> get(RubriqueEnfant rubriqueEnfant,
                               Utilisateur utilisateur,
                               String nomEvenement,
                               Adresse adresse,
                               Double latitude,
                               Double longitude,
                               Calendar debut,
                               Calendar fin,
                               String details,
                               Priorite priorite,
                               Statut statut,
                               Calendar creation) {
        String nomRubriqueEnfant = rubriqueEnfant != null ?
                rubriqueEnfant.getNomRubriqueEnfant() : null;


        String nomUtilisateur = utilisateur != null ?
                utilisateur.getNomUtilisateur() : null;

        String nomRue = null;
        String numeroDeRue = null;
        String numeroNpa = null;
        if (adresse != null) {
            nomRue = adresse.getRue().getNomRue();
            numeroDeRue = adresse.getNumeroDeRue();
            numeroNpa = adresse.getNpa().getNumeroNpa();
        }

        String nomPriorite = priorite != null ?
                priorite.getNomPriorite() : null;

        String nomStatut = statut != null ?
                statut.getNomStatut() : null;

        return get(nomRubriqueEnfant,
                nomUtilisateur,
                nomEvenement,
                nomRue,
                numeroDeRue,
                numeroNpa,
                latitude,
                longitude,
                debut,
                fin,
                details,
                nomPriorite,
                nomStatut,
                creation);
    }

    public List<Evenement> get(String nomRubriqueEnfant,
                               String nomUtilisateur,
                               String nomEvenement,
                               String nomRue,
                               String numeroDeRue,
                               String numeroNpa,
                               Double latitude,
                               Double longitude,
                               Calendar debut,
                               Calendar fin,
                               String details,
                               String nomPriorite,
                               String nomStatut,
                               Calendar creation) {
        List<Evenement> evenementList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Evenement> criteriaQuery = criteriaBuilder.createQuery(Evenement.class);

            Root<Evenement> evenementRoot = criteriaQuery.from(Evenement.class);
            Join<Evenement, RubriqueEnfant> evenementRubriqueEnfantJoin =
                    evenementRoot.join(Evenement_.rubriqueEnfant);
            Join<Evenement, Utilisateur> evenementUtilisateurJoin =
                    evenementRoot.join(Evenement_.utilisateur);
            Join<Evenement, Adresse> evenementAdresseJoin =
                    evenementRoot.join(Evenement_.adresse);
            Join<Adresse, Rue> adresseRueJoin =
                    evenementAdresseJoin.join(Adresse_.rue);
            Join<Adresse, Npa> adresseNpaJoin =
                    evenementAdresseJoin.join(Adresse_.npa);
            Join<Evenement, Priorite> evenementPrioriteJoin =
                    evenementRoot.join(Evenement_.priorite);
            Join<Evenement, Statut> evenementStatutJoin =
                    evenementRoot.join(Evenement_.statut);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomRubriqueEnfant != null) {
                predicateList.add(criteriaBuilder.equal(
                        evenementRubriqueEnfantJoin.get(RubriqueEnfant_.nomRubriqueEnfant),
                        nomRubriqueEnfant.toLowerCase()));
            }

            if (nomUtilisateur != null) {
                predicateList.add(criteriaBuilder.equal(
                        evenementUtilisateurJoin.get(Utilisateur_.nomUtilisateur),
                        nomUtilisateur.toLowerCase()));
            }

            if (nomEvenement != null) {
                predicateList.add(criteriaBuilder.equal(
                        evenementRoot.get(Evenement_.nomEvenement),
                        nomEvenement.toLowerCase()));
            }

            if (nomRue != null) {
                predicateList.add(criteriaBuilder.equal(
                        adresseRueJoin.get(Rue_.nomRue),
                        nomRue.toLowerCase()));
            }

            if (numeroDeRue != null) {
                predicateList.add(criteriaBuilder.equal(
                        evenementAdresseJoin.get(Adresse_.numeroDeRue),
                        numeroDeRue.toLowerCase()));
            }

            if (numeroNpa != null) {
                predicateList.add(criteriaBuilder.equal(
                        adresseNpaJoin.get(Npa_.numeroNpa),
                        numeroNpa.toLowerCase()));
            }

            if (latitude != null) {
                predicateList.add(criteriaBuilder.equal(
                        evenementRoot.get(Evenement_.latitude),
                        latitude));
            }

            if (longitude != null) {
                predicateList.add(criteriaBuilder.equal(
                        evenementRoot.get(Evenement_.longitude),
                        longitude));
            }

            if (debut != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(
                        evenementRoot.get(Evenement_.debut),
                        debut));
            }

            if (fin != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(
                        evenementRoot.get(Evenement_.fin),
                        fin));
            }

            if (details != null) {
                predicateList.add(criteriaBuilder.equal(
                        evenementRoot.get(Evenement_.details),
                        details.toLowerCase()));
            }

            if (nomPriorite != null) {
                predicateList.add(criteriaBuilder.equal(
                        evenementPrioriteJoin.get(Priorite_.nomPriorite),
                        nomPriorite.toLowerCase()));
            }

            if (nomStatut != null) {
                predicateList.add(criteriaBuilder.equal(
                        evenementStatutJoin.get(Statut_.nomStatut),
                        nomStatut.toLowerCase()));
            }

            if (creation != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(
                        evenementRoot.get(Evenement_.creation), creation));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            evenementList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, evenementList != null ?
                evenementList.size() + " " + databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return evenementList;
    }

    public void save(RubriqueEnfant rubriqueEnfant,
                     Utilisateur utilisateur,
                     String nomEvenement,
                     Calendar debut,
                     Priorite priorite,
                     Statut statut) {
        DatabaseAccess.save(new Evenement(
                rubriqueEnfant,
                utilisateur,
                nomEvenement,
                debut,
                priorite,
                statut));
    }

    public void save(RubriqueEnfant rubriqueEnfant,
                     Utilisateur utilisateur,
                     String nomEvenement,
                     Adresse adresse,
                     Double latitude,
                     Double longitude,
                     Calendar debut,
                     Calendar fin,
                     String details,
                     Priorite priorite,
                     Statut statut) {
        DatabaseAccess.save(new Evenement(
                rubriqueEnfant,
                utilisateur,
                nomEvenement,
                adresse,
                latitude,
                longitude,
                debut,
                fin,
                details,
                priorite,
                statut));
    }

    public void update(Integer idEvenement,
                       RubriqueEnfant rubriqueEnfant,
                       Utilisateur utilisateur,
                       String nomEvenement,
                       Adresse adresse,
                       Double latitude,
                       Double longitude,
                       Calendar debut,
                       Calendar fin,
                       String details,
                       Priorite priorite,
                       Statut statut) {
        Evenement evenement = DatabaseAccess.get(Evenement.class, idEvenement);

        if (evenement != null) {
            if (rubriqueEnfant != null) {
                evenement.setRubriqueEnfant(rubriqueEnfant);
            }

            if (utilisateur != null) {
                evenement.setUtilisateur(utilisateur);
            }

            if (nomEvenement != null) {
                evenement.setNomEvenement(nomEvenement);
            }

            if (adresse != null) {
                evenement.setAdresse(adresse);
            }

            if (latitude != null) {
                evenement.setLatitude(latitude);
            }

            if (longitude != null) {
                evenement.setLongitude(longitude);
            }

            if (debut != null) {
                evenement.setDebut(debut);
            }

            if (fin != null) {
                evenement.setFin(fin);
            }

            if (details != null) {
                evenement.setDetails(details);
            }

            if (priorite != null) {
                evenement.setPriorite(priorite);
            }

            if (statut != null) {
                evenement.setStatut(statut);
            }

            DatabaseAccess.update(evenement);
        }
    }

    public void update(RubriqueEnfant oldRubriqueEnfant,
                       Utilisateur oldUtilisateur,
                       String oldNomEvenement,
                       Adresse oldAdresse,
                       Double oldLatitude,
                       Double oldLongitude,
                       Calendar oldDebut,
                       Calendar oldFin,
                       String oldDetails,
                       Priorite oldPriorite,
                       Statut oldStatut,
                       Calendar creation,
                       RubriqueEnfant newRubriqueEnfant,
                       Utilisateur newUtilisateur,
                       String newNomEvenement,
                       Adresse newAdresse,
                       Double newLatitude,
                       Double newLongitude,
                       Calendar newDebut,
                       Calendar newFin,
                       String newDetails,
                       Priorite newPriorite,
                       Statut newStatut) {
        List<Evenement> evenementList = get(
                oldRubriqueEnfant,
                oldUtilisateur,
                oldNomEvenement,
                oldAdresse,
                oldLatitude,
                oldLongitude,
                oldDebut,
                oldFin,
                oldDetails,
                oldPriorite,
                oldStatut,
                creation);

        if (evenementList != null) {
            for (Evenement evenement : evenementList) {
                if (newRubriqueEnfant != null) {
                    evenement.setRubriqueEnfant(newRubriqueEnfant);
                }

                if (newUtilisateur != null) {
                    evenement.setUtilisateur(newUtilisateur);
                }

                if (newNomEvenement != null) {
                    evenement.setNomEvenement(newNomEvenement);
                }

                if (newAdresse != null) {
                    evenement.setAdresse(newAdresse);
                }

                if (newLatitude != null) {
                    evenement.setLatitude(newLatitude);
                }

                if (newLongitude != null) {
                    evenement.setLongitude(newLongitude);
                }

                if (newDebut != null) {
                    evenement.setDebut(newDebut);
                }

                if (newFin != null) {
                    evenement.setFin(newFin);
                }

                if (newDetails != null) {
                    evenement.setDetails(newDetails);
                }

                if (newPriorite != null) {
                    evenement.setPriorite(newPriorite);
                }

                if (newStatut != null) {
                    evenement.setStatut(newStatut);
                }
            }

            DatabaseAccess.update(evenementList);
        }
    }

    public void delete(RubriqueEnfant rubriqueEnfant,
                       Utilisateur utilisateur,
                       String nomEvenement,
                       Adresse adresse,
                       Double latitude,
                       Double longitude,
                       Calendar debut,
                       Calendar fin,
                       String details,
                       Priorite priorite,
                       Statut statut,
                       Calendar creation) {
        String nomRubriqueEnfant = rubriqueEnfant != null ?
                rubriqueEnfant.getNomRubriqueEnfant() : null;

        String nomUtilisateur = utilisateur != null ?
                utilisateur.getNomUtilisateur() : null;

        String nomRue = null;
        String numeroDeRue = null;
        String numeroNpa = null;
        if (adresse != null) {
            nomRue = adresse.getRue().getNomRue();
            numeroDeRue = adresse.getNumeroDeRue();
            numeroNpa = adresse.getNpa().getNumeroNpa();
        }

        String nomPriorite = utilisateur != null ?
                priorite.getNomPriorite() : null;

        String nomStatut = utilisateur != null ?
                statut.getNomStatut() : null;

        delete(nomRubriqueEnfant,
                nomUtilisateur,
                nomEvenement,
                nomRue,
                numeroDeRue,
                numeroNpa,
                latitude,
                longitude,
                debut,
                fin,
                details,
                nomPriorite,
                nomStatut,
                creation);
    }


    public void delete(String nomRubriqueEnfant,
                       String nomUtilisateur,
                       String nomEvenement,
                       String nomRue,
                       String numeroDeRue,
                       String numeroNpa,
                       Double latitude,
                       Double longitude,
                       Calendar debut,
                       Calendar fin,
                       String details,
                       String nomPriorite,
                       String nomStatut,
                       Calendar creation) {
        DatabaseAccess.delete(get(
                nomRubriqueEnfant,
                nomUtilisateur,
                nomEvenement,
                nomRue,
                numeroDeRue,
                numeroNpa,
                latitude,
                longitude,
                debut,
                fin,
                details,
                nomPriorite,
                nomStatut,
                creation));
    }
}
