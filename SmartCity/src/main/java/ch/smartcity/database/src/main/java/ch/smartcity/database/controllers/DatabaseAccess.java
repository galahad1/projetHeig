package ch.smartcity.database.controllers;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fournit l'accès à la base de données de manière génériques
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class DatabaseAccess {

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

    private DatabaseAccess() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());
        hibernate = Hibernate.getInstance();
    }

    /**
     * Fournit l'unique instance de la classe (singleton)
     *
     * @return unique instance de la classe
     */
    public static DatabaseAccess getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Obtient l'objet stocké au sein de la base de données
     *
     * @param tClass classe de l'objet à obtenir
     * @param id     identifiant de l'objet à obtenir
     * @param <T>    type de l'objet à obtenir correspondant à une table de la base de données
     * @return objet stocké dans la base de données correspondant à la classe et à l'identifiant
     * donnée
     */
    public <T> T get(Class<T> tClass, Integer id) {
        T t = null;

        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                t = session.get(tClass, id);

                transaction.commit();
            }
        } catch (Exception e) {
            rollback(e, transaction);
        }

        // Journalise l'état de la transaction et le résultat
        transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                t != null ? 1 : 0,
                tClass.getSimpleName()));

        return t;
    }

    /**
     * Obtient la liste d'objet stockés au sein de la base de données
     *
     * @param tClass classe de l'objet à obtenir
     * @param <T>    type des objets à obtenir correspondant à une table de la base de données
     * @return liste d'objet stockés dans la base de données correspondant à la classe donnée
     */
    public <T> List<T> get(Class<T> tClass) {
        List<T> tList = null;

        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                // Définit des critères de sélection pour la requête
                CriteriaBuilder criteriaBuilder = hibernate.getCriteriaBuilder();
                CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(tClass);
                criteriaQuery.from(tClass);
                tList = hibernate.createQuery(criteriaQuery).getResultList();

                transaction.commit();
            }
        } catch (Exception e) {
            rollback(e, transaction);
        }

        // Journalise l'état de la transaction et le résultat
        transactionMessage(transaction);
        logger.info(String.format(
                configurationManager.getString("databaseAccess.results"),
                tList != null ? tList.size() : 0,
                tClass.getSimpleName()));

        return tList;
    }

    /**
     * Stocke l'objet au sein de la base de données
     *
     * @param t   objet à stocker
     * @param <T> type de l'objet à stocker correspondant à une table de la base de données
     */
    public <T> void save(T t) {
        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                session.save(t);

                transaction.commit();
            }
        } catch (Exception e) {
            rollback(e, transaction);
        }

        // Journalise l'état de la transaction
        transactionMessage(transaction);
    }

    /**
     * Stocke la liste d'objet au sein de la base de données
     *
     * @param tList liste des objets à stocker
     * @param <T>   type des objets à stocker correspondant à une table de la base de données
     */
    public <T> void save(List<T> tList) {
        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                for (T t : tList) {
                    session.save(t);
                }

                transaction.commit();
            }
        } catch (Exception e) {
            rollback(e, transaction);
        }

        // Journalise l'état de la transaction et le résultat
        transactionMessage(transaction);
    }

    /**
     * Met à jour l'objet au sein de la base de données
     *
     * @param t   objet à mettre à jour
     * @param <T> type de l'objet à mettre à jour correspondant à une table de la base de données
     */
    public <T> void update(T t) {
        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                session.update(t);

                transaction.commit();
            }
        } catch (Exception e) {
            rollback(e, transaction);
        }

        // Journalise l'état de la transaction et le résultat
        transactionMessage(transaction);
    }

    /**
     * Met à jour la liste d'objets au sein de la base de données
     *
     * @param tList liste des objets à mettre à jour
     * @param <T>   type des objets à mettre à jour correspondant à une table de la base de données
     */
    public <T> void update(List<T> tList) {
        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                for (T t : tList) {
                    session.update(t);
                }

                transaction.commit();
            }
        } catch (Exception e) {
            rollback(e, transaction);
        }

        // Journalise l'état de la transaction et le résultat
        transactionMessage(transaction);
    }

    /**
     * Supprime l'objet au sein de la base de données
     *
     * @param tClass classe de l'objet à supprimer
     * @param id     identifiant de l'objet à supprimer
     * @param <T>    type de l'objet à supprimer correspondant à une table de la base de données
     */
    public <T> void delete(Class<T> tClass, int id) {
        delete(get(tClass, id));
    }

    /**
     * Supprime l'objet au sein de la base de données
     *
     * @param t   classe de l'objet à supprimer
     * @param <T> type de l'objet à supprimer correspondant à une table de la base de données
     */
    public <T> void delete(T t) {
        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                session.delete(t);

                transaction.commit();
            }
        } catch (Exception e) {
            rollback(e, transaction);
        }

        // Journalise l'état de la transaction et le résultat
        transactionMessage(transaction);
    }

    /**
     * Supprime la liste d'objets au sein de la base de données
     *
     * @param tList liste des objets à supprimer
     * @param <T>   type des objets à supprimer correspondant à une table de la base de données
     */
    public <T> void delete(List<T> tList) {
        Transaction transaction = null;

        try {
            Session session;

            // Démarre une transaction pour la gestion d'erreur
            synchronized (session = hibernate.getSession()) {
                transaction = session.beginTransaction();

                for (T t : tList) {
                    session.delete(t);
                }

                transaction.commit();
            }
        } catch (Exception e) {
            rollback(e, transaction);
        }

        // Journalise l'état de la transaction et le résultat
        transactionMessage(transaction);
    }

    /**
     * Annule une transaction
     *
     * @param e           exception levée lors de l'exécution de la transaction
     * @param transaction tansaction exécutée
     */
    public void rollback(Exception e, Transaction transaction) {
        if (transaction != null) {
            transaction.rollback();
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * Journalise l'état de la transaction
     *
     * @param transaction transaction exécutée
     */
    public void transactionMessage(Transaction transaction) {
        String key;

        if (transaction != null) {
            key = "databaseAccess.transactionCommitted";
        } else {
            key = "databaseAccess.transactionRollbacked";
        }

        logger.info(configurationManager.getString(key));
    }

    /**
     * Ferme la session de travail en cours
     *
     * @param session session de travail en cours
     * @throws HibernateException si la session de travail n'a pas pu être fermé
     */
    public void close(Session session) throws HibernateException {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                throw new HibernateException(e);
            }
        }
    }

    /**
     * Ferme la connexion à la base de données
     *
     * @throws Exception si la connexion n'a pas pu être fermé
     */
    public void close() throws Exception {
        hibernate.close();
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private final static DatabaseAccess instance = new DatabaseAccess();
    }
}
