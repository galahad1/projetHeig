import controllers.DatabaseAccess;

public class Main {

    public static void main(String[] args) {
//        TODO: Organiser DatabaseAccess en plusieurs parties
//        TODO: Créer des fonctions avec templates dans DatabaseAccess
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
            System.out.println(DatabaseAccess.NPA_ACCESS.getNpa());
            System.out.println(DatabaseAccess.NPA_ACCESS.getNpa(1));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DatabaseAccess.close();
        }
    }
}
