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

public class CommentaireAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(CommentaireAccess.class.getName());
    }

    private DatabaseManager databaseManager;

    CommentaireAccess(DatabaseManager databaseManager) {
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

    public List<Commentaire> get(Evenement evenement,
                                 Utilisateur utilisateur,
                                 String commentaire,
                                 Calendar creation) {
        String nomEvenement = evenement != null ? evenement.getNomEvenement() : null;
        String nomUtilisateur = utilisateur != null ? utilisateur.getNomUtilisateur() : null;

        return get(nomEvenement, nomUtilisateur, commentaire, creation);
    }

    public List<Commentaire> get(String nomEvenement,
                                 String nomUtilisateur,
                                 String commentaire,
                                 Calendar creation) {
        List<Commentaire> commentaireList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            session = databaseManager.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = databaseManager.getCriteriaBuilder();
            CriteriaQuery<Commentaire> criteriaQuery = criteriaBuilder
                    .createQuery(Commentaire.class);

            Root<Commentaire> commentaireRoot = criteriaQuery.from(Commentaire.class);
            Join<Commentaire, IdCommentaire> commentaireIdCommentaireJoin =
                    commentaireRoot.join(Commentaire_.idCommentaire);
            Join<IdCommentaire, Evenement> idCommentaireEvenementJoin =
                    commentaireIdCommentaireJoin.join(IdCommentaire_.evenement);
            Join<IdCommentaire, Utilisateur> idCommentaireUtilisateurJoin =
                    commentaireIdCommentaireJoin.join(IdCommentaire_.utilisateur);
            List<Predicate> predicateList = new ArrayList<>();

            if (nomEvenement != null) {
                predicateList.add(criteriaBuilder.equal(
                        idCommentaireEvenementJoin.get(Evenement_.nomEvenement),
                        nomEvenement.toLowerCase()));
            }

            if (nomUtilisateur != null) {
                predicateList.add(criteriaBuilder.equal(
                        idCommentaireUtilisateurJoin.get(Utilisateur_.nomUtilisateur),
                        nomUtilisateur.toLowerCase()));
            }

            if (commentaire != null) {
                predicateList.add(criteriaBuilder.equal(
                        commentaireRoot.get(Commentaire_.commentaire), commentaire.toLowerCase()));
            }

            if (creation != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(
                        commentaireRoot.get(Commentaire_.creation), creation));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            commentaireList = databaseManager.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception ex) {
            rollback(ex, transaction);
        } finally {
            close(session);
        }

        LOGGER.log(Level.INFO, commentaireList != null ?
                commentaireList.size() + " " +
                        databaseManager.getString("databaseAccess.results")
                : databaseManager.getString("databaseAccess.noResults"));

        return commentaireList;
    }

    public void save(Evenement evenement, Utilisateur utilisateur, String commentaire) {
        DatabaseAccess.save(new Commentaire(new IdCommentaire(evenement, utilisateur), commentaire));
    }

    public void update(Integer idCommentaire,
                       Evenement evenement,
                       Utilisateur utilisateur,
                       String commentaire) {
        Commentaire commentaireObject = DatabaseAccess.get(Commentaire.class, idCommentaire);

        if (commentaireObject != null) {
            if (evenement != null) {
                commentaireObject.setEvenement(evenement);
            }

            if (utilisateur != null) {
                commentaireObject.setUtilisateur(utilisateur);
            }

            if (commentaire != null) {
                commentaireObject.setCommentaire(commentaire);
            }

            DatabaseAccess.update(commentaireObject);
        }
    }

    public void update(Evenement oldEvenement,
                       Utilisateur oldUtilisateur,
                       String oldCommentaire,
                       Calendar creation,
                       Evenement newEvenement,
                       Utilisateur newUtilisateur,
                       String newCommentaire) {
        List<Commentaire> commentaireList = get(oldEvenement,
                oldUtilisateur,
                oldCommentaire,
                creation);

        if (commentaireList != null) {
            for (Commentaire commentaire : commentaireList) {
                if (newEvenement != null) {
                    commentaire.setEvenement(newEvenement);
                }

                if (newUtilisateur != null) {
                    commentaire.setUtilisateur(newUtilisateur);
                }

                if (newCommentaire != null) {
                    commentaire.setCommentaire(newCommentaire);
                }
            }

            DatabaseAccess.update(commentaireList);
        }
    }

    public void delete(Evenement evenement,
                       Utilisateur utilisateur,
                       String commentaire,
                       Calendar creation) {
        String nomEvenement = evenement != null ? evenement.getNomEvenement() : null;
        String nomUtilisateur = utilisateur != null ? utilisateur.getNomUtilisateur() : null;

        delete(nomEvenement, nomUtilisateur, commentaire, creation);
    }

    public void delete(String nomEvenement,
                       String nomUtilisateur,
                       String commentaire,
                       Calendar creation) {
        DatabaseAccess.delete(get(nomEvenement, nomUtilisateur, commentaire, creation));
    }
}
