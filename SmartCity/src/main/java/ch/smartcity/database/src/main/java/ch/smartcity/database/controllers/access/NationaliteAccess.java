package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.Nationalite;
import ch.smartcity.database.models.Nationalite_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class NationaliteAccess {

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

    private NationaliteAccess() {
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
    public static NationaliteAccess getInstance() {
        return SingletonHolder.instance;
    }

    public List<Nationalite> get(String nomNationalite) {
        List<Nationalite> nationaliteList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            // Démarre une transaction pour la gestion d'erreur
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            // Définit des critères de sélection pour la requête
            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<Nationalite> criteriaQuery = criteriaBuilder.createQuery(Nationalite.class);
            Root<Nationalite> nationaliteRoot = criteriaQuery.from(Nationalite.class);
            List<Predicate> predicateList = new ArrayList<>();

            // Définit seulement les critères de sélection pour la requête des paramètres non null
            // et non vide
            if (nomNationalite != null && !nomNationalite.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        nationaliteRoot.get(Nationalite_.nomNationalite),
                        nomNationalite.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            nationaliteList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        databaseAccess.close(session);

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                nationaliteList != null ? nationaliteList.size() : 0,
                Nationalite.class.getSimpleName()));

        return nationaliteList;
    }

    public void save(String nomNationalite) {
        databaseAccess.save(new Nationalite(nomNationalite));
    }

    public void update(Integer idNationalite, String nomNationalite) {
        Nationalite nationalite = databaseAccess.get(Nationalite.class, idNationalite);

        // Vérifie si la requête a abouti
        if (nationalite != null) {

            // Affecte les nouveaux attributs à la nationalité
            setAll(nationalite, nomNationalite);
            databaseAccess.update(nationalite);
        }
    }

    public void update(String oldNomNationalite, String newNomNationalite) {
        List<Nationalite> nationaliteList = get(oldNomNationalite);

        // Vérifie si la requête a abouti
        if (nationaliteList != null) {

            // Affecte les nouveaux attributs aux nationalités
            for (Nationalite nationalite : nationaliteList) {
                setAll(nationalite, newNomNationalite);
            }

            databaseAccess.update(nationaliteList);
        }
    }

    public void delete(String nomNationalite) {
        databaseAccess.delete(get(nomNationalite));
    }

    private void setAll(Nationalite nationalite, String nomNationalite) {
        if (nomNationalite != null) {
            nationalite.setNomNationalite(nomNationalite);
        }
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final NationaliteAccess instance = new NationaliteAccess();
    }
}
