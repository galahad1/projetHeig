package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.RubriqueParent;
import ch.smartcity.database.models.RubriqueParent_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Fournit l'accès aux rubriques parents de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class RubriqueParentAccess {

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

    private RubriqueParentAccess() {
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
    public static RubriqueParentAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient la liste des rubriques parents stockées au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomRubriqueParent nom des rubriques parents à obtenir
     * @return liste des rubriques parents stockées au sein de la base de données en fonction des
     * paramètres
     */
    public List<RubriqueParent> get(String nomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            // Démarre une transaction pour la gestion d'erreur
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            // Définit des critères de sélection pour la requête
            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<RubriqueParent> criteriaQuery = criteriaBuilder
                    .createQuery(RubriqueParent.class);
            Root<RubriqueParent> rubriqueParentRoot = criteriaQuery.from(RubriqueParent.class);
            List<Predicate> predicateList = new ArrayList<>();

            // Définit seulement les critères de sélection pour la requête des paramètres non null
            // et non vide
            if (nomRubriqueParent != null && !nomRubriqueParent.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(rubriqueParentRoot.get(
                        RubriqueParent_.nomRubriqueParent),
                        nomRubriqueParent.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            rubriqueParentList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        databaseAccess.close(session);

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                rubriqueParentList != null ? rubriqueParentList.size() : 0,
                RubriqueParent.class.getSimpleName()));

        return rubriqueParentList;
    }

    /**
     * Stocke la rubrique parent définit par les paramètres
     *
     * @param nomRubriqueParent nom de la rubrique parent à stocker
     */
    public void save(String nomRubriqueParent) {
        databaseAccess.save(new RubriqueParent(nomRubriqueParent));
    }

    /**
     * Met à jour la rubrique parent correspondant aux paramètres
     *
     * @param idRubriqueParent  identifiant de la rubrique parent à mettre à jour
     * @param nomRubriqueParent nom de la rubrique parent à mettre à jour
     */
    public void update(Integer idRubriqueParent, String nomRubriqueParent) {
        RubriqueParent rubriqueParent = databaseAccess.get(RubriqueParent.class, idRubriqueParent);

        // Vérifie si la requête a abouti
        if (rubriqueParent != null) {

            // Affecte les nouveaux attributs à la rubriqueParent
            setAll(rubriqueParent, nomRubriqueParent);
            databaseAccess.update(rubriqueParent);
        }
    }

    /**
     * Met à jour les rubriques parents correspondant aux paramètres préfixés de old en leur
     * affectant les paramètres préfixés de new
     * Chaque paramètre préfixés de old différent de null sera utilisé comme critère de recherche
     * Chaque paramètre préfixés de new de valeurs null ne se mettre pas à jour
     *
     * @param oldNomRubriqueParent ancien nom des rubriques parents à mettre à jour
     * @param newNomRubriqueParent nouveau nom des rubriques parents à mettre à jour
     */
    public void update(String oldNomRubriqueParent, String newNomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = get(oldNomRubriqueParent);

        // Vérifie si la requête a abouti
        if (rubriqueParentList != null) {

            // Affecte les nouveaux attributs aux rubriques parents
            for (RubriqueParent rubriqueParent : rubriqueParentList) {
                setAll(rubriqueParent, newNomRubriqueParent);
            }

            databaseAccess.update(rubriqueParentList);
        }
    }

    /**
     * Supprime les rubriques parents correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomRubriqueParent nom des rubriques parents à supprimer
     */
    public void delete(String nomRubriqueParent) {
        databaseAccess.delete(get(nomRubriqueParent));
    }

    /**
     * Affecte les paramètres de la rubrique parent si ils ne sont pas null
     *
     * @param rubriqueParent    rubrique parent
     * @param nomRubriqueParent nom de la rubrique parent
     */
    private void setAll(RubriqueParent rubriqueParent, String nomRubriqueParent) {
        if (nomRubriqueParent != null) {
            rubriqueParent.setNomRubriqueParent(nomRubriqueParent);
        }
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final RubriqueParentAccess instance = new RubriqueParentAccess();
    }
}
