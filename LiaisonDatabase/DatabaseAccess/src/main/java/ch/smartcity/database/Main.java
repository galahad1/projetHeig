import controllers.DatabaseAccess;
import models.Npa;

import java.util.List;

public class Main {

    public static void main(String[] args) {
//        TODO: Tester la suppressioon d'élément
//        TODO: Organiser ORMAcces en plusieurs parties
//        TODO: Gérer les tables intermidiares (UtilisateurConfiaceEvenement,EvenemenntUtilisateur)
//        TODO: Créer queryNamed
//        TODO: Remplir la base de données
//        TODO: Système de log
//        TODO: Exceptions personnalisées
        try {
            DatabaseAccess databaseAccess = new DatabaseAccess();

            Npa npa = databaseAccess.getNpa(1);
            System.out.println(npa);

            List<Npa> npaList1004 = databaseAccess.getNpaList("1004");
            System.out.println(npaList1004);

            List<Npa> npaListAll = databaseAccess.getNpaList();
            System.out.println(npaListAll);
//
//            databaseAccess.saveNpa(new Npa(0, "1111", Calendar.getInstance()));
//
//            npaList = databaseAccess.getNpaList();
//            System.out.println(npaList);
//
//            npaList.get(0).setNumeroNpa("6666");
//            databaseAccess.updateNpaList(npaList);
//
//            npaList = databaseAccess.getNpaList();
//            System.out.println(npaList);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DatabaseAccess.terminate();
        }
    }
}
