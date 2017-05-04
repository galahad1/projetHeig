package ch.smartcity;

import ch.smartcity.carte.Carte;
import ch.smartcity.database.Database;
import ch.smartcity.pdf.GenerateurPDF;

public class SmartCity {

    public static void main(String[] args) throws Exception {
        Carte.main(null);
        Database.main(null);
        GenerateurPDF.main(null);
    }
}
