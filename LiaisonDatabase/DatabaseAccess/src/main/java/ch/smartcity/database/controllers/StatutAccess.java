package database.controllers;

import database.models.Statut;
import org.hibernate.Session;

import java.util.List;

public class StatutAccess {

    private final DatabaseManager databaseManager;

    public StatutAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<Statut> get(String nomStatut) {
        List<Statut> statutList = null;

        try {
            Session session = databaseManager.openSession();

            statutList = session
                    .createNamedQuery(
                            databaseManager.getProperty("statut.namedQuery.findByNomStatut"),
                            Statut.class)
                    .setParameter(
                            databaseManager.getProperty("statut.parameter.nomStatut"),
                            nomStatut.toLowerCase()).list();

            databaseManager.commit();
        } catch (Exception ex) {
            databaseManager.rollback(ex);
        } finally {
            databaseManager.close();
        }

        return statutList;
    }

    public void save(String nomStatut) {
        DatabaseAccess.save(new Statut(nomStatut));
    }

    public void update(int idStatut, String nomStatut) {
        Statut statut = DatabaseAccess.get(Statut.class, idStatut);
        statut.setNomStatut(nomStatut);
        DatabaseAccess.update(statut);
    }

    public void update(String oldNomStatut, String newNomStatut) {
        List<Statut> statutList = get(oldNomStatut);

        for (Statut statut : statutList) {
            statut.setNomStatut(newNomStatut);
        }

        DatabaseAccess.update(statutList);
    }

    public void delete(String nomStatut) {
        DatabaseAccess.delete(get(nomStatut));
    }
}
