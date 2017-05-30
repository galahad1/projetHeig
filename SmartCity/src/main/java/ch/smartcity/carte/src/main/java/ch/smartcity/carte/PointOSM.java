package ch.smartcity.carte;

import static java.lang.Math.pow;
import static java.lang.Math.round;

/**
 * Modélise un point dans le systéme de coordonnées OSM.
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class PointOSM {

    private final int zoom;
    private final double x;
    private final double y;

    /**
     * Construit un point de coordonnées x et y au niveau de zoom donné.
     *
     * @param zoom, x, y Le niveau de zoom du système de coordonnées OSM et les coordonnées x,y
     * @throws IllegalArgumentException Si le zoom est négatif ou si l'une des deux coordonnées
     *                                  n'est pas dans l'intervalle admissible
     */
    public PointOSM(int zoom, double x, double y) {
        double max = maxXY(zoom);
        if (zoom < 0 || x > max || x < 0 || y < 0 || y > max)
            throw new IllegalArgumentException();

        this.zoom = zoom;
        this.x = x;
        this.y = y;
    }

    /**
     * @param zoom Le niveau de zoom du systéme de coordonnées OSM.
     * @return retourne la taille de l'image de la carte du monde au niveau de zoom donné.
     * @throws IllegalArgumentException Si le zoom est négatif.
     */
    public static int maxXY(int zoom) {
        if (zoom < 0)
            throw new IllegalArgumentException();

        return (int) pow(2, zoom + 8);
    }

    /**
     * Retourne la coordonnée x du point.
     *
     * @return la coordonnée x du point.
     */
    public double x() {
        return x;
    }

    /**
     * Retourne la coordonnée y du point.
     *
     * @return la coordonnée y du point.
     */
    public double y() {
        return y;
    }

    /**
     * Retourne l'entier le plus proche de la coordonnée x du point.
     *
     * @return l'entier le plus proche de la coordonnée x du point.
     */
    public int arrondiX() {
        return (int) round(x);
    }

    /**
     * Retourne l'entier le plus proche de la coordonnée y du point.
     *
     * @return l'entier le plus proche de la coordonnée y du point.
     */
    public int arrondiY() {
        return (int) round(y);
    }
}
