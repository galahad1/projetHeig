package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Fournit l'accès aux adresses de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class AdresseAccess {

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
     * Utilisé pour définir les paramètres de la requête en fonction de la valeurs des paramètres de
     * l'adresse
     */
    private String nomRue;
    private String numeroNpa;

    private AdresseAccess() {
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
    public static AdresseAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient la liste des adresses stockées au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param rue         rue des adresses à obtenir
     * @param numeroDeRue numéro de la rue des adresses à obtenir
     * @param npa         npa des adresses à obtenir
     * @return liste des adresses stockées au sein de la base de données en fonction des paramètres
     */
    public List<Adresse> get(Rue rue, String numeroDeRue, Npa npa) {

        // Définit les paramètres de la requête en fonction de la valeurs des paramètres de
        // l'adresse
        checkNull(rue, npa);
        return get(nomRue, numeroDeRue, numeroNpa);
    }

    /**
     * Obtient la liste des adresses stockées au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomRue      nom de la rue des adresses à obtenir
     * @param numeroDeRue numéro de la rue des adresses à obtenir
     * @param numeroNpa   numéro npa des adresses à obtenir
     * @return liste des adresses stockées au sein de la base de données en fonction des paramètres
     */
    public List<Adresse> get(String nomRue, String numeroDeRue, String numeroNpa) {
        List<Adresse> adresseList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            // Démarre une transaction pour la gestion d'erreur
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            // Définit des critères de sélection pour la requête
            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<Adresse> criteriaQuery = criteriaBuilder.createQuery(Adresse.class);

            // Liaison avec différentes tables
            Root<Adresse> adresseRoot = criteriaQuery.from(Adresse.class);
            Join<Adresse, Rue> adresseRueJoin = adresseRoot.join(Adresse_.rue);
            Join<Adresse, Npa> adresseNpaJoin = adresseRoot.join(Adresse_.npa);
            List<Predicate> predicateList = new ArrayList<>();

            // Définit seulement les critères de sélection pour la requête des paramètres non null
            // et non vide
            if (nomRue != null && !nomRue.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        adresseRueJoin.get(Rue_.nomRue),
                        nomRue.toLowerCase()));
            }

            if (numeroDeRue != null && !numeroDeRue.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        adresseRoot.get(Adresse_.numeroDeRue),
                        numeroDeRue.toLowerCase()));
            }

            if (numeroNpa != null && !numeroNpa.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        adresseNpaJoin.get(Npa_.numeroNpa),
                        numeroNpa.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            adresseList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        databaseAccess.close(session);

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                adresseList != null ? adresseList.size() : 0,
                Adresse.class.getSimpleName()));

        return adresseList;
    }

    /**
     * Stocke l'adresse définit par les paramètres
     *
     * @param rue         rue de l'adresse à stocker
     * @param numeroDeRue numéro de rue de l'adresse à stocker
     * @param npa         npa de l'adresse à stocker
     */
    public void save(Rue rue, String numeroDeRue, Npa npa) {
        databaseAccess.save(new Adresse(rue, numeroDeRue, npa));
    }

    /**
     * Met à jour l'adresse correspondant aux paramètres
     * Chaque paramètre de valeurs null ne se mettre pas à jour
     *
     * @param idAdresse   identifiant de l'adresse à mettre à jour
     * @param rue         nouvelle rue de l'adresse à mettre à jour
     * @param numeroDeRue nouveau numéro de rue de l'adresse à mettre à jour
     * @param npa         nouveau npa de l'adresse à mettre à jour
     */
    public void update(Integer idAdresse, Rue rue, String numeroDeRue, Npa npa) {
        Adresse adresse = databaseAccess.get(Adresse.class, idAdresse);

        // Vérifie si la requête a abouti
        if (adresse != null) {

            // Affecte les nouveaux attributs à l'adresse
            setAll(adresse, rue, numeroDeRue, npa);
            databaseAccess.update(adresse);
        }
    }

    /**
     * Met à jour les adresses correspondant aux paramètres préfixés de old en leur affectant les
     * paramètres préfixés de new
     * Chaque paramètre préfixés de old différent de null sera utilisé comme critère de recherche
     * Chaque paramètre préfixés de new de valeurs null ne se mettre pas à jour
     *
     * @param oldRue         ancienne rue des adresses à mettre à jour
     * @param oldNumeroDeRue ancien numéro de rue des adresses à mettre à jour
     * @param oldNpa         ancien npa des adresses à mettre à jour
     * @param newRue         nouvelle rue des adresses à mettre à jour
     * @param newNumeroDeRue nouveau numéro de rue des adresses à mettre à jour
     * @param newNpa         nouveau npa des adresses à mettre à jour
     */
    public void update(Rue oldRue,
                       String oldNumeroDeRue,
                       Npa oldNpa,
                       Rue newRue,
                       String newNumeroDeRue,
                       Npa newNpa) {
        List<Adresse> adresseList = get(oldRue, oldNumeroDeRue, oldNpa);

        // Vérifie si la requête a abouti
        if (adresseList != null) {

            // Affecte les nouveaux attributs aux adresses
            for (Adresse adresse : adresseList) {
                setAll(adresse, newRue, newNumeroDeRue, newNpa);
            }

            databaseAccess.update(adresseList);
        }
    }

    /**
     * Supprime les adresses correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param rue         rue de des adresses à supprimer
     * @param numeroDeRue numéro de rue des adresses à supprimer
     * @param npa         npa des adresses à supprimer
     */
    public void delete(Rue rue, String numeroDeRue, Npa npa) {

        // Définit les paramètres de la requête en fonction de la valeurs des paramètres de
        // l'adresse
        checkNull(rue, npa);
        delete(nomRue, numeroDeRue, numeroNpa);
    }

    /**
     * Supprime les adresses correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomRue      nom de la rue des adresses à supprimer
     * @param numeroDeRue numéro de rue des adresses à supprimer
     * @param numeroNpa   numéro npa des adresses à supprimer
     */
    public void delete(String nomRue, String numeroDeRue, String numeroNpa) {
        databaseAccess.delete(get(nomRue, numeroDeRue, numeroNpa));
    }

    /**
     * Affecte les paramètres à l'adresse si ils ne sont pas null
     *
     * @param adresse     adresse dont il faut définir les paramètres
     * @param rue         rue de l'adresse
     * @param numeroDeRue numéro de rue de l'adresse
     * @param npa         npa de l'adresse
     */
    private void setAll(Adresse adresse, Rue rue, String numeroDeRue, Npa npa) {
        if (rue != null) {
            adresse.setRue(rue);
        }

        if (numeroDeRue != null) {
            adresse.setNumeroDeRue(numeroDeRue);
        }

        if (npa != null) {
            adresse.setNpa(npa);
        }
    }

    /**
     * Définit les paramètres de la requête en fonction de la valeurs des paramètres de l'adresse
     *
     * @param rue rue à vérifier
     * @param npa npa à vérifier
     */
    private void checkNull(Rue rue, Npa npa) {
        nomRue = rue != null ? rue.getNomRue() : null;
        numeroNpa = npa != null ? npa.getNumeroNpa() : null;
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final AdresseAccess instance = new AdresseAccess();
    }
}
