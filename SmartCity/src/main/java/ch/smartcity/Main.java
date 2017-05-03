package ch.smartcity;

import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.models.Adresse;
import org.hibernate.HibernateException;

import java.util.List;

public class Main {

    public static void main(String[] args) throws HibernateException {
        try {
            System.out.println("Adresses\n=======================================================");
            List<Adresse> adresseList = DatabaseAccess.get(Adresse.class);
            System.out.println(adresseList);
            System.out.println("=======================================================\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseAccess.close();
        }
    }
}
