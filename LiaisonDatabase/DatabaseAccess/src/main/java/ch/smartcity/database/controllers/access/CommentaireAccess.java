package database.controllers.access;

import database.controllers.ConfigurationManager;
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

class CommentaireAccess {

    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(CommentaireAccess.class.getName());
    }

    private String nomEvenement;
    private String nomUtilisateur;

    private void setAll(Commentaire objCommentaire,
                        Evenement evenement,
                        Utilisateur utilisateur,
                        String commentaire) {
        if (evenement != null) {
            objCommentaire.setEvenement(evenement);
        }

        if (utilisateur != null) {
            objCommentaire.setUtilisateur(utilisateur);
        }

        if (commentaire != null) {
            objCommentaire.setCommentaire(commentaire);
        }
    }

    private void checkNull(Evenement evenement, Utilisateur utilisateur) {
        nomEvenement = evenement != null ? evenement.getNomEvenement() : null;
        nomUtilisateur = utilisateur != null ? utilisateur.getNomUtilisateur() : null;
    }

    public List<Commentaire> get(Evenement evenement,
                                 Utilisateur utilisateur,
                                 String commentaire,
                                 Calendar creation) {
        checkNull(evenement, utilisateur);
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
            session = Hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = Hibernate.getCriteriaBuilder();
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
            commentaireList = Hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            DatabaseAccess.rollback(e, transaction);
        } finally {
            DatabaseAccess.close(session);
        }

        LOGGER.log(Level.INFO, String.format(
                ConfigurationManager.getString("databaseAccess.results"),
                commentaireList != null ? commentaireList.size() : 0,
                Commentaire.class.getSimpleName()));

        return commentaireList;
    }

    public void save(Evenement evenement, Utilisateur utilisateur, String commentaire) {
        DatabaseAccess.save(new Commentaire(new IdCommentaire(evenement, utilisateur), commentaire));
    }

    public void update(Integer idCommentaire,
                       Evenement evenement,
                       Utilisateur utilisateur,
                       String commentaire) {
        Commentaire objCommentaire = DatabaseAccess.get(Commentaire.class, idCommentaire);

        if (objCommentaire != null) {
            setAll(objCommentaire, evenement, utilisateur, commentaire);
            DatabaseAccess.update(objCommentaire);
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
                setAll(commentaire, newEvenement, newUtilisateur, newCommentaire);
            }

            DatabaseAccess.update(commentaireList);
        }
    }

    public void delete(Evenement evenement,
                       Utilisateur utilisateur,
                       String commentaire,
                       Calendar creation) {
        checkNull(evenement, utilisateur);
        delete(nomEvenement, nomUtilisateur, commentaire, creation);
    }

    public void delete(String nomEvenement,
                       String nomUtilisateur,
                       String commentaire,
                       Calendar creation) {
        DatabaseAccess.delete(get(nomEvenement, nomUtilisateur, commentaire, creation));
    }
}
