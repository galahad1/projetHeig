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
 * Fournit l'accès aux liens de confiances de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class ConfianceAccess {

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
     * Utilisé pour définir les paramètres de la requête en fonction de la valeurs des paramètres
     * d'un lien de confiance
     */
    private String nomUtilisateur;
    private String nomRubriqueEnfant;

    private ConfianceAccess() {
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
    public ConfianceAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient la liste des liens de confiance stockés au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param utilisateur    utilisateur des liens de confiance à obtenir
     * @param rubriqueEnfant rubrique enfant des liens de confiance à obtenir
     * @param creation       date de création des liens de confiance à obtenir
     * @return liste des liens de confiance stockés au sein de la base de données en fonction des
     * paramètres
     */
    public List<Confiance> get(Utilisateur utilisateur,
                               RubriqueEnfant rubriqueEnfant,
                               Calendar creation) {

        // Définit les paramètres de la requête en fonction de la valeurs des paramètres du
        // lien de confiance
        checkNull(utilisateur, rubriqueEnfant);
        return get(nomUtilisateur, nomRubriqueEnfant, creation);
    }

    /**
     * Obtient la liste des liens de confiance stockés au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomUtilisateur    nom de l'utilisateur des liens de confiance à obtenir
     * @param nomRubriqueEnfant nom de la rubrique enfant des liens de confiance à obtenir
     * @param creation          date de création des liens de confiance à obtenir
     * @return liste des liens de confiance stockés au sein de la base de données en fonction des
     * paramètres
     */
    public List<Confiance> get(String nomUtilisateur,
                               String nomRubriqueEnfant,
                               Calendar creation) {
        List<Confiance> confianceList = null;

        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                // Définit des critères de sélection pour la requête
                CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
                CriteriaQuery<Confiance> criteriaQuery = criteriaBuilder.createQuery(Confiance.class);

                // Liaison avec différentes tables
                Root<Confiance> confianceRoot = criteriaQuery.from(Confiance.class);
                Join<Confiance, IdConfiance> confianceIdConfianceJoin =
                        confianceRoot.join(Confiance_.idConfiance);
                Join<IdConfiance, Utilisateur> idConfianceUtilisateurJoin =
                        confianceIdConfianceJoin.join(IdConfiance_.utilisateur);
                Join<IdConfiance, RubriqueEnfant> idConfianceRubriqueEnfantJoin =
                        confianceIdConfianceJoin.join(IdConfiance_.rubriqueEnfant);
                List<Predicate> predicateList = new ArrayList<>();

                // Définit seulement les critères de sélection pour la requête des paramètres non null
                // et non vide
                if (nomUtilisateur != null && !nomUtilisateur.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(
                            idConfianceUtilisateurJoin.get(Utilisateur_.nomUtilisateur),
                            nomUtilisateur.toLowerCase()));
                }

                if (nomRubriqueEnfant != null && !nomRubriqueEnfant.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(
                            idConfianceRubriqueEnfantJoin.get(RubriqueEnfant_.nomRubriqueEnfant),
                            nomRubriqueEnfant.toLowerCase()));
                }

                if (creation != null) {
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(
                            confianceRoot.get(Confiance_.creation),
                            creation));
                }

                criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
                confianceList = hibernate.createQuery(criteriaQuery).getResultList();

                transaction.commit();
            }
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                confianceList != null ? confianceList.size() : 0,
                Confiance.class.getSimpleName()));

        return confianceList;
    }

    /**
     * Stocke le lien de confiance définit par les paramètres
     *
     * @param utilisateur    utilisateur du lien de confiance à stocker
     * @param rubriqueEnfant rubriqueEnfant du lien de confiance à stocker
     */
    public void save(Utilisateur utilisateur, RubriqueEnfant rubriqueEnfant) {
        databaseAccess.save(new Confiance(new IdConfiance(utilisateur, rubriqueEnfant)));
    }

    /**
     * Met à jour le lien de confiance correspondant aux paramètres
     * Chaque paramètre de valeurs null ne se mettre pas à jour
     *
     * @param idConfiance    identifiant du lien de confiance à mettre à jour
     * @param utilisateur    utilisateur du lien de confiance à mettre à jour
     * @param rubriqueEnfant événement du lien de confiance à mettre à jour
     */
    public void update(Integer idConfiance,
                       Utilisateur utilisateur,
                       RubriqueEnfant rubriqueEnfant) {
        Confiance confiance = databaseAccess.get(Confiance.class, idConfiance);

        // Vérifie si la requête a abouti
        if (confiance != null) {

            // Affecte les nouveaux attributs au lien de confiance
            setAll(confiance, utilisateur, rubriqueEnfant);
            databaseAccess.update(confiance);
        }
    }

    /**
     * Met à jour les liens de confiance correspondant aux paramètres préfixés de old en leur
     * affectant les paramètres préfixés de new
     * Chaque paramètre préfixés de old différent de null sera utilisé comme critère de recherche
     * Chaque paramètre préfixés de new de valeurs null ne se mettre pas à jour
     *
     * @param oldUtilisateur    ancien utilisateur des liens de confiance à mettre à jour
     * @param oldRubriqueEnfant ancien événement des liens de confiance à mettre à jour
     * @param creation          date de création des liens de confiance à mettre à jour
     * @param newUtilisateur    nouvel utilisateur des liens de confiance à mettre à jour
     * @param newRubriqueEnfant nouvelle rubrique enfant des liens de confiance à mettre à jour
     */
    public void update(Utilisateur oldUtilisateur,
                       RubriqueEnfant oldRubriqueEnfant,
                       Calendar creation,
                       Utilisateur newUtilisateur,
                       RubriqueEnfant newRubriqueEnfant) {
        List<Confiance> confianceList = get(oldUtilisateur, oldRubriqueEnfant, creation);

        // Vérifie si la requête a abouti
        if (confianceList != null) {

            // Affecte les nouveaux attributs aux liens de confiance
            for (Confiance confiance : confianceList) {
                setAll(confiance, newUtilisateur, newRubriqueEnfant);
            }

            databaseAccess.update(confianceList);
        }
    }

    /**
     * Supprime les liens de confiance correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param utilisateur    utilisateur des liens de confiance à supprimer
     * @param rubriqueEnfant rubrique enfant des liens de confiance à supprimer
     * @param creation       date de création des liens de confiance à supprimer
     */
    public void delete(Utilisateur utilisateur,
                       RubriqueEnfant rubriqueEnfant,
                       Calendar creation) {

        // Définit les paramètres de la requête en fonction de la valeurs des paramètres du
        // lien de confiance
        checkNull(utilisateur, rubriqueEnfant);
        delete(nomUtilisateur, nomRubriqueEnfant, creation);
    }

    /**
     * Supprime les liens de confiance correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomUtilisateur    nom de l'utilisateur des liens de confiance à supprimer
     * @param nomRubriqueEnfant nom de la rubrique enfant des liens de confiance à supprimer
     * @param creation          date de création des liens de confiance à supprimer
     */
    public void delete(String nomUtilisateur,
                       String nomRubriqueEnfant,
                       Calendar creation) {
        databaseAccess.delete(get(nomUtilisateur, nomRubriqueEnfant, creation));
    }

    /**
     * Affecte les paramètres du lien de confiance si ils ne sont pas null
     *
     * @param confiance      lien de confiance dont il faut définir les paramètres
     * @param utilisateur    utilisateur du lien de confiance
     * @param rubriqueEnfant rubrique enfant du lien de confiance
     */
    private void setAll(Confiance confiance,
                        Utilisateur utilisateur,
                        RubriqueEnfant rubriqueEnfant) {
        if (utilisateur != null) {
            confiance.setUtilisateur(utilisateur);
        }

        if (rubriqueEnfant != null) {
            confiance.setRubriqueEnfant(rubriqueEnfant);
        }
    }

    /**
     * Définit les paramètres de la requête en fonction de la valeurs des paramètres du
     * lien de confiance
     *
     * @param utilisateur    utilisateur à vérifier
     * @param rubriqueEnfant rubrique enfant à vérifier
     */
    private void checkNull(Utilisateur utilisateur, RubriqueEnfant rubriqueEnfant) {
        nomUtilisateur = utilisateur != null ? utilisateur.getNomUtilisateur() : null;
        nomRubriqueEnfant = rubriqueEnfant != null ? rubriqueEnfant.getNomRubriqueEnfant() : null;
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final ConfianceAccess instance = new ConfianceAccess();
    }
}
