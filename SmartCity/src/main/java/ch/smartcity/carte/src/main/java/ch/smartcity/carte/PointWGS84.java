package ch.smartcity.carte;
/**
 * Modélise un point dans le systéme de coordonnées WGS 84.
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */

import static java.lang.Math.*;

public final class PointWGS84 {

    private final double latitude;
    private final double longitude;


    public PointWGS84(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90)
            throw new IllegalArgumentException();

        if (longitude < -180 || longitude > 180)
            throw new IllegalArgumentException();


        this.longitude = Math.toRadians(longitude);
        this.latitude = Math.toRadians(latitude);
    }

    public PointOSM toOSM(int zoom) {
        if (zoom < 0)
            throw new IllegalArgumentException();
        double s = pow(2, zoom + 8);
        double x = (s / (2 * PI)) * (longitude + PI);
        double y = (s / (2 * PI)) * (PI - Utils.asinh(tan(latitude)));
        return new PointOSM(zoom, x, y);
    }

}
