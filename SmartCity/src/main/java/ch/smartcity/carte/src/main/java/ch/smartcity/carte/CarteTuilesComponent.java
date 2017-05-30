package ch.smartcity.carte;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

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
    private static final int MAX_ZOOM = 19;
    private static final int MIN_ZOOM = 12;
    private int zoom;
    private LinkedList<FournisseurTuile> fournisseurTuiles;

    /**
     * Crée un composant Swing capable d'afficher une carte en tuiles
     *
     * @param zoom Le niveau de zoom de la carte
     * @throws IllegalArgumentException Léve l'exeption si le niveau de zoom est inférieur
     *                                  à MIN_ZOOM ou supérieur à MAX_ZOOM.
     */
    CarteTuilesComponent(int zoom) {
        if (zoom < MIN_ZOOM || zoom > MAX_ZOOM) {
            throw new IllegalArgumentException();
        }
        this.zoom = zoom;
        fournisseurTuiles = new LinkedList<>();
    }

    public int zoom() {
        return zoom;
    }

    /**
     * Modifie le niveau de zoom de la carte
     *
     * @param zoom Le nouveau niveau de zoom de la carte
     * @return vrai si le zoom est dans la limite possible définie par MAX_ZOOM et MIN_ZOOM
     */
    public boolean setZoom(int zoom) {
        if (zoom >= MIN_ZOOM && zoom <= MAX_ZOOM) {
            this.zoom = zoom;
            repaint();
            return true;
        }
        return false;
    }

    /**
     * Ajoute un nouveau fournisseur de tuiles à la liste des fournisseurs de la
     * carte. Fait en sorte de n'avoir qu'un fournisseurTuilePointCouleur
     *
     * @param fournisseurTuile Un nouveau fournisseur de tuiles de la carte
     */
    public void ajoutFournisseurTuile(FournisseurTuile fournisseurTuile) {
        // s'il y a déjà un fournisseurTuilePointCouleur on l'enleve
        if (fournisseurTuiles.size() == 2) {
            fournisseurTuiles.pollLast();
        }
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
