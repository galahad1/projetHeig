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
 * Fournit l'accès aux événements de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class EvenementAccess {

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
     * d'un événement
     */
    private String nomRubriqueEnfant;
    private String nomUtilisateur;
    private String nomRue;
    private String numeroDeRue;
    private String numeroNpa;
    private String nomPriorite;
    private String nomStatut;

    private EvenementAccess() {
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
    public static EvenementAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient la liste des événements actif, c'est à dire, les événements possédant le statut
     * Statut_.TRAITE et une date de fin inférieure ou égale à la date courante
     *
     * @return liste des événements actif en fonction des paramètres de sélection
     */
    public List<Evenement> getActif() {
        return get(null, null, Calendar.getInstance(), Statut_.TRAITE);
    }

    /**
     * Obtient la liste des événements actif, c'est à dire, les événements possédant un nom de la
     * rubrique enfant fournit, une date de fin inférieure ou égale à la date fournit et le statut
     * Statut_.TRAITE
     *
     * @param nomRubriqueEnfant nom de la rubrique enfant des événements à obtenir
     * @param date              date de référence des événements à obtenir
     * @return liste des événements actif en fonction des paramètres de sélection
     */
    public List<Evenement> getActif(String nomRubriqueEnfant, Calendar date) {
        return get(nomRubriqueEnfant, date, date, Statut_.TRAITE);
    }

    /**
     * Obtient la liste des événements attente, c'est à dire, les événements possédant le statut
     * Statut_.EN_ATTENTE et une date de fin inférieure ou égale à la date courante
     *
     * @return liste des événements en attente en fonction des paramètres de sélection
     */
    public List<Evenement> getEnAttente() {
        return get(null, null, Calendar.getInstance(), Statut_.EN_ATTENTE);
    }

    /**
     * Obtient la liste des événements en attente, c'est à dire, les événements possédant un nom de la
     * rubrique enfant fournit, une date de fin inférieure ou égale à la date fournit et le statut
     * Statut_.EN_ATTENTE
     *
     * @param nomRubriqueEnfant nom de la rubrique enfant des événements à obtenir
     * @param date              date de référence des événements à obtenir
     * @return liste des événements en attente en fonction des paramètres de sélection
     */
    public List<Evenement> getEnAttente(String nomRubriqueEnfant, Calendar date) {
        return get(nomRubriqueEnfant, date, date, Statut_.EN_ATTENTE);
    }

    /**
     * Obtient la liste des événements stockés au sein de la base de données en fonction des
     * paramètres
     *
     * @param nomRubriqueEnfant nom de la rubrique enfant des événements à obtenir
     * @return liste des événements stockés au sein de la base de données en fonction des
     * paramètres
     */
    public List<Evenement> get(String nomRubriqueEnfant) {
        return get(nomRubriqueEnfant,
                "",
                "",
                "",
                "",
                "",
                null,
                null,
                null,
                null,
                "",
                "",
                "",
                null);
    }

    /**
     * Obtient la liste des événements stockés au sein de la base de données en fonction des
     * paramètres
     *
     * @param nomRubriqueEnfant nom de la rubrique enfant des événements à obtenir
     * @param debut             date de début des événements à obtenir
     * @param fin               date de fin des événements à obtenir
     * @param nomStatut         nom du statut des événements à obtenir
     * @return la liste des événements en fonction des paramètres de sélection
     */
    public List<Evenement> get(String nomRubriqueEnfant,
                               Calendar debut,
                               Calendar fin,
                               String nomStatut) {
        List<Evenement> evenementList = null;
        RubriqueEnfant rubriqueEnfant = null;
        boolean success = true;

        // Obtient la rubrique enfant correspondant au paramètres de sélection
        if (nomRubriqueEnfant != null && !nomRubriqueEnfant.isEmpty()) {
            List<RubriqueEnfant> rubriqueEnfantList = RubriqueEnfantAccess.getInstance()
                    .get("", nomRubriqueEnfant);
            success = rubriqueEnfantList != null && rubriqueEnfantList.size() == 1;

            if (success) {
                rubriqueEnfant = rubriqueEnfantList.get(0);
            }
        }

        if (success) {

            // Obtient le statut correspondant au paramètres de sélection
            List<Statut> statutList = StatutAccess.getInstance().get(nomStatut);
            success = statutList != null && statutList.size() == 1;

            if (success) {

                // Obtient l'événement correspondant au paramètres de sélection
                evenementList = get(
                        rubriqueEnfant,
                        null,
                        null,
                        null,
                        null,
                        null,
                        debut,
                        fin,
                        "",
                        null,
                        statutList.get(0),
                        null);
            }
        }

        // Journalisation de l'exécution
        logSuccess(success);

        return evenementList;
    }

    /**
     * Obtient la liste des événements stockés au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param rubriqueEnfant rubrique enfant des événements à obtenir
     * @param utilisateur    utilisateur des événements à obtenir
     * @param nomEvenement   nom d'événement des événements à obtenir
     * @param adresse        adresse des événements à obtenir
     * @param latitude       latitude des événements à obtenir
     * @param longitude      longitude des événements à obtenir
     * @param debut          date de début des événements à obtenir
     * @param fin            date de fin des événements à obtenir
     * @param details        détails des événements à obtenir
     * @param priorite       priorité des événements à obtenir
     * @param statut         statut des événements à obtenir
     * @param creation       date de création des événements à obtenir
     * @return liste des événements stockés au sein de la base de données en fonction des paramètres
     */
    public List<Evenement> get(RubriqueEnfant rubriqueEnfant,
                               Utilisateur utilisateur,
                               String nomEvenement,
                               Adresse adresse,
                               Double latitude,
                               Double longitude,
                               Calendar debut,
                               Calendar fin,
                               String details,
                               Priorite priorite,
                               Statut statut,
                               Calendar creation) {

        // Définit les paramètres de la requête en fonction de la valeurs des paramètres de
        // l'événement
        checkNull(rubriqueEnfant, utilisateur, adresse, priorite, statut);
        return get(nomRubriqueEnfant,
                nomUtilisateur,
                nomEvenement,
                nomRue,
                numeroDeRue,
                numeroNpa,
                latitude,
                longitude,
                debut,
                fin,
                details,
                nomPriorite,
                nomStatut,
                creation);
    }

    /**
     * Obtient la liste des événements stockés au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomRubriqueEnfant nom de la rubrique enfant des événements à obtenir
     * @param nomUtilisateur    nom de l'utilisateur des événements à obtenir
     * @param nomEvenement      nom d'événement des événements à obtenir
     * @param nomRue            nom de la rue de l'adresse des événements à obtenir
     * @param numeroDeRue       numéro de rue de l'adresse des événements à obtenir
     * @param numeroNpa         numéro npa de l'adresse des événements à obtenir
     * @param latitude          latitude des événements à obtenir
     * @param longitude         longitude des événements à obtenir
     * @param debut             date de début des événements à obtenir
     * @param fin               date de fin des événements à obtenir
     * @param details           détails des événements à obtenir
     * @param nomPriorite       nom de la priorité des événements à obtenir
     * @param nomStatut         nom du statut des événements à obtenir
     * @param creation          date de création des événements à obtenir
     * @return liste des événements stockés au sein de la base de données en fonction des paramètres
     */
    public List<Evenement> get(String nomRubriqueEnfant,
                               String nomUtilisateur,
                               String nomEvenement,
                               String nomRue,
                               String numeroDeRue,
                               String numeroNpa,
                               Double latitude,
                               Double longitude,
                               Calendar debut,
                               Calendar fin,
                               String details,
                               String nomPriorite,
                               String nomStatut,
                               Calendar creation) {
        List<Evenement> evenementList = null;

        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                // Définit des critères de sélection pour la requête
                CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
                CriteriaQuery<Evenement> criteriaQuery = criteriaBuilder.createQuery(Evenement.class);

                // Liaison avec différentes tables
                Root<Evenement> evenementRoot = criteriaQuery.from(Evenement.class);
                Join<Evenement, RubriqueEnfant> evenementRubriqueEnfantJoin =
                        evenementRoot.join(Evenement_.rubriqueEnfant);
                Join<Evenement, Utilisateur> evenementUtilisateurJoin =
                        evenementRoot.join(Evenement_.utilisateur);
                Join<Evenement, Adresse> evenementAdresseJoin =
                        evenementRoot.join(Evenement_.adresse);
                Join<Adresse, Rue> adresseRueJoin =
                        evenementAdresseJoin.join(Adresse_.rue);
                Join<Adresse, Npa> adresseNpaJoin =
                        evenementAdresseJoin.join(Adresse_.npa);
                Join<Evenement, Priorite> evenementPrioriteJoin =
                        evenementRoot.join(Evenement_.priorite);
                Join<Evenement, Statut> evenementStatutJoin =
                        evenementRoot.join(Evenement_.statut);
                List<Predicate> predicateList = new ArrayList<>();

                // Définit seulement les critères de sélection pour la requête des paramètres non null
                // et non vide
                if (nomRubriqueEnfant != null && !nomRubriqueEnfant.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(
                            evenementRubriqueEnfantJoin.get(RubriqueEnfant_.nomRubriqueEnfant),
                            nomRubriqueEnfant.toLowerCase()));
                }

                if (nomUtilisateur != null && !nomUtilisateur.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(
                            evenementUtilisateurJoin.get(Utilisateur_.nomUtilisateur),
                            nomUtilisateur.toLowerCase()));
                }

                if (nomEvenement != null && !nomEvenement.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(
                            evenementRoot.get(Evenement_.nomEvenement),
                            nomEvenement.toLowerCase()));
                }

                if (nomRue != null && !nomRue.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(
                            adresseRueJoin.get(Rue_.nomRue),
                            nomRue.toLowerCase()));
                }

                if (numeroDeRue != null && !numeroDeRue.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(
                            evenementAdresseJoin.get(Adresse_.numeroDeRue),
                            numeroDeRue.toLowerCase()));
                }

                if (numeroNpa != null && !numeroNpa.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(
                            adresseNpaJoin.get(Npa_.numeroNpa),
                            numeroNpa.toLowerCase()));
                }

                if (latitude != null) {
                    predicateList.add(criteriaBuilder.equal(
                            evenementRoot.get(Evenement_.latitude),
                            latitude));
                }

                if (longitude != null) {
                    predicateList.add(criteriaBuilder.equal(
                            evenementRoot.get(Evenement_.longitude),
                            longitude));
                }

                if (debut != null) {
                    setMinimumTime(debut);
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(
                            evenementRoot.get(Evenement_.debut),
                            debut));
                }

                if (fin != null) {
                    setMinimumTime(fin);
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(
                            evenementRoot.get(Evenement_.fin),
                            fin));
                }

                if (details != null && !details.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(
                            evenementRoot.get(Evenement_.details),
                            details.toLowerCase()));
                }

                if (nomPriorite != null && !nomPriorite.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(
                            evenementPrioriteJoin.get(Priorite_.nomPriorite),
                            nomPriorite.toLowerCase()));
                }

                if (nomStatut != null && !nomStatut.isEmpty()) {
                    predicateList.add(criteriaBuilder.equal(
                            evenementStatutJoin.get(Statut_.nomStatut),
                            nomStatut.toLowerCase()));
                }

                if (creation != null) {
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(
                            evenementRoot.get(Evenement_.creation), creation));
                }

                criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
                evenementList = hibernate.createQuery(criteriaQuery).getResultList();

                transaction.commit();
            }
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                evenementList != null ? evenementList.size() : 0,
                Evenement.class.getSimpleName()));

        return evenementList;
    }

    /**
     * Stocke l'événement définit par les paramètres
     *
     * @param nomRubriqueEnfant nom de la rubrique enfant de l'événement à stocker
     * @param idUtilisateur     identifiant de l'utilisateur de l'événement à stocker
     * @param nomEvenement      nom d'événement de l'événement à stocker
     * @param nomRue            nom de la rue de l'adresse de l'événement à stocker
     * @param numeroDeRue       numéro de rue de l'adresse de l'événement à stocker
     * @param numeroNpa         numéro npa de l'adresse de l'événement à stocker
     * @param latitude          latitude de l'événement à stocker
     * @param longitude         longitude de l'événement à stocker
     * @param debut             date de début de l'événement à stocker
     * @param fin               date de fin de l'événement à stocker
     * @param details           détails de l'événement à stocker
     * @param nomPriorite       nom de la priorité de l'événement à stocker
     * @param nomStatut         nom du statut de l'événement à stocker
     */
    public void save(String nomRubriqueEnfant,
                     Integer idUtilisateur,
                     String nomEvenement,
                     String nomRue,
                     String numeroDeRue,
                     String numeroNpa,
                     Double latitude,
                     Double longitude,
                     Calendar debut,
                     Calendar fin,
                     String details,
                     String nomPriorite,
                     Integer niveauPriorite,
                     String nomStatut) {

        // Obtient la rubrique enfant désirée
        List<RubriqueEnfant> rubriqueEnfantList = RubriqueEnfantAccess.getInstance().get(
                "",
                nomRubriqueEnfant);

        boolean success = rubriqueEnfantList != null && rubriqueEnfantList.size() == 1;
        if (success) {
            RubriqueEnfant rubriqueEnfant = rubriqueEnfantList.get(0);

            // Obtient l'utilisateur désiré
            Utilisateur utilisateur = databaseAccess.get(Utilisateur.class, idUtilisateur);
            success = utilisateur != null;

            // Vérifie si la requête a abouti
            if (success) {

                // Obtient la rue désirée
                List<Rue> rueList = RueAccess.getInstance().get(nomRue);
                success = rueList != null && rueList.size() <= 1;

                // Vérifie si la requête a abouti
                if (success) {
                    Rue rue = new Rue(nomRue);

                    if (rueList.size() == 1) {
                        rue = rueList.get(0);
                    }

                    // Obtient le npa désiré
                    List<Npa> npaList = NpaAccess.getInstance().get(numeroNpa);
                    success = npaList != null && npaList.size() <= 1;

                    // Vérifie si la requête a abouti
                    if (success) {
                        Npa npa = new Npa(numeroNpa);

                        if (npaList.size() == 1) {
                            npa = npaList.get(0);
                        }

                        // Obtient l'adresse désirée
                        List<Adresse> adresseList = AdresseAccess.getInstance()
                                .get(rue, numeroDeRue, npa);
                        success = adresseList != null && adresseList.size() <= 1;

                        // Vérifie si la requête a abouti
                        if (success) {
                            Adresse adresse = new Adresse(rue, numeroDeRue, npa);

                            if (adresseList.size() == 1) {
                                adresse = adresseList.get(0);
                            }

                            // Obtient la priorité désirée
                            List<Priorite> prioriteList = PrioriteAccess.getInstance()
                                    .get(nomPriorite, niveauPriorite);
                            success = prioriteList != null && prioriteList.size() <= 1;

                            // Vérifie si la requête a abouti
                            if (success) {
                                Priorite priorite = new Priorite(nomPriorite, niveauPriorite);

                                if (prioriteList.size() == 1) {
                                    priorite = prioriteList.get(0);
                                }

                                // Obtient le statut désiré
                                List<Statut> statutList = StatutAccess.getInstance().get(nomStatut);
                                success = statutList != null && statutList.size() <= 1;

                                // Vérifie si la requête a abouti
                                if (success) {
                                    Statut statut = new Statut(nomStatut);

                                    if (statutList.size() == 1) {
                                        statut = statutList.get(0);
                                    }

                                    save(rubriqueEnfant,
                                            utilisateur,
                                            nomEvenement,
                                            adresse,
                                            latitude,
                                            longitude,
                                            debut,
                                            fin,
                                            details,
                                            priorite,
                                            statut);
                                }
                            }
                        }
                    }
                }
            }
        }

        logSuccess(success);
    }

    /**
     * Stocke l'événement définit par les paramètres
     *
     * @param rubriqueEnfant rubrique enfant de l'événement à stocker
     * @param utilisateur    utilisateur de l'événement à stocker
     * @param nomEvenement   nom d'événement de l'événement à stocker
     * @param debut          date de début de l'événement à stocker
     * @param priorite       priorité de l'événement à stocker
     * @param statut         statut de l'événement à stocker
     */
    public void save(RubriqueEnfant rubriqueEnfant,
                     Utilisateur utilisateur,
                     String nomEvenement,
                     Calendar debut,
                     Priorite priorite,
                     Statut statut) {
        databaseAccess.save(new Evenement(
                rubriqueEnfant,
                utilisateur,
                nomEvenement,
                debut,
                priorite,
                statut));
    }

    /**
     * Stocke l'événement définit par les paramètres
     *
     * @param rubriqueEnfant rubrique enfant de l'événement à stocker
     * @param utilisateur    utilisateur de l'événement à stocker
     * @param nomEvenement   nom d'événement de l'événement à stocker
     * @param adresse        adresse de l'événement à stocker
     * @param latitude       latitude de l'événement à stocker
     * @param longitude      longitude de l'événement à stocker
     * @param debut          date de début de l'événement à stocker
     * @param fin            date de fin de l'événement à stocker
     * @param details        détails de l'événement à stocker
     * @param priorite       priorité de l'événement à stocker
     * @param statut         statut de l'événement à stocker
     */
    public void save(RubriqueEnfant rubriqueEnfant,
                     Utilisateur utilisateur,
                     String nomEvenement,
                     Adresse adresse,
                     Double latitude,
                     Double longitude,
                     Calendar debut,
                     Calendar fin,
                     String details,
                     Priorite priorite,
                     Statut statut) {
        databaseAccess.save(new Evenement(
                rubriqueEnfant,
                utilisateur,
                nomEvenement,
                adresse,
                latitude,
                longitude,
                debut,
                fin,
                details,
                priorite,
                statut));
    }

    /**
     * Met à jour l'événement correspondant aux paramètres
     *
     * @param evenement événement à mettre à jour
     */
    public void update(Evenement evenement) {
        update(evenement.getIdEvenement(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                evenement.getStatut());
    }

    /**
     * Met à jour l'événement correspondant aux paramètres
     * Chaque paramètre de valeurs null ne se mettre pas à jour
     *
     * @param idEvenement    identifiant de l'événement à mettre à jour
     * @param rubriqueEnfant rubrique enfant des événements à mettre à jour
     * @param utilisateur    utilisateur des événements à mettre à jour
     * @param nomEvenement   nom d'événement des événements à mettre à jour
     * @param adresse        adresse des événements à mettre à jour
     * @param latitude       latitude des événements à mettre à jour
     * @param longitude      longitude des événements à mettre à jour
     * @param debut          date de début des événements à mettre à jour
     * @param fin            date de fin des événements à mettre à jour
     * @param details        détails des événements à mettre à jour
     * @param priorite       priorité des événements à mettre à jour
     * @param statut         statut des événements à mettre à jour
     */
    public void update(Integer idEvenement,
                       RubriqueEnfant rubriqueEnfant,
                       Utilisateur utilisateur,
                       String nomEvenement,
                       Adresse adresse,
                       Double latitude,
                       Double longitude,
                       Calendar debut,
                       Calendar fin,
                       String details,
                       Priorite priorite,
                       Statut statut) {
        Evenement evenement = databaseAccess.get(Evenement.class, idEvenement);

        // Vérifie si la requête a abouti
        if (evenement != null) {

            // Affecte les nouveaux attributs à l'événement
            setAll(evenement,
                    rubriqueEnfant,
                    utilisateur,
                    nomEvenement,
                    adresse,
                    latitude,
                    longitude,
                    debut,
                    fin,
                    details,
                    priorite,
                    statut);
            databaseAccess.update(evenement);
        }
    }

    /**
     * Met à jour les événements correspondant aux paramètres préfixés de old en leur
     * affectant les paramètres préfixés de new
     * Chaque paramètre préfixés de old différent de null sera utilisé comme critère de recherche
     * Chaque paramètre préfixés de new de valeurs null ne se mettre pas à jour
     *
     * @param oldRubriqueEnfant ancienne rubrique enfant des événements à mettre à jour
     * @param oldUtilisateur    ancien nom d'utilisateur des événements à mettre à jour
     * @param oldNomEvenement   ancien nom d'événement des événements à mettre à jour
     * @param oldAdresse        ancienne adresse des événements à mettre à jour
     * @param oldLatitude       ancienne latitude des événements à mettre à jour
     * @param oldLongitude      ancienne longitude des événements à mettre à jour
     * @param oldDebut          ancienne date de début des événements à mettre à jour
     * @param oldFin            ancienne date de fin des événements à mettre à jour
     * @param oldDetails        ancien détails des événements à mettre à jour
     * @param oldPriorite       ancienne priorité des événements à mettre à jour
     * @param oldStatut         ancien statut des événements à mettre à jour
     * @param creation          ancienne date de création des événements à mettre à jour
     * @param newRubriqueEnfant nouvelle rubrique enfant des événements à mettre à jour
     * @param newUtilisateur    nouveau nom d'utilisateur des événements à mettre à jour
     * @param newNomEvenement   nouveau nom d'événement des événements à mettre à jour
     * @param newAdresse        nouvelle adresse des événements à mettre à jour
     * @param newLatitude       nouvelle latitude des événements à mettre à jour
     * @param newLongitude      nouvelle longitude des événements à mettre à jour
     * @param newDebut          nouvelle date de début des événements à mettre à jour
     * @param newFin            nouvelle date de fin des événements à mettre à jour
     * @param newDetails        nouveaux détails des événements à mettre à jour
     * @param newPriorite       nouvelle priorité des événements à mettre à jour
     * @param newStatut         nouveau statut des événements à mettre à jour
     */
    public void update(RubriqueEnfant oldRubriqueEnfant,
                       Utilisateur oldUtilisateur,
                       String oldNomEvenement,
                       Adresse oldAdresse,
                       Double oldLatitude,
                       Double oldLongitude,
                       Calendar oldDebut,
                       Calendar oldFin,
                       String oldDetails,
                       Priorite oldPriorite,
                       Statut oldStatut,
                       Calendar creation,
                       RubriqueEnfant newRubriqueEnfant,
                       Utilisateur newUtilisateur,
                       String newNomEvenement,
                       Adresse newAdresse,
                       Double newLatitude,
                       Double newLongitude,
                       Calendar newDebut,
                       Calendar newFin,
                       String newDetails,
                       Priorite newPriorite,
                       Statut newStatut) {
        List<Evenement> evenementList = get(
                oldRubriqueEnfant,
                oldUtilisateur,
                oldNomEvenement,
                oldAdresse,
                oldLatitude,
                oldLongitude,
                oldDebut,
                oldFin,
                oldDetails,
                oldPriorite,
                oldStatut,
                creation);

        // Vérifie si la requête a abouti
        if (evenementList != null) {

            // Affecte les nouveaux attributs aux événements
            for (Evenement evenement : evenementList) {
                setAll(evenement,
                        newRubriqueEnfant,
                        newUtilisateur,
                        newNomEvenement,
                        newAdresse,
                        newLatitude,
                        newLongitude,
                        newDebut,
                        newFin,
                        newDetails,
                        newPriorite,
                        newStatut);
            }

            databaseAccess.update(evenementList);
        }
    }

    /**
     * Supprime l'événement en paramètres, c'est à dire, ne sera plus visible comme actif
     *
     * @param evenement événement à supprimer
     */
    public void delete(Evenement evenement) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        evenement.setFin(calendar);
        databaseAccess.update(evenement);
    }

    /**
     * Supprime les événements correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param rubriqueEnfant rubrique enfant des événements à supprimer
     * @param utilisateur    utilisateur des événements à supprimer
     * @param nomEvenement   nom des événement des événements à supprimer
     * @param adresse        adresse des événements à supprimer
     * @param latitude       latitude des événements à supprimer
     * @param longitude      longitude des événements à supprimer
     * @param debut          date de début des événements à supprimer
     * @param fin            date de fin des événements à supprimer
     * @param details        détails des événements à supprimer
     * @param priorite       nom de la priorité des événements à supprimer
     * @param statut         nom du statut des événements à supprimer
     * @param creation       date de création des événements à supprimer
     */
    public void delete(RubriqueEnfant rubriqueEnfant,
                       Utilisateur utilisateur,
                       String nomEvenement,
                       Adresse adresse,
                       Double latitude,
                       Double longitude,
                       Calendar debut,
                       Calendar fin,
                       String details,
                       Priorite priorite,
                       Statut statut,
                       Calendar creation) {


        // Définit les paramètres de la requête en fonction de la valeurs des paramètres de
        // l'événement
        checkNull(rubriqueEnfant, utilisateur, adresse, priorite, statut);
        delete(nomRubriqueEnfant,
                nomUtilisateur,
                nomEvenement,
                nomRue,
                numeroDeRue,
                numeroNpa,
                latitude,
                longitude,
                debut,
                fin,
                details,
                nomPriorite,
                nomStatut,
                creation);
    }

    /**
     * Supprime les événements correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param nomRubriqueEnfant nom de la rubrique enfant des événements à supprimer
     * @param nomUtilisateur    nom de l'utilisateur des événements à supprimer
     * @param nomEvenement      nom des événements à supprimer
     * @param nomRue            nom de la rue de l'adresse des événements à supprimer
     * @param numeroDeRue       numéro de rue de l'adresse des événements à supprimer
     * @param numeroNpa         numéro npa de l'adresse des événements à supprimer
     * @param latitude          latitude des événements à supprimer
     * @param longitude         longitude des événements à supprimer
     * @param debut             date de début des événements à supprimer
     * @param fin               date de fin des événements à supprimer
     * @param details           détails des événements à supprimer
     * @param nomPriorite       nom de la priorité des événements à supprimer
     * @param nomStatut         nom du statut des événements à supprimer
     */
    public void delete(String nomRubriqueEnfant,
                       String nomUtilisateur,
                       String nomEvenement,
                       String nomRue,
                       String numeroDeRue,
                       String numeroNpa,
                       Double latitude,
                       Double longitude,
                       Calendar debut,
                       Calendar fin,
                       String details,
                       String nomPriorite,
                       String nomStatut,
                       Calendar creation) {
        databaseAccess.delete(get(
                nomRubriqueEnfant,
                nomUtilisateur,
                nomEvenement,
                nomRue,
                numeroDeRue,
                numeroNpa,
                latitude,
                longitude,
                debut,
                fin,
                details,
                nomPriorite,
                nomStatut,
                creation));
    }

    /**
     * Affecte les paramètres de l'événement si ils ne sont pas null
     *
     * @param evenement      événement dont il faut définir les paramètres
     * @param rubriqueEnfant rubrique enfant de l'événement
     * @param utilisateur    utilisateur de l'événement
     * @param nomEvenement   nom de l'événement
     * @param adresse        adresse de l'événement
     * @param latitude       latitude de l'événement
     * @param longitude      longitude de l'événement
     * @param debut          date début de l'événement
     * @param fin            date de fin de l'événement
     * @param details        détails de l'événement
     * @param priorite       priorite de l'événement
     * @param statut         statut de l'événement
     */
    private void setAll(Evenement evenement,
                        RubriqueEnfant rubriqueEnfant,
                        Utilisateur utilisateur,
                        String nomEvenement,
                        Adresse adresse,
                        Double latitude,
                        Double longitude,
                        Calendar debut,
                        Calendar fin,
                        String details,
                        Priorite priorite,
                        Statut statut) {
        if (rubriqueEnfant != null) {
            evenement.setRubriqueEnfant(rubriqueEnfant);
        }

        if (utilisateur != null) {
            evenement.setUtilisateur(utilisateur);
        }

        if (nomEvenement != null) {
            evenement.setNomEvenement(nomEvenement);
        }

        if (adresse != null) {
            evenement.setAdresse(adresse);
        }

        if (latitude != null) {
            evenement.setLatitude(latitude);
        }

        if (longitude != null) {
            evenement.setLongitude(longitude);
        }

        if (debut != null) {
            evenement.setDebut(debut);
        }

        if (fin != null) {
            evenement.setFin(fin);
        }

        if (details != null) {
            evenement.setDetails(details);
        }

        if (priorite != null) {
            evenement.setPriorite(priorite);
        }

        if (statut != null) {
            evenement.setStatut(statut);
        }
    }

    /**
     * Définit les paramètres de la requête en fonction de la valeurs des paramètres de l'événement
     *
     * @param rubriqueEnfant rubrique enfant à vérifier
     * @param utilisateur    utilisateur à vérifier
     * @param adresse        adresse à vérifier
     * @param priorite       priorite à vérifier
     * @param statut         statut à vérifier
     */
    private void checkNull(RubriqueEnfant rubriqueEnfant,
                           Utilisateur utilisateur,
                           Adresse adresse,
                           Priorite priorite,
                           Statut statut) {
        nomRubriqueEnfant = rubriqueEnfant != null ? rubriqueEnfant.getNomRubriqueEnfant() : null;
        nomUtilisateur = utilisateur != null ? utilisateur.getNomUtilisateur() : null;
        nomRue = adresse != null ? adresse.getRue().getNomRue() : null;
        numeroDeRue = adresse != null ? adresse.getNumeroDeRue() : null;
        numeroNpa = adresse != null ? adresse.getNpa().getNumeroNpa() : null;
        nomPriorite = priorite != null ? priorite.getNomPriorite() : null;
        nomStatut = statut != null ? statut.getNomStatut() : null;
    }

    /**
     * Définit le minimum du temps d'une date
     *
     * @param calendar date dont il faut définir le temps au minimum
     */
    private void setMinimumTime(Calendar calendar) {
        if (calendar != null) {
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
            calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        }
    }

    /**
     * Journalise l'exécution
     *
     * @param success état de réussite de l'exécution
     */
    private void logSuccess(boolean success) {
        if (success) {
            logger.info(configurationManager.getString("databaseAccess.successInSubQuery"));
        } else {
            logger.info(configurationManager.getString("databaseAccess.errorInSubQuery"));
        }
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final EvenementAccess instance = new EvenementAccess();
    }
}
