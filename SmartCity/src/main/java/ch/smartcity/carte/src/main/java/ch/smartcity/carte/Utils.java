package ch.smartcity.carte;

import static java.lang.Math.*;

/**
 * Classe utilitaire de calculs mathématiques
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class Utils {

    /**
     * constructeur par défaut en private pour rendre la classe non-instanciable
     */
    private Utils() {
    }

    /**
     * retourne le resultat de la fonction arcsinh
     *
     * @param x la valeur x du point pour lequel calculer arcsinh
     * @return La valeur de la fonction arcsinh
     */
    public static double asinh(double x) {
        return log(x + sqrt(1 + pow(x, 2)));
    }

    /**
     * retourne le resultat de la fonction haversin
     *
     * @param x la valeur x du point pour lequel calculer haversin
     * @return La valeur de la fonction haversin
     */
    public static double haversin(double x) {
        return pow(sin(x / 2), 2);
    }

    /**
     * convertit un angle de radians vers degré
     *
     * @param angle Une valeur d'angle en radians
     * @return L'angle en degré
     */
    public static double radToDeg(double angle) {
        return 180 * angle / PI;
    }

    public static double degToRad(double angle) {
        return PI * angle / 180;
    }


}
