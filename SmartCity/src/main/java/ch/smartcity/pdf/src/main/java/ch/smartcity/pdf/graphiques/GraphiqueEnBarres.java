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

class GraphiqueEnBarres {

    /**
     * Crée un graphique en barres
     * @param stats les statistiques relatives au graphique
     * @return un nouveau graphe
     */
    static JFreeChart cree(int[] stats) {

        DefaultCategoryDataset ds = new DefaultCategoryDataset();

        /* Insère les valeurs selons les dates */
        DefaultPieDataset pie = new DefaultPieDataset();
        for (int i = 0; i < stats.length; i++) {
            if (stats[i] != 0) {
                pie.setValue(Mois.values()[i].toString().substring(0, 3), stats[i]);
                ds.setValue(stats[i], "", Mois.values()[i].toString().substring(0, 3));
            }
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

    /* Constructeur privé */
    private GraphiqueEnBarres() {
    }
}
