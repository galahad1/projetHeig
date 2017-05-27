package ch.smartcity.carte;

/**
 * Représente un transformateur de fournisseur de tuiles qui fait office de
 * cache
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class FournisseurTuileCache implements FournisseurTuile {

    private final TuileCache tuileCache;
    private final FournisseurTuile fournisseurTuile;

    /**
     * Constructeur qui transforme un fournisseur de tuiles en fournisseurTuileCache
     *
     * @param fournisseurTuile Le fournisseur de tuiles à transformer
     */
    public FournisseurTuileCache(FournisseurTuile fournisseurTuile) {
        this.tuileCache = new TuileCache();
        this.fournisseurTuile = fournisseurTuile;
    }

    @Override
    public Tuile getTuile(int zoom, int x, int y) {
        Tuile tuileEnCache = tuileCache.get(zoom, x, y);
        if (tuileEnCache != null) {
            return tuileEnCache;
        }
        Tuile nouvelleTuile = fournisseurTuile.getTuile(zoom, x, y);
        tuileCache.put(zoom, x, y, nouvelleTuile);
        return nouvelleTuile;
    }
}
