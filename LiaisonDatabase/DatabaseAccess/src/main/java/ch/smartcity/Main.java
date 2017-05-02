import database.controllers.DatabaseAccess;
import database.models.*;
import org.hibernate.HibernateException;

import java.util.List;

public class Main {

    public static void main(String[] args) throws HibernateException {
//        TODO: Pour chaque objet, tester l'obtention
//        TODO: Pour chaque objet, tester l'insertion
//        TODO: Pour chaque objet, tester la mise à jour
//        TODO: Pour chaque objet, tester la suppression

//        TODO: Implémenter des requêtes personnalisée
//        TODO: Aperçu des évènements pour la liste déroulante
//        TODO: Liste des évènements en attente de validation
//        TODO: Liste des évènements en fonction d'une rubrique et d'une date

//        TODO: Accès à la base de données par plusieurs clients en même temps
//        TODO: Mise en place de la base de données et du gestionnaire sur un serveur

        try {
            System.out.println("Adresses\n=======================================================");
            List<Adresse> adresseList = DatabaseAccess.get(Adresse.class);
            System.out.println(adresseList);
            System.out.println("=======================================================\n");

            System.out.println("Commentaires\n===================================================");
            List<Commentaire> commentaireList = DatabaseAccess.get(Commentaire.class);
            System.out.println(commentaireList);
            System.out.println("=======================================================\n");

            System.out.println("Confiances\n=====================================================");
            List<Confiance> confianceList = DatabaseAccess.get(Confiance.class);
            System.out.println(confianceList);
            System.out.println("=======================================================\n");

            System.out.println("Evenements\n=====================================================");
            List<Evenement> evenementList = DatabaseAccess.get(Evenement.class);
            System.out.println(evenementList);
            System.out.println("=======================================================\n");

            System.out.println("Nationalite\n====================================================");
            List<Nationalite> nationaliteList = DatabaseAccess.get(Nationalite.class);
            System.out.println(nationaliteList);
            System.out.println("=======================================================\n");

            System.out.println("Npas\n===========================================================");
            List<Npa> npaList = DatabaseAccess.get(Npa.class);
            System.out.println(npaList);
            System.out.println("=======================================================\n");

            System.out.println("Priorites\n======================================================");
            List<Priorite> prioriteList = DatabaseAccess.get(Priorite.class);
            System.out.println(prioriteList);
            System.out.println("=======================================================\n");

            System.out.println("RubriqueEnfants\n================================================");
            List<RubriqueEnfant> rubriqueEnfantList = DatabaseAccess.get(RubriqueEnfant.class);
            System.out.println(rubriqueEnfantList);
            System.out.println("=======================================================\n");

            System.out.println("RubriqueParents\n================================================");
            List<RubriqueParent> rubriqueParentList = DatabaseAccess.get(RubriqueParent.class);
            System.out.println(rubriqueParentList);
            System.out.println("=======================================================\n");

            System.out.println("Rues\n===========================================================");
            List<Rue> rueList = DatabaseAccess.get(Rue.class);
            System.out.println(rueList);
            System.out.println("=======================================================\n");

            System.out.println("Sexes\n==========================================================");
            List<Sexe> sexeList = DatabaseAccess.get(Sexe.class);
            System.out.println(sexeList);
            System.out.println("=======================================================\n");

            System.out.println("Statuts\n========================================================");
            List<Statut> statutList = DatabaseAccess.get(Statut.class);
            System.out.println(statutList);
            System.out.println("=======================================================\n");

            System.out.println("TitreCivils\n=====================================================");
            List<TitreCivil> titreCivilList = DatabaseAccess.get(TitreCivil.class);
            System.out.println(titreCivilList);
            System.out.println("=======================================================\n");

            System.out.println("Utilisateurs\n===================================================");
            List<Utilisateur> utilisateursList = DatabaseAccess.get(Utilisateur.class);
            System.out.println(utilisateursList);
            System.out.println("=======================================================\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseAccess.close();
        }
    }
}
