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


}
