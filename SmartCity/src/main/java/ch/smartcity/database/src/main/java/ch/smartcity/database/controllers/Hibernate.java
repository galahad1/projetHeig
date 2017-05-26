package ch.smartcity.database.controllers;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gère la connexion et les requêtes effectuées auprès de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class Hibernate {

    /**
     * Fichier de configuration pour l'accès à la base données
     */
    private final String hibernateConfigurationXmlFile =
            "ch/smartcity/database/resources/hibernate/hibernate.cfg.xml";

    /**
     * Utilisé pour accéder aux fichiers de propriétés
     */
    private final ConfigurationManager configurationManager;

    /**
     * Utilisé pour journaliser les actions effectuées
     */
    private final Logger logger;

    /**
     * Utilisé pour créer une session de travail avec la base de données
     */
    private final SessionFactory sessionFactory;

    /**
     * Utilisé pour accéder à la base de données
     */
    private Session session;

    private Hibernate() {
        configurationManager = ConfigurationManager.getInstance();
        logger = Logger.getLogger(getClass().getName());

        try {
            sessionFactory = new Configuration()
                    .configure(hibernateConfigurationXmlFile)
                    .buildSessionFactory();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Fournit l'unique instance de la classe (singleton)
     *
     * @return unique instance de la classe
     */
    public static Hibernate getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Fournit le créateur d'une session de travail avec la base de données
     *
     * @return créateur d'une session de travail avec la base de données
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Fournit la session de travail en cours avec la base de données
     *
     * @return session de travail en cours avec la base de données
     */
    public Session getSession() {
        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession();
        }

        return session;
    }

    /**
     * Fournit le créateur de critères d'une requête
     *
     * @return créateur de critères d'une requête
     */
    public CriteriaBuilder getCriteriaBuilder() {
        return sessionFactory.getCriteriaBuilder();
    }

    /**
     * Crée une requête suivant les critères fournis
     *
     * @param tCriteriaQuery critères de la requête
     * @param <T>            type d'objet à accéder lors de la requête
     * @return requête suivant les critères fournis
     */
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> tCriteriaQuery) {
        return getSession().createQuery(tCriteriaQuery);
    }

    /**
     * Ferme la session de travail avec la base de données
     *
     * @throws HibernateException si la session n'a pas pu être fermé
     */
    public void closeSession() throws HibernateException {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                throw e;
            }
        }
    }

    /**
     * Ferme la création de session de travail avec la base de données
     *
     * @throws HibernateException si la sessionFactory n'a pas pu être fermé
     */
    public void closeSessionFactory() throws HibernateException {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            try {
                sessionFactory.close();
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                throw e;
            }
        }
    }

    /**
     * Ferme la connexion avec la base de données
     *
     * @throws HibernateException si la session ou la sessionFactory n'ont pas pu être fermé
     */
    public void close() throws HibernateException {
        closeSession();
        closeSessionFactory();
    }

    /**
     * Utilisé pour créer un singleton de la classe
     */
    private static class SingletonHolder {
        private static final Hibernate instance = new Hibernate();
    }
}
