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

    private static final int NB_GRAPHES = 2;

    public final static String CHEMIN_IMAGE = System.getProperty("user.home") + File.separator
            + "Documents" + File.separator + "Smartcity" + File.separator
            + "Images" + File.separator + "image.png";

    public GenerateurGraphique(int[] stats) throws URISyntaxException, IOException {

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
        } catch (Exception e) {
            System.out.println("Error while loading chart !");
        }

    }

}

