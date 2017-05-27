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
public final class CarteTuilesComponent extends JComponent {

    private static final long serialVersionUID = 1L;
    private static final int TAILLE_TUILE = 256;
    private int zoom;
    private List<FournisseurTuile> fournisseurTuiles;

    /**
     * Crée un composant Swing capable d'afficher une carte en tuiles
     *
     * @param zoom Le niveau de zoom de la carte
     * @throws IllegalArgumentException Léve l'exeption si le niveau de zoom est inférieur à 10 ou
     *                                  supérieur à 19.
     */
    CarteTuilesComponent(int zoom) {
        if (zoom < 10 || zoom > 19) {
            throw new IllegalArgumentException();
        }
        this.zoom = zoom;
        fournisseurTuiles = new ArrayList<>();
    }

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
     * @param fournisseurTuile Un nouveau fournisseur de tuiles de la carte
     */
    public void ajoutFournisseurTuile(FournisseurTuile fournisseurTuile) {
        fournisseurTuiles.add(fournisseurTuile);
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
        // fenêtre de visualisation du component
        Rectangle visibleRect = getVisibleRect();

        int baseX = (visibleRect.x / TAILLE_TUILE) * TAILLE_TUILE;
        int baseY = (visibleRect.y / TAILLE_TUILE) * TAILLE_TUILE;
        int maxX = visibleRect.x + visibleRect.width;
        int maxY = visibleRect.y + visibleRect.height;

        for (FournisseurTuile fournisseurTuile : fournisseurTuiles) {
            for (int x = baseX; x < maxX; x += TAILLE_TUILE) {
                for (int y = baseY; y < maxY; y += TAILLE_TUILE) {
                    int tuilePosX = x / TAILLE_TUILE;
                    int tuilePosY = y / TAILLE_TUILE;
                    Tuile tuile = fournisseurTuile.getTuile(zoom, tuilePosX, tuilePosY);
                    g0.drawImage(tuile.image(), x, y, null);
                }
            }
        }
    }
}
