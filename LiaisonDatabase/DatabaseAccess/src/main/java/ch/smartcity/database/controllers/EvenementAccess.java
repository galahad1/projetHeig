package database.controllers;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class EvenementAccess {

    private final static Logger LOGGER;

    static {
        try {
            LogManager.getLogManager().readConfiguration(DatabaseManager.class.getClassLoader()
                    .getResourceAsStream(DatabaseManager.LOGGING_PROPERTIES_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER = Logger.getLogger(EvenementAccess.class.getName());
    }

    private final DatabaseManager databaseManager;

    public EvenementAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
//
//    public List<Evenement> get(String nomEvenement) {
//        List<Evenement> evenementList = null;
//
//        try {
//            Session session = databaseManager.openSession();
//
//            evenementList = session
//                    .createNamedQuery(
//                            databaseManager.getProperty("evenement.namedQuery.findByNomEvenement"),
//                            Evenement.class)
//                    .setParameter(databaseManager.getProperty("evenement.parameter.nomEvenement"),
//                            nomEvenement.toLowerCase()).list();
//
//            databaseManager.commit();
//        } catch (Exception ex) {
//            databaseManager.rollback(ex);
//        } finally {
//            databaseManager.close();
//        }
//
//        return evenementList;
//    }
//
//    public void save(RubriqueEnfant rubriqueEnfant,
//                     Utilisateur utilisateur,
//                     String nomEvenement,
//                     Calendar debut,
//                     Priorite priorite,
//                     Statut statut) {
//        DatabaseAccess.save(new Evenement(rubriqueEnfant,
//                utilisateur,
//                nomEvenement,
//                debut,
//                priorite,
//                statut));
//    }
//
//    public void save(RubriqueEnfant rubriqueEnfant,
//                     Utilisateur utilisateur,
//                     String nomEvenement,
//                     Adresse adresse,
//                     double latitude,
//                     double longitude,
//                     Calendar debut,
//                     Calendar fin,
//                     String details,
//                     Priorite priorite,
//                     Statut statut) {
//        DatabaseAccess.save(new Evenement(rubriqueEnfant,
//                utilisateur,
//                nomEvenement,
//                adresse,
//                latitude,
//                longitude,
//                debut,
//                fin,
//                details,
//                priorite,
//                statut));
//    }
//
//    public void delete(String nomEvenement) {
//        DatabaseAccess.delete(get(nomEvenement));
//    }

    private static void rollback(Exception ex, Transaction transaction) {
        if (transaction != null) {
            try {
                transaction.rollback();
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            } catch (Exception _ex) {
                LOGGER.log(Level.SEVERE, _ex.getMessage(), _ex);
            }
        }
    }

    private static void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }
}
