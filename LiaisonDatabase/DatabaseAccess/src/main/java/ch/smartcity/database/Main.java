import controllers.DatabaseAccess;
import models.Npa;

import java.util.List;

public class Main {

    public static void main(String[] args) {
//        TODO: Définir en détails npa.hbm.xml

//        TODO: Organiser DatabaseAccess en plusieurs parties
//        TODO: Gérer les liaisons MANY-TO-MANY, MANY-TO-ONE, ONE-TO-MANY
//        TODO: Orgaiser les valeurs en dur (string, int) dans des fichiers
//        TODO: Remplir la base de données
//        TODO: Créer un système de log
//        TODO: Créer des exceptions personnalisées

//        TODO: Pour chaque objet, définir en détails le hbm.xml correspondant
//        TODO: Pour chaque objet, tester l'obtention
//        TODO: Pour chaque objet, tester l'insertion
//        TODO: Pour chaque objet, tester la mise à jour
//        TODO: Pour chaque objet, tester la suppression

//        TODO: aperçu des évènements pour la liste déroulante
//        TODO: liste des évènements en attente de validation
//        TODO: liste des évènements en fonction d'une rubrique et d'une date

        try {
            DatabaseAccess databaseAccess = new DatabaseAccess();

            List<Npa> npaListAll = databaseAccess.getNpa();
            System.out.println(npaListAll);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DatabaseAccess.terminate();
        }
    }
}
