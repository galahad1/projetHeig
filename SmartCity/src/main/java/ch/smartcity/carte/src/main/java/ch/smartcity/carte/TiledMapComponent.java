package ch.smartcity.carte;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un composant Swing capable d'afficher une carte en tuiles
 * (fournies par des fournisseurs de tuiles)
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class TiledMapComponent extends JComponent {

    private static final long serialVersionUID = 1L;
    private final int TILESIZE = 256;
    private int zoom;
    private List<TileProvider> tileProviders;

    /**
     * Crée un composant Swing capable d'afficher une carte en tuiles
     *
     * @param zoom Le niveau de zoom de la carte
     * @throws IllegalArgumentException Léve l'exeption si le niveau de zoom est inférieur é 10 ou
     *                                  supérieur é 19.
     */
    TiledMapComponent(int zoom) {
        if (zoom < 10 || zoom > 19) {
            throw new IllegalArgumentException();
        }
        this.zoom = zoom;
        tileProviders = new ArrayList<TileProvider>();
    }

    /**
     * Retourne le niveau de zoom de la carte
     *
     * @return Le niveau de zoom de la carte
     */
    public int zoom() {
        return zoom;
    }

    /**
     * Modifie le niveau de zoom de la carte
     *
     * @param zoom Le nouveau niveau de zoom de la carte
     * @throws IllegalArgumentException Léve l'exeption si le niveau de zoom est inférieur é 10 ou
     *                                  supérieur é 19.
     */
    public boolean setZoom(int zoom) {
        if (zoom >= 10 && zoom <= 19) {
            this.zoom = zoom;
            repaint();
            return true;
        }
        return false;
    }

    /**
     * Ajoute un nouveau fournisseur de tuiles é la liste des fournisseurs de la
     * carte
     *
     * @param tileProvider Un nouveau fournisseur de tuiles de la carte
     */
    public void addTileProviders(TileProvider tileProvider) {
        tileProviders.add(tileProvider);
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        int size = PointOSM.maxXY(zoom);
        return new Dimension(size, size);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        g0 = (Graphics2D) g0;

        g0.setColor(Color.black);
        // fenétre de visualisation du component
        Rectangle visibleRect = getVisibleRect();

        int baseX = (visibleRect.x / TILESIZE) * TILESIZE;
        int baseY = (visibleRect.y / TILESIZE) * TILESIZE;
        int maxX = visibleRect.x + visibleRect.width;
        int maxY = visibleRect.y + visibleRect.height;

        for (TileProvider tileProvider : tileProviders) {
            for (int x = baseX; x < maxX; x += TILESIZE) {
                for (int y = baseY; y < maxY; y += TILESIZE) {
                    int tilePosX = x / TILESIZE;
                    int tilePosY = y / TILESIZE;
                    Tile t = tileProvider.tileAt(zoom, tilePosX, tilePosY);
                    g0.drawImage(t.image(), x, y, null);

                }
            }
        }

    }

}
