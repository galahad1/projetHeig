package ch.smartcity;

import ch.smartcity.database.Database;
import ch.smartcity.graphique.FenetrePrincipale;
import ch.smartcity.pdf.GenerateurPDF;

public class SmartCity {

    public static void main(String[] args) throws Exception {
        FenetrePrincipale fenetre = new FenetrePrincipale();
        fenetre.fenetre.setVisible(true);

        final int contexte_EnAttente = 1;
        final int contexte_Ajouter = 0;

//      FenetreModification fenetre2 = new FenetreModification(1);
//      fenetre2.fenetre.setVisible(true);

//		fenetre2.frame.setAlwaysOnTop(true);
//		fenetre.fenetre.setEnabled(false);
//		Thread.sleep(5000);
//		fenetre2.frame.dispose();
//		fenetre.fenetre.setEnabled(true);

        Database.main(null);
        GenerateurPDF.main(null);
    }
}
