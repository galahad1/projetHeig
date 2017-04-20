package database.controllers;

import database.models.Nationalite;
import org.hibernate.Session;

import java.util.List;

public class NationaliteAccess {

    private final DatabaseManager databaseManager;

    public NationaliteAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<Nationalite> get(String nomNationalite) {
        List<Nationalite> nationaliteList = null;

        try {
            Session session = databaseManager.openSession();

            nationaliteList = session
                    .createNamedQuery(
                            databaseManager.getProperty("nationalite.namedQuery.findByNomNationalite"),
                            Nationalite.class)
                    .setParameter(databaseManager.getProperty("nationalite.parameter.nomNationalite"),
                            nomNationalite.toLowerCase()).list();

            databaseManager.commit();
        } catch (Exception ex) {
            databaseManager.rollback(ex);
        } finally {
            databaseManager.close();
        }

        return nationaliteList;
    }

    public void save(String nomNationalite) {
        DatabaseAccess.save(new Nationalite(nomNationalite));
    }

    public void update(int idNationalite, String nomNationalite) {
        Nationalite nationalite = DatabaseAccess.get(Nationalite.class, idNationalite);
        nationalite.setNomNationalite(nomNationalite);
        DatabaseAccess.update(nationalite);
    }

    public void update(String oldNomNationalite, String newNomNationalite) {
        List<Nationalite> nationaliteList = get(oldNomNationalite);

        for (Nationalite nationalite : nationaliteList) {
            nationalite.setNomNationalite(newNomNationalite);
        }

        DatabaseAccess.update(nationaliteList);
    }

    public void delete(String nomNationalite) {
        DatabaseAccess.delete(get(nomNationalite));
    }
}
