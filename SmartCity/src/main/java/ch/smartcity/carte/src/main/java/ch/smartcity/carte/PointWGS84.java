package ch.smartcity.carte;

import static java.lang.Math.*;

/**
 * Modélise un point dans le systéme de coordonnées WGS 84.
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class PointWGS84 {

    private final double latitude;    // degrés décimaux
    private final double longitude;   // degrés décimaux

    /**
     * Construit un point WGS84 grâce aux degrés decémiaux des coordonnées.
     *
     * @param latitude  en degrés décimaux
     * @param longitude en degrés décimaux
     * @throws IllegalArgumentException Si les degrés donnés ne sont pas compris dans les bornes
     *                                  du système de coordonnées
     */
    public PointWGS84(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90)
            throw new IllegalArgumentException();

        if (longitude < -180 || longitude > 180)
            throw new IllegalArgumentException();

        this.longitude = Math.toRadians(longitude);
        this.latitude = Math.toRadians(latitude);
    }

    /**
     * Permet de transformer un point WGS84 en point OSM spécifique a une tuile
     *
     * @param zoom zoom voulu du point OSM
     * @return le point OSM
     */
    public PointOSM toOSM(int zoom) {
        if (zoom < 0)
            throw new IllegalArgumentException();
        double s = pow(2, zoom + 8);
        double x = (s / (2 * PI)) * (longitude + PI);
        double y = (s / (2 * PI)) * (PI - Utils.asinh(tan(latitude)));
        return new PointOSM(zoom, x, y);
    }
}
