package ch.smartcity.carte;
/**
 * Modélise un point dans le systéme de coordonnées WGS 84.
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */

import static java.lang.Math.*;

public final class PointWGS84 {
    private final double longitude;
    private final double latitude;

    public PointWGS84(double longitude, double latitude) {

        if (longitude < -PI || longitude > PI)
            throw new IllegalArgumentException();

        if (latitude < -(PI / 2) || latitude > PI / 2)
            throw new IllegalArgumentException();

        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double longitude() {
        return longitude;
    }

    public double latitude() {
        return latitude;
    }

    public double distanceTo(PointWGS84 that) {
        return 2 * 6378137 * asin(sqrt(Utils.haversin(latitude - that.latitude())
                + cos(latitude) * cos(that.latitude())
                * Utils.haversin(longitude - that.longitude())));
    }

    public PointOSM toOSM(int zoom) {
        if (zoom < 0)
            throw new IllegalArgumentException();
        double s = pow(2, zoom + 8);
        double x = (s / (2 * PI)) * (longitude + PI);
        double y = (s / (2 * PI)) * (PI - Utils.asinh(tan(latitude)));
        return new PointOSM(zoom, x, y);
    }

    public String toString() {
        return "(" + Utils.radToDeg(longitude) + "," + Utils.radToDeg(latitude) + ")";
    }
}
