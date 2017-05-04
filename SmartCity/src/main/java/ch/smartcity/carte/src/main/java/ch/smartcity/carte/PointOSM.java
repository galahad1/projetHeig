package ch.smartcity.carte;

import static java.lang.Math.*;


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
     * @param zoom, x, y
     *              Le niveau de zoom du systéme de coordonnées OSM et les coordonnées x,y
     * @throws IllegalArgumentException Si le zoom est négatif ou si l'une des deux coordonnées n'est pas dans l'intervalle admissible
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
     * Retourne ce point mais dans un systéme de coordonnées OSM.
     *
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

    public int roundedX() {
        return (int) round(x);
    }

    /**
     * Retourne l'entier le plus proche de la coordonnée y du point.
     *
     * @return l'entier le plus proche de la coordonnée y du point.
     */

    public int roundedY() {
        return (int) round(y);
    }

    /**
     * Retourne le niveau de zoom du systéme de coordonnées du point.
     *
     * @return retourne le niveau de zoom du systéme de coordonnées du point.
     */

    public int zoom() {
        return zoom;
    }

    /**
     * Retourne ce méme point mais au niveau de zoom passé en argument.
     *
     * @param zoom Le niveau de zoom du systéme de coordonnées OSM.
     * @return Retourne le point au niveau de zoom passé en argument..
     * @throws IllegalArgumentException Si le zoom est négatif.
     */

    public PointOSM atZoom(int newZoom) {
        if (newZoom < 0)
            throw new IllegalArgumentException();
        int diffZoom = newZoom - zoom;
        double x = this.x * pow(2, diffZoom);
        double y = this.y * pow(2, diffZoom);
        return new PointOSM(newZoom, x, y);
    }

    /**
     * Retourne ce point mais dans un systéme de coordonnées WGS 84.
     *
     * @return retourne le point dans un systéme de coordonnées WGS 84.
     */

    public PointWGS84 toWGS84() {

        double s = pow(2, zoom + 8);
        double longitude = ((2 * PI) / s) * x - PI;
        double latitude = atan(sinh(PI - ((2 * PI) / s) * y));
        return new PointWGS84(longitude, latitude);
    }

    /**
     * Redéfinit la méthode toString et retourne une représentation textuelle du point.
     *
     * @return une représentation textuelle du point
     */

    public String toString() {
        return "(" + zoom + "," + x + "," + y + ")";
    }
}
