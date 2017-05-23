package ch.smartcity.carte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.pow;

/**
 * ! TODO
 */
public final class Carte {
    private static final String URL_TUILE_OSM = "http://a.tile.openstreetmap.org/";
    private static final int ZOOM_INITIAL = 14;
    //  nord sud / ouest est
    private static final PointWGS84 POSITION_INITIALE = new PointWGS84(46.545, 6.58);
    private static final int LARGEUR_COMPOSANT = 1190;
    private static final int HAUTEUR_COMPOSANT = 765;
    private final CarteTuilesComponent carteTuilesComponent;
    private Point positionSouris = new Point();
    private Point positionVue = new Point();

    /**
     * Constructeur de la carte qui initialise le Jcomponent de la carte et lui ajoute un fournisseur de tuile OSM
     *
     * @throws IOException
     */
    public Carte() throws IOException {
        carteTuilesComponent = new CarteTuilesComponent(ZOOM_INITIAL);
        FournisseurTuile fournisseurTuileOSM = new FournisseurTuileCache(new FournisseurTuileOSM(URL_TUILE_OSM));
        carteTuilesComponent.ajoutFournisseurTuile(fournisseurTuileOSM);
    }

    /**
     * Permet de mettre à jour les informations des événements sur la carte
     * @param newEvenements listes des événements à afficher sur les tuiles
     */
    public void updateEvenement(ArrayList<Event> newEvenements) {
        FournisseurTuilePointCouleur newFournisseurTuilePointCouleur = new FournisseurTuilePointCouleur(newEvenements);
        carteTuilesComponent.ajoutFournisseurTuile(newFournisseurTuilePointCouleur);
    }


    /**
     *
     * @return Le JComponent de la carte interactive
     */
    public JComponent createCenterPanel() {
        final JViewport viewPort = new JViewport();
        viewPort.setView(carteTuilesComponent);
        PointOSM positionInitialeOSM = POSITION_INITIALE.toOSM(carteTuilesComponent.zoom());
        viewPort.setViewPosition(new Point(positionInitialeOSM.arrondiX(), positionInitialeOSM.arrondiY()));

        final JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(LARGEUR_COMPOSANT, HAUTEUR_COMPOSANT));

        layeredPane.add(viewPort, new Integer(0));

        // permet d'adapter la dimension du viewPort
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Rectangle newBounds = layeredPane.getBounds();
                viewPort.setBounds(newBounds);
                viewPort.revalidate();
            }
        });

        // récupère les positions de la vue et de la souris lors d'un click
        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                positionSouris = e.getLocationOnScreen();
                positionVue = viewPort.getViewPosition();
            }
        });

        // permet de faire bouger la carte lors du glissement de la souris
        layeredPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int posX = positionVue.x - (e.getXOnScreen() - positionSouris.x);
                int posY = positionVue.y - (e.getYOnScreen() - positionSouris.y);
                viewPort.setViewPosition(new Point(posX, posY));
            }
        });

        // interaction de la roulette de la souris pour le zoom
        layeredPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotationRoulette = e.getWheelRotation();
                int newZoom = carteTuilesComponent.zoom() - rotationRoulette;
                if (carteTuilesComponent.setZoom(newZoom)) {
                    Point lPPos = e.getPoint();
                    Point position = SwingUtilities.convertPoint(e.getComponent(), lPPos, carteTuilesComponent);
                    int x = (int) (position.x / pow(2, rotationRoulette)) - lPPos.x;
                    int y = (int) (position.y / pow(2, rotationRoulette)) - lPPos.y;
                    viewPort.setViewPosition(new Point(x, y));
                }
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(layeredPane);
        return centerPanel;
    }
}
