package database.controllers;

import database.models.Rue;
import org.hibernate.Session;

import java.util.List;

public class RueAccess {

    private final DatabaseManager databaseManager;

    public RueAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<Rue> get(String nomRue) {
        List<Rue> rueList = null;

        try {
            Session session = databaseManager.openSession();

            rueList = session
                    .createNamedQuery(
                            databaseManager.getProperty("rue.namedQuery.findByNomRue"),
                            Rue.class)
                    .setParameter(
                            databaseManager.getProperty("rue.parameter.nomRue"),
                            nomRue.toLowerCase()).list();

            databaseManager.commit();
        } catch (Exception ex) {
            databaseManager.rollback(ex);
        } finally {
            databaseManager.close();
        }

        return rueList;
    }

    public void save(String nomRue) {
        DatabaseAccess.save(new Rue(nomRue));
    }

    public void update(int idRue, String nomRue) {
        Rue rue = DatabaseAccess.get(Rue.class, idRue);
        rue.setNomRue(nomRue);
        DatabaseAccess.update(rue);
    }

    public void update(String oldNomRue, String newNomRue) {
        List<Rue> rueList = get(oldNomRue);

        for (Rue rue : rueList) {
            rue.setNomRue(newNomRue);
        }

        DatabaseAccess.update(rueList);
    }

    public void delete(String nomRue) {
        DatabaseAccess.delete(get(nomRue));
    }
}
