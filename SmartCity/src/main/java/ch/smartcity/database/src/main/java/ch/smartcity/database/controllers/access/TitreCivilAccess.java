package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.TitreCivil;
import ch.smartcity.database.models.TitreCivil_;
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
 * Fournit l'accès aux titres civils de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class TitreCivilAccess {

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

    private TitreCivilAccess() {
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
    public static TitreCivilAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient la liste des titres civils stockés au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param titre       titre des titres civils à obtenir
     * @param abreviation abréviation des titres civils à obtenir
     * @return liste des titres civils stockés au sein de la base de données en fonction des
     * paramètres
     */
    public List<TitreCivil> get(String titre, String abreviation) {
        List<TitreCivil> titreCivilList = null;

        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                // Définit des critères de sélection pour la requête
                CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
                CriteriaQuery<TitreCivil> criteriaQuery = criteriaBuilder
                        .createQuery(TitreCivil.class);
                Root<TitreCivil> titreCivilRoot = criteriaQuery.from(TitreCivil.class);
                List<Predicate> predicateList = new ArrayList<>();

                // Définit seulement les critères de sélection pour la requête des paramètres non null
                // et non vide
                if (titre != null && !titre.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(titreCivilRoot.get(
                            TitreCivil_.titre),
                            titre.toLowerCase()));
                }

                if (abreviation != null && !abreviation.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(titreCivilRoot.get(
                            TitreCivil_.abreviation),
                            abreviation.toLowerCase()));
                }

                criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
                titreCivilList = hibernate.createQuery(criteriaQuery).getResultList();

                transaction.commit();
            }
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                titreCivilList != null ? titreCivilList.size() : 0,
                TitreCivil.class.getSimpleName()));

        return titreCivilList;
    }

    /**
     * Stocke le titre civil correspondant aux paramètres
     *
     * @param titre       titre du titre civil à stocker
     * @param abreviation abréviation du titre civil à stocker
     */
    public void save(String titre, String abreviation) {
        databaseAccess.save(new TitreCivil(titre, abreviation));
    }

    /**
     * Met à jour le titre civil correspondant aux paramètres
     *
     * @param idTitreCivil identifiant du titre civil à mettre à jour
     * @param titre        titre du titre civil à mettre à jour
     * @param abreviation  abreviation du titre civil à mettre à jour
     */
    public void update(Integer idTitreCivil, String titre, String abreviation) {
        TitreCivil titreCivil = databaseAccess.get(TitreCivil.class, idTitreCivil);

        // Vérifie si la requête a abouti
        if (titreCivil != null) {

            // Affecte les nouveaux attributs au titre civil
            setAll(titreCivil, titre, abreviation);
            databaseAccess.update(titreCivil);
        }
    }

    /**
     * Met à jour les titres civils correspondant aux paramètres préfixés de old en leur
     * affectant les paramètres préfixés de new
     * Chaque paramètre préfixés de old différent de null sera utilisé comme critère de recherche
     * Chaque paramètre préfixés de new de valeurs null ne se mettre pas à jour
     *
     * @param oldTitre       ancien titre des titres civils à mettre à jour
     * @param oldAbreviation ancienne abréviation des titres civils à mettre à jour
     * @param newTitre       nouveau titre des titres civils à mettre à jour
     * @param newAbreviation nouvelle abréviation des titres civils à mettre à jour
     */
    public void update(String oldTitre,
                       String oldAbreviation,
                       String newTitre,
                       String newAbreviation) {
        List<TitreCivil> titreCivilList = get(oldTitre, oldAbreviation);

        // Vérifie si la requête a abouti
        if (titreCivilList != null) {

            // Affecte les nouveaux attributs aux titres civils
            for (TitreCivil titreCivil : titreCivilList) {
                setAll(titreCivil, newTitre, newAbreviation);
            }

            databaseAccess.update(titreCivilList);
        }
    }

    /**
     * Supprime les titres civils correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param titre       titre des titres civils à supprimer
     * @param abreviation abréviation des titres civils à supprimer
     */
    public void delete(String titre, String abreviation) {
        databaseAccess.delete(get(titre, abreviation));
    }

    /**
     * Affecte les paramètres du titre civil si ils ne sont pas null
     *
     * @param titreCivil  titre civil
     * @param titre       titre du titre civil
     * @param abreviation abréviation du titre civil
     */
    private void setAll(TitreCivil titreCivil, String titre, String abreviation) {
        if (titre != null) {
            titreCivil.setTitre(titre);
        }

        if (abreviation != null) {
            titreCivil.setAbreviation(abreviation);
        }
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final TitreCivilAccess instance = new TitreCivilAccess();
    }
}
