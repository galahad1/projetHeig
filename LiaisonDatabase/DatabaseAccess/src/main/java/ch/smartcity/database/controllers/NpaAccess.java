package database.controllers;

import database.models.Npa;
import org.hibernate.Session;

import java.util.List;

public class NpaAccess {

    private final DatabaseManager databaseManager;

    public NpaAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<Npa> get(String numeroNpa) {
        List<Npa> npaList = null;

        try {
            Session session = databaseManager.openSession();

            npaList = session
                    .createNamedQuery(
                            databaseManager.getProperty("npa.namedQuery.findByNumeroNpa"),
                            Npa.class)
                    .setParameter(
                            databaseManager.getProperty("npa.parameter.numeroNpa"),
                            numeroNpa.toLowerCase()).list();

            databaseManager.commit();
        } catch (Exception ex) {
            databaseManager.rollback(ex);
        } finally {
            databaseManager.close();
        }

        return npaList;
    }

    public void save(String numeroNpa) {
        DatabaseAccess.save(new Npa(numeroNpa));
    }

    public void update(int idNpa, String numeroNpa) {
        Npa npa = DatabaseAccess.get(Npa.class, idNpa);
        npa.setNumeroNpa(numeroNpa);
        DatabaseAccess.update(npa);
    }

    public void update(String oldNumeroNpa, String newNumeroNpa) {
        List<Npa> npaList = get(oldNumeroNpa);

        for (Npa npa : npaList) {
            npa.setNumeroNpa(newNumeroNpa);
        }

        DatabaseAccess.update(npaList);
    }

    public void delete(String numeroNpa) {
        DatabaseAccess.delete(get(numeroNpa));
    }
}
