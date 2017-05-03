/**
 * Blabla
 */
package ch.smartcity.pdf.graphiques;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.util.Random;

public class GenerateurGraphique {

    public final static String CHEMIN_IMAGE = "image.png";

    public GenerateurGraphique() {

        Random generateurGraphe = new Random();
        int i = generateurGraphe.nextInt(2);

        JFreeChart chart;

        if (i == 0) {
            chart = GraphiqueCirculaire.cree();
        } else {
            chart = GraphiqueEnBarres.cree();
        }


        File file = new File(CHEMIN_IMAGE);
        try {
            ChartUtilities.saveChartAsPNG(file, chart, 800, 600);
        } catch (Exception e) {
            // Gestion d'erreur
        }

    }

    // retourne le nombre de données pour le rapport
    public double getNombreDonnees() {
        return 0.;
    }

    // retourne le total de l'addition de toutes les données
    public double getTotalDonnees() {
        return 0.;
    }

}

