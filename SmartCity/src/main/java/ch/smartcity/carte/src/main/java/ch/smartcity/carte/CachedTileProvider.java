package ch.smartcity.carte;

/**
 * Représente un transformateur de fournisseur de tuiles qui fait office de
 * cache
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class CachedTileProvider implements TileProvider {

    private final TileCache tileCache;
    private final TileProvider tileProvider;

    /**
     * Construit un transformateur de fournisseur de tuiles
     *
     * @param tileProvider Le fournisseur de tuile à transformer
     */
    public CachedTileProvider(TileProvider tileProvider) {
        this.tileCache = new TileCache();
        this.tileProvider = tileProvider;
    }

    @Override
    public Tile tileAt(int zoom, int x, int y) {
        Tile cachedTile = tileCache.get(zoom, x, y);
        if (cachedTile != null) {
            return cachedTile;
        }
        Tile notCachedTile = tileProvider.tileAt(zoom, x, y);
        tileCache.put(zoom, x, y, notCachedTile);
        return notCachedTile;
    }

}
