package ch.smartcity.database.controllers.access;

import ch.smartcity.database.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.Hibernate;
import ch.smartcity.database.models.Priorite;
import ch.smartcity.database.models.Priorite_;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PrioriteAccess {

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

    private PrioriteAccess() {
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
    public static PrioriteAccess getInstance() {
        return SingletonHolder.instance;
    }

    public List<Priorite> get(String nomPriorite, Integer niveau) {
        List<Priorite> prioriteList = null;

        Session session = null;
        Transaction transaction = null;

        try {
            // Démarre une transaction pour la gestion d'erreur
            session = hibernate.getSession();
            transaction = session.beginTransaction();

            // Définit des critères de sélection pour la requête
            CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
            CriteriaQuery<Priorite> criteriaQuery = criteriaBuilder.createQuery(Priorite.class);
            Root<Priorite> prioriteRoot = criteriaQuery.from(Priorite.class);
            List<Predicate> predicateList = new ArrayList<>();

            // Définit seulement les critères de sélection pour la requête des paramètres non null
            // et non vide
            if (nomPriorite != null && !nomPriorite.isEmpty()) {
                predicateList.add(criteriaBuilder.equal(
                        prioriteRoot.get(Priorite_.nomPriorite),
                        nomPriorite.toLowerCase()));
            }

            if (niveau != null) {
                predicateList.add(criteriaBuilder.equal(
                        prioriteRoot.get(Priorite_.niveau),
                        niveau));
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
            prioriteList = hibernate.createQuery(criteriaQuery).getResultList();

            transaction.commit();
        } catch (Exception e) {
            databaseAccess.rollback(e, transaction);
        }

        databaseAccess.close(session);

        // Journalise l'état de la transaction et le résultat
        databaseAccess.transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                prioriteList != null ? prioriteList.size() : 0,
                Priorite.class.getSimpleName()));

        return prioriteList;
    }

    public void save(String nomPriorite, Integer numero) {
        databaseAccess.save(new Priorite(nomPriorite, numero));
    }

    public void update(Integer idPriorite, String nomPriorite, Integer niveau) {
        Priorite priorite = databaseAccess.get(Priorite.class, idPriorite);

        // Vérifie si la requête a abouti
        if (priorite != null) {

            // Affecte les nouveaux attributs au priorité
            setAll(priorite, nomPriorite, niveau);
            databaseAccess.update(priorite);
        }
    }

    public void update(String oldNomPriorite,
                       Integer oldNiveau,
                       String newNomPriorite,
                       Integer newNiveau) {
        List<Priorite> prioriteList = get(oldNomPriorite, oldNiveau);

        // Vérifie si la requête a abouti
        if (prioriteList != null) {
            for (Priorite priorite : prioriteList) {

                // Affecte les nouveaux attributs aux priorités
                setAll(priorite, newNomPriorite, newNiveau);
            }

            databaseAccess.update(prioriteList);
        }
    }

    public void delete(String nomPriorite, Integer niveau) {
        databaseAccess.delete(get(nomPriorite, niveau));
    }

    private void setAll(Priorite priorite, String nomPriorite, Integer niveau) {
        if (nomPriorite != null) {
            priorite.setNomPriorite(nomPriorite);
        }

        if (niveau != null) {
            priorite.setNiveau(niveau);
        }
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final PrioriteAccess instance = new PrioriteAccess();
    }
}
