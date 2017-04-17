package controllers;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseAccess {

    private final static Logger LOGGER = Logger.getLogger(
            DatabaseAccess.class.getName());

    private static String _hibernateConfigurationFile =
            "ressources/hibernate/hibernate.cfg.xml";

    private final static SessionFactory SESSION_FACTORY = new Configuration()
            .configure(_hibernateConfigurationFile)
            .buildSessionFactory();

    public final static NpaAccess NPA_ACCESS = new NpaAccess(SESSION_FACTORY);

    public static String getHibernateConfigurationFile() {
        return _hibernateConfigurationFile;
    }

    public static void setHibernateConfigurationFile(
            String hibernateConfigurationFile) {
        _hibernateConfigurationFile = hibernateConfigurationFile;
    }

    public static void close() {
        try {
            SESSION_FACTORY.close();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
