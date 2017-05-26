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
 * Fournit l'accès aux utilisateurs de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class UtilisateurAccess {

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
     * d'un utilisateur
     */
    private String titre;
    private String abreviation;
    private String nomSexe;
    private String nomPriorite;
    private String nomRue;
    private String numeroDeRue;
    private String numeroNpa;

    private UtilisateurAccess() {
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
    public static UtilisateurAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient la liste des utilisateurs au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param personnePhysique personnePhysique de l'utilisateur à obtenir
     * @param avs              avs de l'utilisateur à obtenir
     * @param titreCivil       titre civil de l'utilisateur à obtenir
     * @param nomUtilisateur   nom de l'utilisateur à obtenir
     * @param prenom           prénom de l'utilisateur à obtenir
     * @param dateDeNaissance  date de naissance de l'utilisateur à obtenir
     * @param sexe             sexe de l'utilisateur à obtenir
     * @param nationalite      nationalité de l'utilisateur à obtenir
     * @param adresse          adresse de l'utilisateur à obtenir
     * @param email            email de l'utilisateur à obtenir
     * @param pseudo           pseudo de l'utilisateur à obtenir
     * @param motDePasse       mot de passe de l'utilisateur à obtenir
     * @param sel              sel de l'utilisateur à obtenir
     * @param creation         date de création de l'utilisateur à obtenir
     * @return liste des utilisateurs au sein de la base de données en fonction des
     * paramètres
     */
    public List<Utilisateur> get(Boolean personnePhysique,
                                 String avs,
                                 TitreCivil titreCivil,
                                 String nomUtilisateur,
                                 String prenom,
                                 Calendar dateDeNaissance,
                                 Sexe sexe,
                                 Nationalite nationalite,
                                 Adresse adresse,
                                 String email,
                                 String pseudo,
                                 String motDePasse,
                                 String sel,
                                 Calendar creation) {
        checkNull(titreCivil, sexe, nationalite, adresse);
        return get(personnePhysique,
                avs,
                titre,
                abreviation,
                nomUtilisateur,
                prenom,
                dateDeNaissance,
                nomSexe,
                nomPriorite,
                nomRue,
                numeroDeRue,
                numeroNpa,
                email,
                pseudo,
                motDePasse,
                sel,
                creation);
    }

    /**
     * Obtient la liste des utilisateurs stockés au sein de la base de données en fonction des
     * paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param personnePhysique personnePhysique des utilisateurs à obtenir
     * @param avs              avs des utilisateurs à obtenir
     * @param titre            titredu titre civil des utilisateurs à obtenir
     * @param abreviation      abreviation des utilisateurs à obtenir
     * @param nomUtilisateur   nom des utilisateurs à obtenir
     * @param prenom           prénom des utilisateurs à obtenir
     * @param dateDeNaissance  date de naissance des utilisateurs à obtenir
     * @param nomSexe          nom du sexe des utilisateurs à obtenir
     * @param nomNationalite   nom de la nationalité des utilisateurs à obtenir
     * @param nomRue           nom de la rue de l'adresse des utilisateurs à obtenir
     * @param numeroDeRue      numéro de la rue de l'adresse des utilisateurs à obtenir
     * @param numeroNpa        numéro npa de l'adresse des utilisateurs à obtenir
     * @param email            email des utilisateurs à obtenir
     * @param pseudo           pseudo des utilisateurs à obtenir
     * @param motDePasse       mot de passe des utilisateurs à obtenir
     * @param sel              sel des utilisateurs à obtenir
     * @param creation         date de création des utilisateurs à obtenir
     * @return liste des utilisateurs stockés au sein de la base de données en fonction des
     * paramètres
     */
    public List<Utilisateur> get(Boolean personnePhysique,
                                 String avs,
                                 String titre,
                                 String abreviation,
                                 String nomUtilisateur,
                                 String prenom,
                                 Calendar dateDeNaissance,
                                 String nomSexe,
                                 String nomNationalite,
                                 String nomRue,
                                 String numeroDeRue,
                                 String numeroNpa,
                                 String email,
                                 String pseudo,
                                 String motDePasse,
                                 String sel,
                                 Calendar creation) {
        List<Utilisateur> utilisateurList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            // Démarre une transaction pour la gestion d'erreur
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            // Définit des critères de sélection pour la requête
            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<Utilisateur> criteriaQuery = criteriaBuilder
                    .createQuery(Utilisateur.class);

            // Liaison avec différentes tables
            Root<Utilisateur> utilisateurRoot = criteriaQuery.from(Utilisateur.class);
            Join<Utilisateur, TitreCivil> utilisateurTitreCivilJoin =
                    utilisateurRoot.join(Utilisateur_.titreCivil);
            Join<Utilisateur, Sexe> utilisateurSexeJoin =
                    utilisateurRoot.join(Utilisateur_.sexe);
            Join<Utilisateur, Nationalite> utilisateurNationaliteJoin =
                    utilisateurRoot.join(Utilisateur_.nationalite);
            Join<Utilisateur, Adresse> utilisateurAdresseJoin =
                    utilisateurRoot.join(Utilisateur_.adresse);
            Join<Adresse, Rue> adresseRueJoin =
                    utilisateurAdresseJoin.join(Adresse_.rue);
            Join<Adresse, Npa> adresseNpaJoin =
                    utilisateurAdresseJoin.join(Adresse_.npa);
            List<Predicate> predicateList = new ArrayList<>();

            // Définit seulement les critères de sélection pour la requête des paramètres non null
            // et non vide
            if (personnePhysique != null) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurRoot.get(Utilisateur_.personnePhysique),
                        personnePhysique));
            }

            if (avs != null && !avs.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurRoot.get(Utilisateur_.avs),
                        avs));
            }

            if (titre != null && !titre.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurTitreCivilJoin.get(TitreCivil_.titre),
                        titre.toLowerCase()));
            }

            if (abreviation != null && !abreviation.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurTitreCivilJoin.get(TitreCivil_.abreviation),
                        abreviation.toLowerCase()));
            }

            if (nomUtilisateur != null && !nomUtilisateur.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurRoot.get(Utilisateur_.nomUtilisateur),
                        nomUtilisateur.toLowerCase()));
            }

            if (prenom != null && !prenom.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurRoot.get(Utilisateur_.prenom),
                        prenom.toLowerCase()));
            }

            if (dateDeNaissance != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(
                        utilisateurRoot.get(Utilisateur_.dateDeNaissance),
                        dateDeNaissance));
            }

            if (nomSexe != null && !nomSexe.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurSexeJoin.get(Sexe_.nomSexe),
                        nomSexe.toLowerCase()));
            }

            if (nomNationalite != null && !nomNationalite.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurNationaliteJoin.get(Nationalite_.nomNationalite),
                        nomNationalite.toLowerCase()));
            }

            if (nomRue != null && !nomRue.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        adresseRueJoin.get(Rue_.nomRue),
                        nomRue.toLowerCase()));
            }

            if (numeroDeRue != null && !numeroDeRue.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurAdresseJoin.get(Adresse_.numeroDeRue),
                        numeroDeRue.toLowerCase()));
            }

            if (numeroNpa != null && !numeroNpa.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        adresseNpaJoin.get(Npa_.numeroNpa),
                        numeroNpa.toLowerCase()));
            }

            if (email != null && !email.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurRoot.get(Utilisateur_.email),
                        email.toLowerCase()));
            }

            if (pseudo != null && !pseudo.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurRoot.get(Utilisateur_.pseudo),
                        pseudo.toLowerCase()));
            }

            if (motDePasse != null && !motDePasse.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurRoot.get(Utilisateur_.motDePasse),
                        motDePasse.toLowerCase()));
            }

            if (sel != null && !sel.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        utilisateurRoot.get(Utilisateur_.sel),
                        sel.toLowerCase()));
            }

            if (creation != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(
                        utilisateurRoot.get(Utilisateur_.creation),
                        creation));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            utilisateurList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        databaseAccess.close(session);

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                utilisateurList != null ? utilisateurList.size() : 0,
                Utilisateur.class.getSimpleName()));

        return utilisateurList;
    }

    /**
     * Stocke l'utilisateur définit par les paramètres
     *
     * @param titreCivil     titre civil de l'utilisateur à stocker
     * @param nomUtilisateur nom de l'utilisateur à stocker
     * @param adresse        adresse de l'utilisateur à stocker
     * @param email          email de l'utilisateur à stocker
     * @param pseudo         pseudo de l'utilisateur à stocker
     * @param motDePasse     mot de passe de l'utilisateur à stocker
     * @param sel            sel de l'utilisateur à stocker
     */
    public void save(TitreCivil titreCivil,
                     String nomUtilisateur,
                     Adresse adresse,
                     String email,
                     String pseudo,
                     String motDePasse,
                     String sel) {
        databaseAccess.save(new Utilisateur(
                titreCivil,
                nomUtilisateur,
                adresse,
                email,
                pseudo,
                motDePasse,
                sel));
    }

    /**
     * Stocke l'utilisateur définit par les paramètres
     *
     * @param avs             avs de l'utilisateur à stocker
     * @param titreCivil      titre civil de l'utilisateur à stocker
     * @param nomUtilisateur  nom de l'utilisateur à stocker
     * @param prenom          prénom de l'utilisateur à stocker
     * @param dateDeNaissance date de naissance de l'utilisateur à stocker
     * @param sexe            sexe de l'utilisateur à stocker
     * @param nationalite     nationalité de l'utilisateur à stocker
     * @param adresse         adresse de l'utilisateur à stocker
     * @param email           email de l'utilisateur à stocker
     * @param pseudo          pseudo de l'utilisateur à stocker
     * @param motDePasse      mot de passe de l'utilisateur à stocker
     * @param sel             sel de l'utilisateur à stocker
     */
    public void save(String avs,
                     TitreCivil titreCivil,
                     String nomUtilisateur,
                     String prenom,
                     Calendar dateDeNaissance,
                     Sexe sexe,
                     Nationalite nationalite,
                     Adresse adresse,
                     String email,
                     String pseudo,
                     String motDePasse,
                     String sel) {
        databaseAccess.save(new Utilisateur(true,
                avs,
                titreCivil,
                nomUtilisateur,
                prenom,
                dateDeNaissance,
                sexe,
                nationalite,
                adresse,
                email,
                pseudo,
                motDePasse,
                sel));
    }

    /**
     * Met à jour l'utilisateur correspondant aux paramètres
     * Chaque paramètre de valeurs null ne se mettre pas à jour
     *
     * @param idUtilisateur    identifiant de l'utilisateur à mettre à jour
     * @param personnePhysique personnePhysique de l'utilisateur à mettre à jour
     * @param avs              avs de l'utilisateur à mettre à jour
     * @param titreCivil       titre civil de l'utilisateur à mettre à jour
     * @param nomUtilisateur   nom de l'utilisateur à mettre à jour
     * @param prenom           prénom de l'utilisateur à mettre à jour
     * @param dateDeNaissance  date de naissance de l'utilisateur à mettre à jour
     * @param sexe             sexe de l'utilisateur à mettre à jour
     * @param nationalite      nationalité de l'utilisateur à mettre à jour
     * @param adresse          adresse de l'utilisateur à mettre à jour
     * @param email            email de l'utilisateur à mettre à jour
     * @param pseudo           pseudo de l'utilisateur à mettre à jour
     * @param motDePasse       mot de passe de l'utilisateur à mettre à jour
     * @param sel              sel de l'utilisateur à mettre à jour
     */
    public void update(Integer idUtilisateur,
                       Boolean personnePhysique,
                       String avs,
                       TitreCivil titreCivil,
                       String nomUtilisateur,
                       String prenom,
                       Calendar dateDeNaissance,
                       Sexe sexe,
                       Adresse adresse,
                       Nationalite nationalite,
                       String email,
                       String pseudo,
                       String motDePasse,
                       String sel) {
        Utilisateur utilisateur = databaseAccess.get(Utilisateur.class, idUtilisateur);

        // Vérifie si la requête a abouti
        if (utilisateur != null) {

            // Affecte les nouveaux attributs à l'utilisateur
            setAll(utilisateur,
                    personnePhysique,
                    avs,
                    titreCivil,
                    nomUtilisateur,
                    prenom,
                    dateDeNaissance,
                    sexe,
                    nationalite,
                    adresse,
                    email,
                    pseudo,
                    motDePasse,
                    sel);
            databaseAccess.update(utilisateur);
        }
    }

    /**
     * Met à jour les utilisateurs correspondant aux paramètres préfixés de old en leur
     * affectant les paramètres préfixés de new
     * Chaque paramètre préfixés de old différent de null sera utilisé comme critère de recherche
     * Chaque paramètre préfixés de new de valeurs null ne se mettre pas à jour
     *
     * @param oldPersonnePhysique ancien personnnePhysique des utilisateurs à mettre à jour
     * @param oldAvs              ancien avs des utilisateurs à mettre à jour
     * @param oldTitreCivil       ancien des utilisateurs à mettre à jour
     * @param oldNomUtilisateur   ancien nom des utilisateurs à mettre à jour
     * @param oldPrenom           ancien prénom des utilisateurs à mettre à jour
     * @param oldDateDeNaissance  ancienne date de naissance des utilisateurs à mettre à jour
     * @param oldSexe             ancien sexe des utilisateurs à mettre à jour
     * @param oldNationalite      ancienne nationalité des utilisateurs à mettre à jour
     * @param oldAdresse          ancienne adresse des utilisateurs à mettre à jour
     * @param oldEmail            ancien email des utilisateurs à mettre à jour
     * @param oldPseudo           ancien pseudo des utilisateurs à mettre à jour
     * @param oldMotDePasse       anicen mot de passe des utilisateurs à mettre à jour
     * @param oldSel              ancien sel des utilisateurs à mettre à jour
     * @param creation            date de création des utilisateurs à mettre à jour
     * @param newPersonnePhysique nouvelle personnePhysique des utilisateurs à mettre à jour
     * @param newAvs              nouveau avs des utilisateurs à mettre à jour
     * @param newTitreCivil       nouveeau titre civil des utilisateurs à mettre à jour
     * @param newNomUtilisateur   nouveau nom des utilisateurs à mettre à jour
     * @param newPrenom           nouveau prénom des utilisateurs à mettre à jour
     * @param newDateDeNaissance  nouvelle date de naissance des utilisateurs à mettre à jour
     * @param newSexe             nouveau sexe des utilisateurs à mettre à jour
     * @param newNationalite      nouvelle nationalité des utilisateurs à mettre à jour
     * @param newAdresse          nouvelle adresse des utilisateurs à mettre à jour
     * @param newEmail            nouvel email des utilisateurs à mettre à jour
     * @param newPseudo           nouveau pseudo des utilisateurs à mettre à jour
     * @param newMotDePasse       nouveau mot de passe des utilisateurs à mettre à jour
     * @param newSel              nouveau sel des utilisateurs à mettre à jour
     */
    public void update(Boolean oldPersonnePhysique,
                       String oldAvs,
                       TitreCivil oldTitreCivil,
                       String oldNomUtilisateur,
                       String oldPrenom,
                       Calendar oldDateDeNaissance,
                       Sexe oldSexe,
                       Nationalite oldNationalite,
                       Adresse oldAdresse,
                       String oldEmail,
                       String oldPseudo,
                       String oldMotDePasse,
                       String oldSel,
                       Calendar creation,
                       Boolean newPersonnePhysique,
                       String newAvs,
                       TitreCivil newTitreCivil,
                       String newNomUtilisateur,
                       String newPrenom,
                       Calendar newDateDeNaissance,
                       Sexe newSexe,
                       Nationalite newNationalite,
                       Adresse newAdresse,
                       String newEmail,
                       String newPseudo,
                       String newMotDePasse,
                       String newSel) {
        List<Utilisateur> utilisateurList = get(
                oldPersonnePhysique,
                oldAvs,
                oldTitreCivil,
                oldNomUtilisateur,
                oldPrenom,
                oldDateDeNaissance,
                oldSexe,
                oldNationalite,
                oldAdresse,
                oldEmail,
                oldPseudo,
                oldMotDePasse,
                oldSel,
                creation);

        // Vérifie si la requête a abouti
        if (utilisateurList != null) {

            // Affecte les nouveaux attributs aux utilisateurs
            for (Utilisateur utilisateur : utilisateurList) {
                setAll(utilisateur,
                        newPersonnePhysique,
                        newAvs,
                        newTitreCivil,
                        newNomUtilisateur,
                        newPrenom,
                        newDateDeNaissance,
                        newSexe,
                        newNationalite,
                        newAdresse,
                        newEmail,
                        newPseudo,
                        newMotDePasse,
                        newSel);
                databaseAccess.update(utilisateurList);
            }
        }
    }

    /**
     * Supprime les utilisateurs correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param personnePhysique personnePhysique des utilisateurs à supprimer
     * @param avs              avs des utilisateurs à supprimer
     * @param titreCivil       titre civil des utilisateurs à supprimer
     * @param nomUtilisateur   nom des utilisateurs à supprimer
     * @param prenom           prénom des utilisateurs à supprimer
     * @param dateDeNaissance  date de naissance des utilisateurs à supprimer
     * @param sexe             sexe des utilisateurs à supprimer
     * @param nationalite      nationalité des utilisateurs à supprimer
     * @param adresse          adresse des utilisateurs à supprimer
     * @param email            email des utilisateurs à supprimer
     * @param pseudo           pseudo des utilisateurs à supprimer
     * @param motDePasse       mot de passe des utilisateurs à supprimer
     * @param sel              sel des utilisateurs à supprimer
     */
    public void delete(Boolean personnePhysique,
                       String avs,
                       TitreCivil titreCivil,
                       String nomUtilisateur,
                       String prenom,
                       Calendar dateDeNaissance,
                       Sexe sexe,
                       Nationalite nationalite,
                       Adresse adresse,
                       String email,
                       String pseudo,
                       String motDePasse,
                       String sel,
                       Calendar creation) {

        // Définit les paramètres de la requête en fonction de la valeurs des paramètres de
        // l'utilisateur
        checkNull(titreCivil, sexe, nationalite, adresse);
        delete(personnePhysique,
                avs,
                titre,
                abreviation,
                nomUtilisateur,
                prenom,
                dateDeNaissance,
                nomSexe,
                nomPriorite,
                nomRue,
                numeroDeRue,
                numeroNpa,
                email,
                pseudo,
                motDePasse,
                sel,
                creation);
    }

    /**
     * Supprime les utilisateurs correspondant aux paramètres
     * Chaque paramètre différent de null sera utilisé comme critère de recherche
     *
     * @param personnePhysique personnePhysique des utilisateurs à supprimer
     * @param avs              avs des utilisateurs à supprimer
     * @param titre            titredu titre civil des utilisateurs à supprimer
     * @param abreviation      abreviation des utilisateurs à supprimer
     * @param nomUtilisateur   nom des utilisateurs à supprimer
     * @param prenom           prénom des utilisateurs à supprimer
     * @param dateDeNaissance  date de naissance des utilisateurs à supprimer
     * @param nomSexe          nom du sexe des utilisateurs à supprimer
     * @param nomNationalite   nom de la nationalité des utilisateurs à supprimer
     * @param nomRue           nom de la rue de l'adresse des utilisateurs à supprimer
     * @param numeroDeRue      numéro de la rue de l'adresse des utilisateurs à supprimer
     * @param numeroNpa        numéro npa de l'adresse des utilisateurs à supprimer
     * @param email            email des utilisateurs à supprimer
     * @param pseudo           pseudo des utilisateurs à supprimer
     * @param motDePasse       mot de passe des utilisateurs à supprimer
     * @param sel              sel des utilisateurs à supprimer
     * @param creation         date de création des utilisateurs à supprimer
     */
    public void delete(Boolean personnePhysique,
                       String avs,
                       String titre,
                       String abreviation,
                       String nomUtilisateur,
                       String prenom,
                       Calendar dateDeNaissance,
                       String nomSexe,
                       String nomNationalite,
                       String nomRue,
                       String numeroDeRue,
                       String numeroNpa,
                       String email,
                       String pseudo,
                       String motDePasse,
                       String sel,
                       Calendar creation) {
        databaseAccess.delete(get(
                personnePhysique,
                avs,
                titre,
                abreviation,
                nomUtilisateur,
                prenom,
                dateDeNaissance,
                nomSexe,
                nomNationalite,
                nomRue,
                numeroDeRue,
                numeroNpa,
                email,
                pseudo,
                motDePasse,
                sel,
                creation));
    }

    /**
     * Affecte les paramètres de l'utilisateur si ils ne sont pas null
     *
     * @param utilisateur      utilisateur dont il faut définir les paramètres
     * @param personnePhysique personnePhysique de l'utilisateur
     * @param avs              avs de l'utilisateur
     * @param titreCivil       titre civil de l'utilisateur
     * @param nomUtilisateur   nom de l'utilisateur
     * @param prenom           prénom de l'utilisateur
     * @param dateDeNaissance  date de naissance de l'utilisateur
     * @param sexe             sexe de l'utilisateur
     * @param nationalite      nationalité de l'utilisateur
     * @param adresse          adresse de l'utilisateur
     * @param email            email de l'utilisateur
     * @param pseudo           pseudo de l'utilisateur
     * @param motDePasse       mot de passe de l'utilisateur
     * @param sel              sel de l'utilisateur
     */
    private void setAll(Utilisateur utilisateur,
                        Boolean personnePhysique,
                        String avs,
                        TitreCivil titreCivil,
                        String nomUtilisateur,
                        String prenom,
                        Calendar dateDeNaissance,
                        Sexe sexe,
                        Nationalite nationalite,
                        Adresse adresse,
                        String email,
                        String pseudo,
                        String motDePasse,
                        String sel) {
        if (personnePhysique != null) {
            utilisateur.setPersonnePhysique(personnePhysique);
        }

        if (avs != null) {
            utilisateur.setAvs(avs);
        }

        if (titreCivil != null) {
            utilisateur.setTitreCivil(titreCivil);
        }

        if (nomUtilisateur != null) {
            utilisateur.setNomUtilisateur(nomUtilisateur);
        }

        if (prenom != null) {
            utilisateur.setPrenom(prenom);
        }

        if (dateDeNaissance != null) {
            utilisateur.setDateDeNaissance(dateDeNaissance);
        }

        if (sexe != null) {
            utilisateur.setSexe(sexe);
        }

        if (nationalite != null) {
            utilisateur.setNationalite(nationalite);
        }

        if (adresse != null) {
            utilisateur.setAdresse(adresse);
        }

        if (email != null) {
            utilisateur.setEmail(email);
        }

        if (pseudo != null) {
            utilisateur.setPseudo(pseudo);
        }

        if (motDePasse != null) {
            utilisateur.setMotDePasse(motDePasse);
        }

        if (sel != null) {
            utilisateur.setSel(sel);
        }
    }

    /**
     * Définit les paramètres de la requête en fonction de la valeurs des paramètres de
     * l'utilisateur
     *
     * @param titreCivil  titre civil à vérifier
     * @param sexe        sexe à vérifier
     * @param nationalite nationalié à vérifier
     * @param adresse     adresse à vérifier
     */
    private void checkNull(TitreCivil titreCivil,
                           Sexe sexe,
                           Nationalite nationalite,
                           Adresse adresse) {
        titre = titreCivil != null ? titreCivil.getTitre() : null;
        abreviation = titreCivil != null ? titreCivil.getAbreviation() : null;
        nomSexe = sexe != null ? sexe.getNomSexe() : null;
        nomPriorite = nationalite != null ? nationalite.getNomNationalite() : null;
        nomRue = adresse != null ? adresse.getRue().getNomRue() : null;
        numeroDeRue = adresse != null ? adresse.getNumeroDeRue() : null;
        numeroNpa = adresse != null ? adresse.getNpa().getNumeroNpa() : null;
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final UtilisateurAccess instance = new UtilisateurAccess();
    }
}
