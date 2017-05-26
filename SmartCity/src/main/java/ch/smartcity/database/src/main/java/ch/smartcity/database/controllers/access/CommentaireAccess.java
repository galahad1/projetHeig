package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * Fournit l'accès aux commentaires de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class CommentaireAccess {

    /**
     * Utilisé pour accéder aux fichiers de propriétés
     */
    private final ConfigurationManager configurationManager;

    /**
     * Utilisé pour journaliser les actions effectuées
     */
    private final Logger logger;

    /**
     * Utilisé pour la connexion à la base de données
     */
    private final Hibernate hibernate;

    /**
     * Utilisé pour des accès génériques à la base de données
     */
    private final DatabaseAccess databaseAccess;

    /**
     * Utilisé pour stocker la valeur des attributs ci-dessous en fonction de la nullité des paramètres
     * d'un commentaire
     */
    private String nomEvenement;
    private String nomUtilisateur;

    private CommentaireAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
        databaseAccess = DatabaseAccess.getInstance();
    }

    /**
     * Fournit l'unique instance de la classe (singleton)
     *
     * @return unique instance de la classe
     */
    public static CommentaireAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient la liste des commentaires stockée au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param evenement   événement des commentaires à obtenir
     * @param utilisateur utilisateur des commentaires à obtenir
     * @param commentaire commentaire texte des commentaires à obtenir
     * @param creation    date de création des commentaires à otbenir
     * @return liste des commentaires stockée au sein de la base de données en fonction
     * des paramètres
     */
    public List<Commentaire> get(Evenement evenement,
                                 Utilisateur utilisateur,
                                 String commentaire,
                                 Calendar creation) {

        // Définit nomEvenement et nomUtilisateur en fonction de la valeurs des paramètres
        // evenement et utilisateur
        checkNull(evenement, utilisateur);
        return get(nomEvenement, nomUtilisateur, commentaire, creation);
    }

    /**
     * Obtient la liste des commentaires stockée au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomEvenement   nom de l'événement des commentaires à obtenir
     * @param nomUtilisateur nom de l'utilisateur des commentaires à obtenir
     * @param commentaire    commentaire texte des commentaires à obtenir
     * @param creation       date de création des commentaires à otbenir
     * @return liste des commentaires stockée au sein de la base de données en fonction
     * des paramètres
     */
    public List<Commentaire> get(String nomEvenement,
                                 String nomUtilisateur,
                                 String commentaire,
                                 Calendar creation) {
        List<Commentaire> commentaireList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            // Démarre une transaction pour la gestion d'erreur
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            // Définit des critères de sélection pour la requête
            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<Commentaire> criteriaQuery = criteriaBuilder
                    .createQuery(Commentaire.class);

            // Liaison avec différentes tables
            Root<Commentaire> commentaireRoot = criteriaQuery.from(Commentaire.class);
            Join<Commentaire, IdCommentaire> commentaireIdCommentaireJoin =
                    commentaireRoot.join(Commentaire_.idCommentaire);
            Join<IdCommentaire, Evenement> idCommentaireEvenementJoin =
                    commentaireIdCommentaireJoin.join(IdCommentaire_.evenement);
            Join<IdCommentaire, Utilisateur> idCommentaireUtilisateurJoin =
                    commentaireIdCommentaireJoin.join(IdCommentaire_.utilisateur);
            List<Predicate> predicateList = new ArrayList<>();

            // Définit seulement les critères de sélection pour la requête des paramètres non null
            // et non vide
            if (nomEvenement != null && !nomEvenement.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        idCommentaireEvenementJoin.get(Evenement_.nomEvenement),
                        nomEvenement.toLowerCase()));
            }

            if (nomUtilisateur != null && !nomUtilisateur.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        idCommentaireUtilisateurJoin.get(Utilisateur_.nomUtilisateur),
                        nomUtilisateur.toLowerCase()));
            }

            if (commentaire != null && !commentaire.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        commentaireRoot.get(Commentaire_.commentaire), commentaire.toLowerCase()));
            }

            if (creation != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(
                        commentaireRoot.get(Commentaire_.creation), creation));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            commentaireList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        databaseAccess.close(session);

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                commentaireList != null ? commentaireList.size() : 0,
                Commentaire.class.getSimpleName()));

        return commentaireList;
    }

    /**
     * Stocke le commentaire définit par l'événement, l'utilisateur et le commentaire texte
     *
     * @param evenement   événement du commentaire à stocker
     * @param utilisateur utilisateur du commentaire à stocker
     * @param commentaire commentaire texte du commentaire à stocker
     */
    public void save(Evenement evenement, Utilisateur utilisateur, String commentaire) {
        databaseAccess.save(new Commentaire(new IdCommentaire(evenement, utilisateur), commentaire));
    }

    /**
     * Met à jour le commentaire correspondant à l'identifiant avec les paramètres fournis
     * Chaque paramètre de valeurs null ne se mettre pas à jour
     *
     * @param idCommentaire identifiant du commentaire à mettre à jour
     * @param evenement     événement du commentaire à mettre à jour
     * @param utilisateur   utilisateur du commentaire à mettre à jour
     * @param commentaire   commentaire texte du commentaire à mettre à jour
     */
    public void update(Integer idCommentaire,
                       Evenement evenement,
                       Utilisateur utilisateur,
                       String commentaire) {
        Commentaire objCommentaire = databaseAccess.get(Commentaire.class, idCommentaire);

        // Vérifie si la requête a abouti
        if (objCommentaire != null) {

            // Affecte les nouveaux attributs au commentaire
            setAll(objCommentaire, evenement, utilisateur, commentaire);
            databaseAccess.update(objCommentaire);
        }
    }

    /**
     * Met à jour les commentaires avec les paramètres préfixés de new et correspondant aux
     * paramètres préfixés de old
     * Chaque paramètre préfixés de old différent de null sera utilisé comme critère de recherche
     * Chaque paramètre préfixés de new de valeurs null ne se mettre pas à jour
     *
     * @param oldEvenement   ancien événement des commentaires à mettre à jour
     * @param oldUtilisateur ancien utilisateur des commentaires à mettre à jour
     * @param oldCommentaire ancien commentaire texte des commentaires à mettre à jour
     * @param creation       date de création des commentaires à mettre à jour
     * @param newEvenement   nouveau événement des commentaires à mettre à jour
     * @param newUtilisateur nouveau utilisateur des commentaires à mettre à jour
     * @param newCommentaire nouveau commentaire texte des commentaires à mettre à jour
     */
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

        // Vérifie si la requête a abouti
        if (commentaireList != null) {

            // Affecte les nouveaux attributs aux commentaires
            for (Commentaire commentaire : commentaireList) {
                setAll(commentaire, newEvenement, newUtilisateur, newCommentaire);
            }

            databaseAccess.update(commentaireList);
        }
    }

    /**
     * Supprime les commentaires correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param evenement   événement des commentaires à supprimer
     * @param utilisateur utilisateur des commentaires à supprimer
     * @param commentaire commentaire texte des commentaires à supprimer
     * @param creation    date de création des commentaires à supprimer
     */
    public void delete(Evenement evenement,
                       Utilisateur utilisateur,
                       String commentaire,
                       Calendar creation) {

        // Définit nomEvenement et nomUtilisateur en fonction de la valeurs des paramètres evenement
        // et utilisateur
        checkNull(evenement, utilisateur);
        delete(nomEvenement, nomUtilisateur, commentaire, creation);
    }

    /**
     * Supprime les commentaires correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomEvenement   nom de l'événement des commentaires à supprimer
     * @param nomUtilisateur nom de l'utilisateur des commentaires à supprimer
     * @param commentaire    commentaire texte des commentaires à supprimer
     * @param creation       date de création des commentaires à supprimer
     */
    public void delete(String nomEvenement,
                       String nomUtilisateur,
                       String commentaire,
                       Calendar creation) {
        databaseAccess.delete(get(nomEvenement, nomUtilisateur, commentaire, creation));
    }

    /**
     * Affecte les paramètres au commentaire si ils ne sont pas null
     *
     * @param commentaire    commentaire dont il faut définir les paramètres
     * @param evenement      événement du commenaire
     * @param utilisateur    utilisateur du commenaire
     * @param strCommentaire commentaire texte du commenaire
     */
    private void setAll(Commentaire commentaire,
                        Evenement evenement,
                        Utilisateur utilisateur,
                        String strCommentaire) {
        if (evenement != null) {
            commentaire.setEvenement(evenement);
        }

        if (utilisateur != null) {
            commentaire.setUtilisateur(utilisateur);
        }

        if (strCommentaire != null) {
            commentaire.setCommentaire(strCommentaire);
        }
    }

    /**
     * Définit nomEvenement et nomUtilisateur en fonction de la nullité des paramètres
     *
     * @param evenement   evenement à vérifier
     * @param utilisateur utilisateur à vérifier
     */
    private void checkNull(Evenement evenement, Utilisateur utilisateur) {
        nomEvenement = evenement != null ? evenement.getNomEvenement() : null;
        nomUtilisateur = utilisateur != null ? utilisateur.getNomUtilisateur() : null;
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final CommentaireAccess instance = new CommentaireAccess();
    }
}
