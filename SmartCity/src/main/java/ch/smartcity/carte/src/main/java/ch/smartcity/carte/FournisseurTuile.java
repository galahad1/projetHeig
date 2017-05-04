package ch.smartcity.carte;

/**
 * Représente un fournisseur de tuile
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public interface FournisseurTuile {
    /**
     * Fournit la tuile de coordonnées données
     *
     * @param zoom Niveau de zoom de la carte pour la tuile
     * @param x    Coordonnée x de la tuile
     * @param y    Coordonnée y de la tuile
     * @return La tuile de coordonnées données
     */
    Tuile tileAt(int zoom, int x, int y);
}
