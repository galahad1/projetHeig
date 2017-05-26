package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.Statut;
import ch.smartcity.database.models.Statut_;
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
 * Fournit l'accès aux statuts de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class StatutAccess {

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

    private StatutAccess() {
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
    public static StatutAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient la liste des statuts stockés au sein de la base de données en fonction des paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomStatut nom des statuts à obtenir
     * @return liste des statuts stockés au sein de la base de données en fonction des paramètres
     */
    public List<Statut> get(String nomStatut) {
        List<Statut> statutList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            // Démarre une transaction pour la gestion d'erreur
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            // Définit des critères de sélection pour la requête
            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<Statut> criteriaQuery = criteriaBuilder
                    .createQuery(Statut.class);
            Root<Statut> statutRoot = criteriaQuery.from(Statut.class);
            List<Predicate> predicateList = new ArrayList<>();

            // Définit seulement les critères de sélection pour la requête des paramètres non null
            // et non vide
            if (nomStatut != null && !nomStatut.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(statutRoot.get(
                        Statut_.nomStatut),
                        nomStatut.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            statutList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        databaseAccess.close(session);

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                statutList != null ? statutList.size() : 0,
                Statut.class.getSimpleName()));

        return statutList;
    }

    /**
     * Stocke le statut définit par les paramètres
     *
     * @param nomStatut nom du statut à stocker
     */
    public void save(String nomStatut) {
        databaseAccess.save(new Statut(nomStatut));
    }

    /**
     * Met à jour le statut correspondant aux paramètres
     *
     * @param idStatut  identifiant du statut à mettre à jour
     * @param nomStatut nom du statut à mettre à jour
     */
    public void update(Integer idStatut, String nomStatut) {
        Statut statut = databaseAccess.get(Statut.class, idStatut);

        // Vérifie si la requête a abouti
        if (statut != null) {

            // Affecte les nouveaux attributs au statut
            setAll(statut, nomStatut);
            databaseAccess.update(statut);
        }
    }

    /**
     * Met à jour les statuts correspondant aux paramètres préfixés de old en leur
     * affectant les paramètres préfixés de new
     * Chaque paramètre préfixés de old différent de null sera utilisé comme critère de recherche
     * Chaque paramètre préfixés de new de valeurs null ne se mettre pas à jour
     *
     * @param oldNomStatut ancien nom des statuts à mettre à jour
     * @param newNomStatut nouveau nom des statuts à mettre à jour
     */
    public void update(String oldNomStatut, String newNomStatut) {
        List<Statut> statutList = get(oldNomStatut);

        // Vérifie si la requête a abouti
        if (statutList != null) {

            // Affecte les nouveaux attributs aux statuts
            for (Statut statut : statutList) {
                setAll(statut, newNomStatut);
            }

            databaseAccess.update(statutList);
        }
    }

    /**
     * Supprime les statuts correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomStatut nom des statuts à supprimer
     */
    public void delete(String nomStatut) {
        databaseAccess.delete(get(nomStatut));
    }

    /**
     * Affecte les paramètres du statut si ils ne sont pas null
     *
     * @param statut    statut
     * @param nomStatut nom du statut
     */
    private void setAll(Statut statut, String nomStatut) {
        if (nomStatut != null) {
            statut.setNomStatut(nomStatut);
        }
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final StatutAccess instance = new StatutAccess();
    }
}
