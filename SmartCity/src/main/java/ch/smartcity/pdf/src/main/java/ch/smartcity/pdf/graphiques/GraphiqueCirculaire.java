package ch.smartcity.pdf.graphiques;

import ch.smartcity.pdf.donnees.Mois;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.text.DecimalFormat;

public class GraphiqueCirculaire {

    /* Constructeur privé */
    private GraphiqueCirculaire() {
    }

    /**
     * Fonction principale.
     * Crée un graphique circulaire
     *
     * @return une instance représentant un graphique circulaire
     */
    public static JFreeChart cree() {

        double[] valeurs = {7, 15, 6, 1, 21, 10, 4, 3, 9, 11, 5, 10};

        double total = 0;
        for (int i = 0; i < valeurs.length; i++) {
            total += valeurs[i];
        }

        DefaultPieDataset pie = new DefaultPieDataset();

        for (int i = 0; i < valeurs.length; ++i) {
            pie.setValue(Mois.values()[i], valeurs[i]);
        }

        JFreeChart chart = ChartFactory.createPieChart("", pie, false, false, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(null);

        //plot.setLabelGenerator(null);
        plot.setOutlineVisible(false);

        StandardPieSectionLabelGenerator label
                = new StandardPieSectionLabelGenerator("{0} : {1}",
                new DecimalFormat("#,##0"),
                new DecimalFormat("0"));
        plot.setLabelGenerator(label);
        plot.setLabelFont(new Font("Times", Font.PLAIN, 28));
        plot.setLabelBackgroundPaint(Color.WHITE);

        for (int i = 0; i < Mois.values().length; ++i) {
            plot.setExplodePercent(Mois.values()[i], 0.05);
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

}
