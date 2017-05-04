package ch.smartcity.carte;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Représente un fournisseur de tuile contenant des points de couleurs selon
 * l'emplacement d'événements
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class FournisseurTuilePointCouleur implements FournisseurTuile {
    public final static int DIMEN = 256;

    private ArrayList<Evenement> evenements;
    private Color[] couleur = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE,
            Color.MAGENTA};

    /**
     * Crée un fournisseur de tuile isochrone avec l'arbre des trajets, la table
     * des couleurs et la vitesse de marche donnés
     *
     * @param
     **/
    public FournisseurTuilePointCouleur(ArrayList<Evenement> evenements) {
        this.evenements = evenements;
    }

    @Override
    public Tuile tileAt(int zoom, int x, int y) {
        // création d'une tuile
        BufferedImage i = new BufferedImage(DIMEN, DIMEN, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = i.createGraphics();

        int rayon = 5;
        for (Evenement e : evenements) {
            g.setColor(couleur[e.getCategorie()]);
            Ellipse2D ellipse = new Ellipse2D.Double(e.position().toOSM(zoom).x() - DIMEN * x - rayon,
                    e.position().toOSM(zoom).y() - DIMEN * y - rayon, rayon * 2, rayon * 2);
            g.fill(ellipse);

            // contour en noir
            g.setColor(Color.black);
            g.draw(ellipse);

            g.setColor(Color.white);
            g.setFont(new Font(Font.SERIF, 1, 15));
            // permet d'avoir un bord blanc autour de l'écriture
            g.drawString(e.name(), e.position().toOSM(zoom).roundedX() - DIMEN * x - 6,
                    e.position().toOSM(zoom).roundedY() - DIMEN * y - 9);
            g.drawString(e.name(), e.position().toOSM(zoom).roundedX() - DIMEN * x - 6,
                    e.position().toOSM(zoom).roundedY() - DIMEN * y - 7);
            g.drawString(e.name(), e.position().toOSM(zoom).roundedX() - DIMEN * x - 4,
                    e.position().toOSM(zoom).roundedY() - DIMEN * y - 9);
            g.drawString(e.name(), e.position().toOSM(zoom).roundedX() - DIMEN * x - 4,
                    e.position().toOSM(zoom).roundedY() - DIMEN * y - 7);
            g.setColor(Color.black);
            g.drawString(e.name(), e.position().toOSM(zoom).roundedX() - DIMEN * x - 5,
                    e.position().toOSM(zoom).roundedY() - DIMEN * y - 8);
            ;
        }

        return new Tuile(zoom, x, y, i);
    }

}
