package ch.smartcity.pdf.graphiques;

import ch.smartcity.pdf.GenerateurPDF;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe qui génère un graphique pour le document PDF
 *
 * @author Luana Martelli
 */
public class GenerateurGraphique {

    public final static String CHEMIN_IMAGE =
            System.getProperty("user.home") + File.separator
                    + "Smartcity" + File.separator
            + "Images" + File.separator + "image.png";
    /**
     * Utilisé pour journaliser les actions effectuées
     */
    private static final Logger LOGGER = Logger.getLogger(GenerateurPDF.class.getName());
    private static final int NB_GRAPHES = 2;

    /**
     * Crée un grahique
     *
     * @param stats les statistiques du graphique
     * @throws IOException si il y  a un problème lors de la génération de l'image dans un fichier
     */
    public GenerateurGraphique(int[] stats) throws IOException {

        /* Génère de manière aléatoire un graphique en barre ou un graphique circulaire */
        Random generateurGraphe = new Random();
        int i = generateurGraphe.nextInt(NB_GRAPHES);

        JFreeChart chart;

        if (i % NB_GRAPHES == 0) {
            chart = GraphiqueCirculaire.cree(stats);
        } else {
            chart = GraphiqueEnBarres.cree(stats);
        }

        File file = new File(CHEMIN_IMAGE);
        file.getParentFile().mkdirs();
        file.createNewFile();

        try {
            ChartUtilities.saveChartAsPNG(file, chart, 900, 700);
            LOGGER.info("Success of generation of chart "
                    + CHEMIN_IMAGE.substring(CHEMIN_IMAGE.lastIndexOf(File.separator) + 1));
        } catch (Exception e) {
            LOGGER.severe("Error while loading chart !");
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }
}

