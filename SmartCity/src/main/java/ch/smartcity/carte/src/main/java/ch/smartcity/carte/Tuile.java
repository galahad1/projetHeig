package ch.smartcity.carte;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Représente une tuile de carte
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class Tuile {
    private final int zoom;
    private final int x;
    private final int y;
    private final BufferedImage image;

    /**
     * Crée une tuile de carte avec les coordonnées données selon une image
     * donnée
     *
     * @param zoom  Niveau de zoom de la carte pour la tuile
     * @param x     Coordonnée x de la tuile
     * @param y     Coordonnée y de la tuile
     * @param image L'image représentant la tuile, partie de la carte aux
     *              coordonnées données
     */
    Tuile(int zoom, int x, int y, BufferedImage image) {
        this.zoom = zoom;
        this.x = x;
        this.y = y;

        ColorModel cm = image.getColorModel();
        WritableRaster raster = image.copyData(null);
        // copie profonde de l'image
        this.image = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
    }


    /**
     * Retourne l'image correspondant à la tuile
     *
     * @return L'image de la tuile
     */
    public BufferedImage image() {
        return image;
    }


}
