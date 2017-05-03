/**
 * PROJET HEIG - VD SMARTCITY
 * Classe : GraphiqueEnBarres
 * <p>
 * Descriptif : Cette classe cree un graphique en barre. Toutes les informations sont extraites de la base de données
 */
package ch.smartcity.pdf.graphiques;

import ch.smartcity.pdf.donnees.Mois;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;

public class GraphiqueEnBarres {

    /* Constructeur privé */
    private GraphiqueEnBarres() {
    }

    /**
     * Méthode principale
     * Crée un graphique en barre
     * Les données sont extraites de la base de données
     * @return une instance représentant un graphique en barre
     */
    public static JFreeChart cree() {

        DefaultCategoryDataset ds = new DefaultCategoryDataset();

        double[] valeurs = {7, 15, 6, 1, 21, 10, 4, 3, 9, 11, 5, 10};

        /* Insère les valeurs selons les dates */
        DefaultPieDataset pie = new DefaultPieDataset();
        for (int i = 0; i < valeurs.length; i++) {
            pie.setValue(Mois.values()[i].toString().substring(0, 3), valeurs[i]);
            ds.setValue(valeurs[i], "", Mois.values()[i].toString().substring(0, 3));
        }

        /* Création graphique du diagramme */
        JFreeChart chart = ChartFactory.createBarChart("", "", "", ds,
                PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.WHITE);
        p.setRangeGridlinesVisible(false);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);

        CategoryAxis axeX = plot.getDomainAxis();
        ValueAxis axeY = plot.getRangeAxis(0);

        axeX.setTickLabelFont(new Font("Times", Font.PLAIN, 18));
        axeY.setTickLabelFont(new Font("Times", Font.PLAIN, 18));

        AbstractCategoryItemRenderer render = (AbstractCategoryItemRenderer) plot.getRenderer();
        render.setSeriesPaint(0, Color.BLUE);

        return chart;
    }
}
