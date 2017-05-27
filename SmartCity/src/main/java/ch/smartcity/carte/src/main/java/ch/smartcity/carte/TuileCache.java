package ch.smartcity.carte;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Représente un cache de tuiles
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class TuileCache {

    private static final int TAILLE_MAX = 1000; // nombre de tuiles en cache
    private LinkedHashMap<Long, Tuile> cache = new LinkedHashMap<Long, Tuile>() {
        private static final long serialVersionUID = 1L;

        @Override
        //redéfinie la taile maximum du HashMap (FIFO)
        protected boolean removeEldestEntry(Map.Entry<Long, Tuile> e) {
            return size() > TAILLE_MAX;
        }
    };

    /**
     * Mémorise une tuile de la carte
     *
     * @param zoom  Niveau de zoom de la carte pour la tuile
     * @param x     Coordonnée x de la tuile
     * @param y     Coordonnée y de la tuile
     * @param tuile La tuile à placer dans le HashMap
     */
    public void put(int zoom, int x, int y, Tuile tuile) {
        cache.put(getCleCoordonnee(zoom, x, y), tuile);
    }

    /**
     * Recherche et retourne (si disponible) une tuile mémorisée selon les
     * coordonnées données
     *
     * @param zoom Niveau de zoom de la carte pour la tuile
     * @param x    Coordonnée x de la tuile
     * @param y    Coordonnée y de la tuile
     * @return La tuile (si disponible) aux coordonnées données, null sinon
     * (soit parce qu'elle n'y a jamais été ajoutée, soit parce qu'elle
     * a été supprimée aprés que le cache ait atteint sa taille
     * maximale)
     */
    public Tuile get(int zoom, int x, int y) {
        return cache.get(getCleCoordonnee(zoom, x, y));
    }

    /**
     * Permet de retrouver la clé de la tuile selon les coordonées et le zoom
     * Encode les coordonnées en un seul long, 2 chiffres pour le zoom, 7
     * chiffres pour la coordonnée x et autant pour la coordonnées y
     * pour en faire une clé du HashMap
     *
     * @param zoom
     * @param x
     * @param y
     * @return La clé du Hashmap pour retrouvé la tuile
     */
    private long getCleCoordonnee(int zoom, int x, int y) {
        return (long) (zoom * Math.pow(10, 14) + x * Math.pow(10, 7) + y);
    }
}
