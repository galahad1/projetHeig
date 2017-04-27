package database.controllers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class DatabaseManager {

    private final static String LOGGING_PROPERTIES_FILE;
    private final static Logger LOGGER;

    static {
        LOGGING_PROPERTIES_FILE = "database/resources/logging.properties";

        try {
            LogManager.getLogManager().readConfiguration(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(LOGGING_PROPERTIES_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    }

    private SessionFactory sessionFactory;
    private Properties properties;
    private ResourceBundle resourceBundle;
    private Session session;

    public DatabaseManager() {
        this("database/resources/hibernate/hibernate.cfg.xml",
                "database/resources/database.properties",
                "database/resources/messagesBundles/messageBundle");
    }

    public DatabaseManager(String hibernateConfigurationFile,
                           String databasePropertiesFile,
                           String baseNameResourceBundle) {
        loadHibernateConfiguration(hibernateConfigurationFile);
        loadDatabaseProperties(databasePropertiesFile);
        loadResourceBundle(baseNameResourceBundle);
    }

    private void loadHibernateConfiguration(String hibernateConfigurationFile) {
        try {
            sessionFactory = new Configuration()
                    .configure(hibernateConfigurationFile)
                    .buildSessionFactory();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void loadDatabaseProperties(String databasePropertiesFile) {
        try {
            properties = new Properties();
            properties.load(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(databasePropertiesFile));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void loadResourceBundle(String baseNameResourceBundle) {
        resourceBundle = ResourceBundle.getBundle(baseNameResourceBundle, Locale.getDefault());
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public Session getSession() {
        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession();
        }

        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return sessionFactory.getCriteriaBuilder();
    }

    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> tCriteriaQuery) {
        return getSession().createQuery(tCriteriaQuery);
    }

    public void close() {
        if (session != null) {
            try {
                session.close();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getString(String key) {
        return resourceBundle.getString(key);
    }
}
