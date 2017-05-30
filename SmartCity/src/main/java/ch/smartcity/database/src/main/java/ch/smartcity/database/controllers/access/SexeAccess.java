package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.Sexe;
import ch.smartcity.database.models.Sexe_;
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
 * Fournit l'accès aux sexes de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class SexeAccess {

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

    private SexeAccess() {
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
    public static SexeAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient la liste des sexes stockés au sein de la base de données en fonction des paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomSexe nom des sexes à obtenir
     * @return liste des sexes stockés au sein de la base de données en fonction des paramètres
     */
    public List<Sexe> get(String nomSexe) {
        List<Sexe> sexeList = null;

        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                // Définit des critères de sélection pour la requête
                CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
                CriteriaQuery<Sexe> criteriaQuery = criteriaBuilder
                        .createQuery(Sexe.class);
                Root<Sexe> sexeRoot = criteriaQuery.from(Sexe.class);
                List<Predicate> predicateList = new ArrayList<>();

                // Définit seulement les critères de sélection pour la requête des paramètres non null
                // et non vide
                if (nomSexe != null && !nomSexe.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(sexeRoot.get(
                            Sexe_.nomSexe),
                            nomSexe.toLowerCase()));
                }

                criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
                sexeList = hibernate.createQuery(criteriaQuery).getResultList();

                transaction.commit();
            }
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                sexeList != null ? sexeList.size() : 0,
                Sexe.class.getSimpleName()));

        return sexeList;
    }

    /**
     * Stocke le sexe définit par les paramètres
     *
     * @param nomSexe nom du sexe à stocker
     */
    public void save(String nomSexe) {
        databaseAccess.save(new Sexe(nomSexe));
    }

    /**
     * Met à jour le sexe correspondant aux paramètres
     *
     * @param idSexe  identifiant du sexe à mettre à jour
     * @param nomSexe nom du sexe à mettre à jour
     */
    public void update(Integer idSexe, String nomSexe) {
        Sexe sexe = databaseAccess.get(Sexe.class, idSexe);

        // Vérifie si la requête a abouti
        if (sexe != null) {

            // Affecte les nouveaux attributs au sexe
            setAll(sexe, nomSexe);
            databaseAccess.update(sexe);
        }
    }

    /**
     * Met à jour les sexes correspondant aux paramètres préfixés de old en leur
     * affectant les paramètres préfixés de new
     * Chaque paramètre préfixés de old différent de null sera utilisé comme critère de recherche
     * Chaque paramètre préfixés de new de valeurs null ne se mettre pas à jour
     *
     * @param oldNomSexe ancien nom des sexes à mettre à jour
     * @param newNomSexe nouveau nom des sexes à mettre à jour
     */
    public void update(String oldNomSexe, String newNomSexe) {
        List<Sexe> sexeList = get(oldNomSexe);

        // Vérifie si la requête a abouti
        if (sexeList != null) {

            // Affecte les nouveaux attributs aux sexes
            for (Sexe sexe : sexeList) {
                setAll(sexe, newNomSexe);
            }

            databaseAccess.update(sexeList);
        }
    }

    /**
     * Supprime les sexes correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomSexe nom des sexes à supprimer
     */
    public void delete(String nomSexe) {
        databaseAccess.delete(get(nomSexe));
    }

    /**
     * Affecte les paramètres du sexe si ils ne sont pas null
     *
     * @param sexe    sexe
     * @param nomSexe nom du sexe
     */
    private void setAll(Sexe sexe, String nomSexe) {
        if (nomSexe != null) {
            sexe.setNomSexe(nomSexe);
        }
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final SexeAccess instance = new SexeAccess();
    }
}
