package database.controllers;

import database.models.RubriqueEnfant;
import database.models.RubriqueParent;
import org.hibernate.Session;

import java.util.List;

public class RubriqueEnfantAccess {

    private final DatabaseManager databaseManager;

    public RubriqueEnfantAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<RubriqueEnfant> get(String nomRubriqueEnfant) {
        List<RubriqueEnfant> rubriqueEnfantList = null;

        try {
            Session session = databaseManager.openSession();

            rubriqueEnfantList = session
                    .createNamedQuery(
                            databaseManager.getProperty("rubriqueEnfant.namedQuery.findByNomRubriqueEnfant"),
                            RubriqueEnfant.class)
                    .setParameter(
                            databaseManager.getProperty("rubriqueEnfant.parameter.nomRubriqueEnfant"),
                            nomRubriqueEnfant.toLowerCase()).list();

            databaseManager.commit();
        } catch (Exception ex) {
            databaseManager.rollback(ex);
        } finally {
            databaseManager.close();
        }

        return rubriqueEnfantList;
    }

    public void save(String nomRubriqueEnfant, RubriqueParent rubriqueParent) {
        DatabaseAccess.save(new RubriqueEnfant(rubriqueParent, nomRubriqueEnfant));
    }

    public void update(int idRubriqueEnfant, String nomRubriqueEnfant) {
        RubriqueEnfant rubriqueEnfant = DatabaseAccess.get(RubriqueEnfant.class, idRubriqueEnfant);
        rubriqueEnfant.setNomRubriqueEnfant(nomRubriqueEnfant);
        DatabaseAccess.update(rubriqueEnfant);
    }

    public void update(String oldNomRubriqueEnfant, String newNomRubriqueEnfant) {
        List<RubriqueEnfant> rubriqueEnfantList = get(oldNomRubriqueEnfant);

        for (RubriqueEnfant rubriqueEnfant : rubriqueEnfantList) {
            rubriqueEnfant.setNomRubriqueEnfant(newNomRubriqueEnfant);
        }

        DatabaseAccess.update(rubriqueEnfantList);
    }

    public void delete(String nomRubriqueEnfant) {
        DatabaseAccess.delete(get(nomRubriqueEnfant));
    }
}
