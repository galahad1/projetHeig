package database.controllers;

import database.models.RubriqueParent;
import org.hibernate.Session;

import java.util.List;

public class RubriqueParentAccess {

    private final DatabaseManager databaseManager;

    public RubriqueParentAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<RubriqueParent> get(String nomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = null;

        try {
            Session session = databaseManager.openSession();

            rubriqueParentList = session
                    .createNamedQuery(
                            databaseManager.getProperty("rubriqueParent.namedQuery.findByNomRubriqueParent"),
                            RubriqueParent.class)
                    .setParameter(
                            databaseManager.getProperty("rubriqueParent.parameter.nomRubriqueParent"),
                            nomRubriqueParent.toLowerCase()).list();

            databaseManager.commit();
        } catch (Exception ex) {
            databaseManager.rollback(ex);
        } finally {
            databaseManager.close();
        }

        return rubriqueParentList;
    }

    public void save(String nomRubriqueParent) {
        DatabaseAccess.save(new RubriqueParent(nomRubriqueParent));
    }

    public void update(int idRubriqueParent, String nomRubriqueParent) {
        RubriqueParent rubriqueParent = DatabaseAccess.get(RubriqueParent.class, idRubriqueParent);
        rubriqueParent.setNomRubriqueParent(nomRubriqueParent);
        DatabaseAccess.update(rubriqueParent);
    }

    public void update(String oldNomRubriqueParent, String newNomRubriqueParent) {
        List<RubriqueParent> rubriqueParentList = get(oldNomRubriqueParent);

        for (RubriqueParent rubriqueParent : rubriqueParentList) {
            rubriqueParent.setNomRubriqueParent(newNomRubriqueParent);
        }

        DatabaseAccess.update(rubriqueParentList);
    }

    public void delete(String nomRubriqueParent) {
        DatabaseAccess.delete(get(nomRubriqueParent));
    }
}
