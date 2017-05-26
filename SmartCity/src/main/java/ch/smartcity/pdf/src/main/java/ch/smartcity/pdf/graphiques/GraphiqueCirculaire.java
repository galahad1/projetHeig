package ch.smartcity.pdf.graphiques;

import ch.smartcity.pdf.donnees.Mois;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Classe qui génère un graphique circulaire
 *
 * @author Luana Martelli
 */
class GraphiqueCirculaire {

    /**
     * Crée un graphique circulaire
     * @param stats les statistiques relatives au graphique
     * @return un nouveau graphe
     */
    @SuppressWarnings( "deprecation" )
    static JFreeChart cree(int[] stats) {

        DefaultPieDataset pie = new DefaultPieDataset();

        /* Ajout des données */
        for (int i = 0; i < stats.length; ++i) {
            if (stats[i] != 0)
                pie.setValue(Mois.values()[i], stats[i]);
        }

        JFreeChart chart = ChartFactory.createPieChart("", pie, false, false, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);

        plot.setOutlineVisible(false);

        StandardPieSectionLabelGenerator label
                = new StandardPieSectionLabelGenerator("{0} : {1}",
                new DecimalFormat("#,##0"),
                new DecimalFormat("0"));
        plot.setLabelGenerator(label);
        plot.setLabelFont(new Font("Times", Font.PLAIN, 28));
        plot.setLabelBackgroundPaint(Color.WHITE);

        for (int i = 0; i < Mois.values().length; ++i) {
            plot.setExplodePercent(Mois.values()[i], 0.01);
        }

        plot.setSectionPaint(1, new Color(0x6FB9F2));
        plot.setSectionPaint(2, new Color(0x976FF2));
        plot.setSectionPaint(3, new Color(0xE06FF2));
        plot.setSectionPaint(4, new Color(0x6FD3F2));
        plot.setSectionPaint(5, new Color(0x6F97F2));
        plot.setSectionPaint(6, new Color(0xB96FF2));
        plot.setSectionPaint(7, new Color(0x6F6FF2));

        return chart;
    }

    /* Constructeur privé */
    private GraphiqueCirculaire() {
    }
}
