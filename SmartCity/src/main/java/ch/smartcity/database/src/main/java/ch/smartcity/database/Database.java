package ch.smartcity.database;

import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.access.EvenementAccess;
import ch.smartcity.database.models.Evenement;
import org.hibernate.HibernateException;

import java.util.List;

public class Database {

    public static void main(String[] args) throws HibernateException {
        try {
            System.out.println("Tests\n=======================================================");
            List<Evenement> list = EvenementAccess.getActif();
            System.out.println(list);
            System.out.println("=======================================================\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseAccess.close();
        }
    }
}