package database.controllers;

import database.models.Sexe;
import org.hibernate.Session;

import java.util.List;

public class SexeAccess {

    private final DatabaseManager databaseManager;

    public SexeAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<Sexe> get(String nomSexe) {
        List<Sexe> sexeList = null;

        try {
            Session session = databaseManager.openSession();

            sexeList = session
                    .createNamedQuery(
                            databaseManager.getProperty("sexe.namedQuery.findByNomSexe"),
                            Sexe.class)
                    .setParameter(
                            databaseManager.getProperty("sexe.parameter.nomSexe"),
                            nomSexe.toLowerCase()).list();

            databaseManager.commit();
        } catch (Exception ex) {
            databaseManager.rollback(ex);
        } finally {
            databaseManager.close();
        }

        return sexeList;
    }

    public void save(String nomSexe) {
        DatabaseAccess.save(new Sexe(nomSexe));
    }

    public void update(int idSexe, String nomSexe) {
        Sexe sexe = DatabaseAccess.get(Sexe.class, idSexe);
        sexe.setNomSexe(nomSexe);
        DatabaseAccess.update(sexe);
    }

    public void update(String oldNomSexe, String newNomSexe) {
        List<Sexe> sexeList = get(oldNomSexe);

        for (Sexe npa : sexeList) {
            npa.setNomSexe(newNomSexe);
        }

        DatabaseAccess.update(sexeList);
    }

    public void delete(String nomSexe) {
        DatabaseAccess.delete(get(nomSexe));
    }
}
