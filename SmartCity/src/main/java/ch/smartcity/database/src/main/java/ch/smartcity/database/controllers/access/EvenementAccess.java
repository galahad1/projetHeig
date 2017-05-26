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
     * Utilisé pour stocker la valeur des attributs ci-dessous en fonction de la nullité des
     * paramètres d'un événement
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

    public List<Evenement> getActif() {
        return getActif(null, null, Calendar.getInstance(), Statut_.TRAITE);
    }

    public List<Evenement> getActif(String nomRubriqueEnfant,
                                    Calendar date,
                                    String nomStatut) {
        return getActif(nomRubriqueEnfant, date, date, nomStatut);
    }

    public List<Evenement> getActif(String nomRubriqueEnfant,
                                    Calendar debut,
                                    Calendar fin,
                                    String nomStatut) {
        List<Evenement> evenementList = null;
        RubriqueEnfant rubriqueEnfant = null;
        boolean success = true;

        if (nomRubriqueEnfant != null && !nomRubriqueEnfant.isEmpty()) {
            List<RubriqueEnfant> rubriqueEnfantList = RubriqueEnfantAccess.getInstance().get(
                    "",
                    nomRubriqueEnfant);
            success = rubriqueEnfantList != null && rubriqueEnfantList.size() == 1;
            if (success) {
                rubriqueEnfant = rubriqueEnfantList.get(0);
            }
        }

        if (success) {
            List<Statut> statutList = StatutAccess.getInstance().get(nomStatut);
            success = statutList != null && statutList.size() == 1;
            if (success) {
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

        if (success) {
            logger.info(configurationManager.getString("databaseAccess.successInSubQuery"));

        } else {
            logger.info(configurationManager.getString("databaseAccess.errorInSubQuery"));
        }

        return evenementList;
    }

    public List<Evenement> getEnAttente() {
        return getActif(
                null,
                null,
                Calendar.getInstance(),
                Statut_.EN_ATTENTE);
    }

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

        // Définit nomRubriqueEnfant, nomUtilisateur, nomRue, numeroDeRue, numeroNpa, nomPriorite
        // et nomStatut en fonction de la valeurs des paramètres rubriqueEnfant, utilisateur,
        // adresse, priorite et statut
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

        Session session = null;
        Transaction transaction = null;

        try {
            // Démarre une transaction pour la gestion d'erreur
            session = hibernate.getSession();
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
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        databaseAccess.close(session);

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                evenementList != null ? evenementList.size() : 0,
                Evenement.class.getSimpleName()));

        return evenementList;
    }

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
        List<RubriqueEnfant> rubriqueEnfantList = RubriqueEnfantAccess.getInstance().get(
                "",
                nomRubriqueEnfant);

        boolean success = rubriqueEnfantList != null && rubriqueEnfantList.size() <= 1;
        if (success) {
            RubriqueEnfant rubriqueEnfant = rubriqueEnfantList.get(0);

            Utilisateur administrator = databaseAccess.get(Utilisateur.class, idUtilisateur);
            success = administrator != null;
            if (success) {

                List<Rue> rueList = RueAccess.getInstance().get(nomRue);
                success = rueList != null && rueList.size() <= 1;
                if (success) {
                    Rue rue = new Rue(nomRue);

                    if (rueList.size() == 1) {
                        rue = rueList.get(0);
                    }

                    List<Npa> npaList = NpaAccess.getInstance().get(numeroNpa);
                    success = npaList != null && npaList.size() <= 1;
                    if (success) {
                        Npa npa = new Npa(numeroNpa);

                        if (npaList.size() == 1) {
                            npa = npaList.get(0);
                        }

                        List<Adresse> adresseList = AdresseAccess.getInstance()
                                .get(rue, numeroDeRue, npa);
                        success = adresseList != null && adresseList.size() <= 1;
                        if (success) {
                            Adresse adresse = new Adresse(rue, numeroDeRue, npa);

                            if (adresseList.size() == 1) {
                                adresse = adresseList.get(0);
                            }

                            List<Priorite> prioriteList = PrioriteAccess.getInstance()
                                    .get(nomPriorite, niveauPriorite);
                            success = prioriteList != null && prioriteList.size() <= 1;
                            if (success) {
                                Priorite priorite = new Priorite(nomPriorite, niveauPriorite);

                                if (prioriteList.size() == 1) {
                                    priorite = prioriteList.get(0);
                                }

                                List<Statut> statutList = StatutAccess.getInstance().get(nomStatut);
                                success = statutList != null && statutList.size() <= 1;
                                if (success) {
                                    Statut statut = new Statut(nomStatut);

                                    if (statutList.size() == 1) {
                                        statut = statutList.get(0);
                                    }

                                    save(rubriqueEnfant,
                                            administrator,
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

        if (success) {
            logger.info(configurationManager.getString("databaseAccess.successInSubQuery"));

        } else {
            logger.info(configurationManager.getString("databaseAccess.errorInSubQuery"));
        }
    }

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

    public void delete(Evenement evenement) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        evenement.setFin(calendar);
        databaseAccess.update(evenement);
    }

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

        // Définit nomRubriqueEnfant, nomUtilisateur, nomRue, numeroDeRue, numeroNpa, nomPriorite
        // et nomStatut en fonction de la valeurs des paramètres rubriqueEnfant, utilisateur,
        // adresse, priorite et statut
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

    private void setMinimumTime(Calendar calendar) {
        if (calendar != null) {
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
            calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        }
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final EvenementAccess instance = new EvenementAccess();
    }
}
