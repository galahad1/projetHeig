/**
 * Blabla
 */
package ch.smartcity.pdf.graphiques;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

public class GenerateurGraphique {

    // !! TODO: LOAN : Vérifier le bon fonctionnement des modifications !!
    public final static String CHEMIN_IMAGE = System.getProperty("user.home") + File.separator
            + "Documents" + File.separator + "Smartcity" + File.separator
            + "Images" + File.separator + "image.png";

    public GenerateurGraphique() throws URISyntaxException, IOException {

        Random generateurGraphe = new Random();
        int i = generateurGraphe.nextInt(2);

        JFreeChart chart;

        if (i == 0) {
            chart = GraphiqueCirculaire.cree();
        } else {
            chart = GraphiqueEnBarres.cree();
        }

        // !! TODO: LOAN : Vérifier le bonne dimension du graphique !!
        File file = new File(CHEMIN_IMAGE);
        file.getParentFile().mkdirs();
        file.createNewFile();
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

