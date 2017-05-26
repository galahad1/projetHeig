package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.Rue;
import ch.smartcity.database.models.Rue_;
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
 * Fournit l'accès aux rues de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class RueAccess {

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

    private RueAccess() {
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
    public static RueAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient la liste des rues stockées au sein de la base de données en fonction des paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomRue nom des rues à obtenir
     * @return liste des rues stockées au sein de la base de données en fonction des paramètres
     */
    public List<Rue> get(String nomRue) {
        List<Rue> rueList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            // Démarre une transaction pour la gestion d'erreur
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            // Définit des critères de sélection pour la requête
            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<Rue> criteriaQuery = criteriaBuilder
                    .createQuery(Rue.class);
            Root<Rue> rueRoot = criteriaQuery.from(Rue.class);
            List<Predicate> predicateList = new ArrayList<>();

            // Définit seulement les critères de sélection pour la requête des paramètres non null
            // et non vide
            if (nomRue != null && !nomRue.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(rueRoot.get(
                        Rue_.nomRue),
                        nomRue.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            rueList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        databaseAccess.close(session);

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                rueList != null ? rueList.size() : 0,
                Rue.class.getSimpleName()));

        return rueList;
    }

    /**
     * Stocke la rue définit par les paramètres
     *
     * @param nomRue nom de la rue à stocker
     */
    public void save(String nomRue) {
        databaseAccess.save(new Rue(nomRue));
    }

    /**
     * Met à jour la rue correspondant aux paramètres
     *
     * @param idRue  identifiant de la rue à mettre à jour
     * @param nomRue nom de la rue à mettre à jour
     */
    public void update(Integer idRue, String nomRue) {
        Rue rue = databaseAccess.get(Rue.class, idRue);

        // Vérifie si la requête a abouti
        if (rue != null) {

            // Affecte les nouveaux attributs à la rue
            setAll(rue, nomRue);
            databaseAccess.update(rue);
        }
    }

    /**
     * Met à jour les rues correspondant aux paramètres préfixés de old en leur
     * affectant les paramètres préfixés de new
     * Chaque paramètre préfixés de old différent de null sera utilisé comme critère de recherche
     * Chaque paramètre préfixés de new de valeurs null ne se mettre pas à jour
     *
     * @param oldNomRue ancien nom des rues à mettre à jour
     * @param newNomRue nouveau nom des rues à mettre à jour
     */
    public void update(String oldNomRue, String newNomRue) {
        List<Rue> rueList = get(oldNomRue);

        // Vérifie si la requête a abouti
        if (rueList != null) {

            // Affecte les nouveaux attributs aux rues
            for (Rue rue : rueList) {
                setAll(rue, newNomRue);
            }

            databaseAccess.update(rueList);
        }
    }

    /**
     * Supprime les rues correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomRue nom des rues à supprimer
     */
    public void delete(String nomRue) {
        databaseAccess.delete(get(nomRue));
    }

    /**
     * Affecte les paramètres de la rue si ils ne sont pas null
     *
     * @param rue    rue
     * @param nomRue nom de la rue
     */
    private void setAll(Rue rue, String nomRue) {
        if (nomRue != null) {
            rue.setNomRue(nomRue);
        }
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final RueAccess instance = new RueAccess();
    }
}
