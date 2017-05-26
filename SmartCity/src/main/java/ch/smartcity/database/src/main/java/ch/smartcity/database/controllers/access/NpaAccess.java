package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.Npa;
import ch.smartcity.database.models.Npa_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class NpaAccess {

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

    private NpaAccess() {
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
    public static NpaAccess getInstance() {
        return SingletonHolder.instance;
    }

    public List<Npa> get(String numeroNpa) {
        List<Npa> npaList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            // Démarre une transaction pour la gestion d'erreur
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            // Définit des critères de sélection pour la requête
            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<Npa> criteriaQuery = criteriaBuilder.createQuery(Npa.class);
            Root<Npa> npaRoot = criteriaQuery.from(Npa.class);
            List<Predicate> predicateList = new ArrayList<>();

            // Définit seulement les critères de sélection pour la requête des paramètres non null
            // et non vide
            if (numeroNpa != null && !numeroNpa.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        npaRoot.get(Npa_.numeroNpa), numeroNpa.toLowerCase()));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            npaList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        databaseAccess.close(session);

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                npaList != null ? npaList.size() : 0,
                Npa.class.getSimpleName()));

        return npaList;
    }

    public void save(String numeroNpa) {
        databaseAccess.save(new Npa(numeroNpa));
    }

    public void update(Integer idNpa, String numeroNpa) {
        Npa npa = databaseAccess.get(Npa.class, idNpa);

        // Vérifie si la requête a abouti
        if (npa != null) {

            // Affecte les nouveaux attributs au npa
            setAll(npa, numeroNpa);
            databaseAccess.update(npa);
        }
    }

    public void update(String oldNumeroNpa, String newNumeroNpa) {
        List<Npa> npaList = get(oldNumeroNpa);

        // Vérifie si la requête a abouti
        if (npaList != null) {

            // Affecte les nouveaux attributs aux npas
            for (Npa npa : npaList) {
                setAll(npa, newNumeroNpa);
            }

            databaseAccess.update(npaList);
        }
    }

    public void delete(String numeroNpa) {
        databaseAccess.delete(get(numeroNpa));
    }

    private void setAll(Npa npa, String numeroNpa) {
        if (numeroNpa != null) {
            npa.setNumeroNpa(numeroNpa);
        }
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final NpaAccess instance = new NpaAccess();
    }
}
