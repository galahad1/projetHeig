package database.controllers;

import database.models.TitreCivil;
import org.hibernate.Session;

import java.util.List;

public class TitreCivilAccess {

    private final DatabaseManager databaseManager;

    public TitreCivilAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<TitreCivil> get(String abreviation) {
        List<TitreCivil> titreCivilList = null;

        try {
            Session session = databaseManager.openSession();

            titreCivilList = session
                    .createNamedQuery(
                            databaseManager.getProperty("titreCivil.namedQuery.findByAbreviation"),
                            TitreCivil.class)
                    .setParameter(
                            databaseManager.getProperty("titreCivil.parameter.abreviation"),
                            abreviation.toLowerCase()).list();

            databaseManager.commit();
        } catch (Exception ex) {
            databaseManager.rollback(ex);
        } finally {
            databaseManager.close();
        }

        return titreCivilList;
    }

    public void save(String titre, String abreviation) {
        DatabaseAccess.save(new TitreCivil(titre, abreviation));
    }

    public void update(int idTitreCivil, String titre, String abreviation) {
        TitreCivil titreCivil = DatabaseAccess.get(TitreCivil.class, idTitreCivil);
        titreCivil.setTitre(titre);
        titreCivil.setAbreviation(abreviation);
        DatabaseAccess.update(titreCivil);
    }

    public void update(String oldAbreviation, String newTitre, String newAbreviation) {
        List<TitreCivil> titreCivilList = get(oldAbreviation);

        for (TitreCivil titreCivil : titreCivilList) {
            titreCivil.setTitre(newTitre);
            titreCivil.setAbreviation(newAbreviation);
        }

        DatabaseAccess.update(titreCivilList);
    }

    public void delete(String abreviation) {
        DatabaseAccess.delete(get(abreviation));
    }
}
