package database.controllers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class DatabaseManager {

    private Logger logger;
    private SessionFactory sessionFactory;
    private Properties properties;
    private Session session;
    private Transaction transaction;

    public DatabaseManager() {
        this("database/resources/hibernate/hibernate.cfg.xml",
                "database/resources/logging.properties",
                "database/resources/database.properties");
    }

    public DatabaseManager(String hibernateConfigurationFile,
                           String loggingPropertiesFile,
                           String databasePropertiesFile) {
        this(Logger.getLogger(DatabaseManager.class.getName()),
                new Configuration()
                        .configure(hibernateConfigurationFile)
                        .buildSessionFactory(),
                new Properties());

        try {
            LogManager.getLogManager()
                    .readConfiguration(DatabaseManager.class.getClassLoader()
                            .getResourceAsStream(loggingPropertiesFile));
            properties.load(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(databasePropertiesFile));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public DatabaseManager(Logger logger, SessionFactory sessionFactory, Properties properties) {
        this.logger = logger;
        this.sessionFactory = sessionFactory;
        this.properties = properties;
    }

    public Session openSession() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();

        return session;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void commit() {
        transaction.commit();
    }

    public void log(Level level, String msg) {
        logger.log(Level.SEVERE, msg);
    }

    public void log(Level level, String msg, Throwable thrown) {
        logger.log(Level.SEVERE, msg, thrown);
    }

    public void rollback(Exception ex) {
        try {
            transaction.rollback();
            log(Level.SEVERE, ex.getMessage(), ex);
        } catch (Exception _ex) {
            log(Level.SEVERE, _ex.getMessage(), _ex);
        }
    }

    public void close() {
        try {
            session.close();
        } catch (Exception ex) {
            log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void terminate() {
        try {
            sessionFactory.close();
        } catch (Exception ex) {
            log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
