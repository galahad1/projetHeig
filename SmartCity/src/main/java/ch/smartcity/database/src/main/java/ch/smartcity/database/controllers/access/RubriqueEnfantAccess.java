package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.RubriqueEnfant;
import ch.smartcity.database.models.RubriqueEnfant_;
import ch.smartcity.database.models.RubriqueParent;
import ch.smartcity.database.models.RubriqueParent_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Fournit l'accès aux rubriques enfants de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class RubriqueEnfantAccess {

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

    private String nomRubriqueParent;

    private RubriqueEnfantAccess() {
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
    public static RubriqueEnfantAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient la liste des rubriques enfants stockées au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param rubriqueParent    rubrique parent des rubriques enfants à obtenir
     * @param nomRubriqueEnfant nom des rubriques enfants à obtenir
     * @return liste des rubriques enfants stockées au sein de la base de données en fonction des
     * paramètres
     */
    public List<RubriqueEnfant> get(RubriqueParent rubriqueParent, String nomRubriqueEnfant) {
        checkNull(rubriqueParent);
        return get(nomRubriqueParent, nomRubriqueEnfant);
    }

    /**
     * Obtient la liste des rubriques enfants stockées au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomRubriqueParent nom de la rubrique parent des rubriques enfants à obtenir
     * @param nomRubriqueEnfant nom des rubriques enfants à obtenir
     * @return liste des rubriques enfants stockées au sein de la base de données en fonction des
     * paramètres
     */
    public List<RubriqueEnfant> get(String nomRubriqueParent, String nomRubriqueEnfant) {
        List<RubriqueEnfant> rubriqueEnfantList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            // Démarre une transaction pour la gestion d'erreur
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            // Définit des critères de sélection pour la requête
            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<RubriqueEnfant> criteriaQuery = criteriaBuilder
                    .createQuery(RubriqueEnfant.class);

            // Liaison avec différentes tables
            Root<RubriqueEnfant> rubriqueEnfantRoot = criteriaQuery.from(RubriqueEnfant.class);
            Join<RubriqueEnfant, RubriqueParent> rubriqueEnfantRubriqueParentJoin =
                    rubriqueEnfantRoot.join(RubriqueEnfant_.rubriqueParent);
            List<Predicate> predicateList = new ArrayList<>();

            // Définit seulement les critères de sélection pour la requête des paramètres non null
            // et non vide
            if (nomRubriqueParent != null && !nomRubriqueParent.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(rubriqueEnfantRubriqueParentJoin.get(
                        RubriqueParent_.nomRubriqueParent),
                        nomRubriqueParent.toLowerCase()));
            }

            if (nomRubriqueEnfant != null && !nomRubriqueEnfant.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(rubriqueEnfantRoot.get(
                        RubriqueEnfant_.nomRubriqueEnfant),
                        nomRubriqueEnfant.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            rubriqueEnfantList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        databaseAccess.close(session);

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                rubriqueEnfantList != null ? rubriqueEnfantList.size() : 0,
                RubriqueEnfant.class.getSimpleName()));

        return rubriqueEnfantList;
    }

    /**
     * Stocke la rubrique enfant définit par les paramètres
     *
     * @param rubriqueParent    rubrique parent de la rubrique enfant à stocker
     * @param nomRubriqueEnfant nom de la rubrique enfant à stocker
     */
    public void save(RubriqueParent rubriqueParent, String nomRubriqueEnfant) {
        databaseAccess.save(new RubriqueEnfant(rubriqueParent, nomRubriqueEnfant));
    }

    /**
     * Met à jour la rubrique enfant correspondant aux paramètres
     *
     * @param idRubriqueEnfant  identifiant de la rubrique enfant à mettre à jour
     * @param rubriqueParent    rurbique parent de la rubrique enfant à mettre à jour
     * @param nomRubriqueEnfant nom de la rubrique enfant à mettre à jour
     */
    public void update(Integer idRubriqueEnfant,
                       RubriqueParent rubriqueParent,
                       String nomRubriqueEnfant) {
        RubriqueEnfant rubriqueEnfant = databaseAccess.get(RubriqueEnfant.class, idRubriqueEnfant);

        // Vérifie si la requête a abouti
        if (rubriqueEnfant != null) {

            // Affecte les nouveaux attributs à la rubrique enfant
            setAll(rubriqueEnfant, rubriqueParent, nomRubriqueEnfant);
            databaseAccess.update(rubriqueEnfant);
        }
    }

    /**
     * Met à jour les rubriques enfants correspondant aux paramètres préfixés de old en leur
     * affectant les paramètres préfixés de new
     * Chaque paramètre préfixés de old différent de null sera utilisé comme critère de recherche
     * Chaque paramètre préfixés de new de valeurs null ne se mettre pas à jour
     *
     * @param oldRubriqueParent    ancienne rubrique parent des rubriques enfants à mettre à jour
     * @param oldNomRubriqueEnfant ancien nom des rubriques enfants à mettre à jour
     * @param newRubriqueParent    nouvelle rubrique parent des rubriques enfants à mettre à jour
     * @param newNomRubriqueEnfant nouveau nom des rubriques enfants à mettre à jour
     */
    public void update(RubriqueParent oldRubriqueParent,
                       String oldNomRubriqueEnfant,
                       RubriqueParent newRubriqueParent,
                       String newNomRubriqueEnfant) {
        List<RubriqueEnfant> rubriqueEnfantList = get(oldRubriqueParent, oldNomRubriqueEnfant);

        // Vérifie si la requête a abouti
        if (rubriqueEnfantList != null) {

            // Affecte les nouveaux attributs aux rubriques enfants
            for (RubriqueEnfant rubriqueEnfant : rubriqueEnfantList) {
                setAll(rubriqueEnfant, newRubriqueParent, newNomRubriqueEnfant);
            }

            databaseAccess.update(rubriqueEnfantList);
        }
    }

    /**
     * Supprime les rubriques enfants correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param rubriqueParent    rurbique parent des rubriques enfants à supprimer
     * @param nomRubriqueEnfant nom des rubriques enfants à supprimer
     */
    public void delete(RubriqueParent rubriqueParent,
                       String nomRubriqueEnfant) {

        // Définit nomRubriqueParent en fonction de la valeurs du paramètre nomRubriqueEnfant
        checkNull(rubriqueParent);
        delete(nomRubriqueParent, nomRubriqueEnfant);
    }

    public void delete(String nomRubriqueParent,
                       String nomRubriqueEnfant) {
        databaseAccess.delete(get(nomRubriqueParent, nomRubriqueEnfant));
    }

    /**
     * Affecte les paramètres de la rubrique enfant si ils ne sont pas null
     *
     * @param rubriqueEnfant    rubrique enfant dont il faut définir les paramètres
     * @param rubriqueParent    rubrique parent
     * @param nomRubriqueEnfant nom de la rubrique enfant
     */
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

    /**
     * Définit les paramètres de la requête en fonction de la valeurs des paramètres de la
     * rubrique enfant
     *
     * @param rubriqueParent rubrique parent à vérifier
     */
    private void checkNull(RubriqueParent rubriqueParent) {
        nomRubriqueParent = rubriqueParent != null ? rubriqueParent.getNomRubriqueParent() : null;
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final RubriqueEnfantAccess instance = new RubriqueEnfantAccess();
    }
}
