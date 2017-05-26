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

    public static UtilisateurAccess getInstance() {
        return SingletonHolder.instance;
    }

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
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<Utilisateur> criteriaQuery = criteriaBuilder
                    .createQuery(Utilisateur.class);
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
        } finally {
            databaseAccess.close(session);
        }

        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                utilisateurList != null ? utilisateurList.size() : 0,
                Utilisateur.class.getSimpleName()));

        return utilisateurList;
    }

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

    public void save(Boolean personnePhysique,
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
        databaseAccess.save(new Utilisateur(
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
                sel));
    }

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

        if (utilisateur != null) {
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

        if (utilisateurList != null) {
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

    private static class SingletonHolder {
        private static final UtilisateurAccess instance = new UtilisateurAccess();
    }
}
