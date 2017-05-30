package ch.smartcity.carte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static java.lang.Math.pow;

/**
 * Classe principale du module de la carte. Elle se charge de construire l'interface graphique
 * qui sera integré à la fenêtre principale du programme
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class Carte {

    private static final String URL_TUILE_OSM = "http://a.tile.openstreetmap.org/";
    private static final int ZOOM_INITIAL = 14;
    private static final PointWGS84 POSITION_INITIALE =
            new PointWGS84(46.545, 6.58);
    private final CarteTuilesComponent carteTuilesComponent;
    private Point positionSouris = new Point();
    private Point positionVue = new Point();

    /**
     * Constructeur de la carte qui initialise le Jcomponent de la carte et lui ajoute un
     * fournisseur de tuile OSM
     *
     * @throws IllegalArgumentException si la carte n'a pas pu se construire à cause du niveau de zoom
     */
    public Carte() throws IllegalArgumentException {
        carteTuilesComponent = new CarteTuilesComponent(ZOOM_INITIAL);
        FournisseurTuile fournisseurTuileOSM =
                new FournisseurTuileCache(new FournisseurTuileOSM(URL_TUILE_OSM));
        carteTuilesComponent.ajoutFournisseurTuile(fournisseurTuileOSM);
    }

    /**
     * Permet de mettre à jour les informations des événements sur la carte
     *
     * @param newEvenements listes des événements à afficher sur les tuiles
     */
    public void updateEvenement(ArrayList<Event> newEvenements) {
        FournisseurTuilePointCouleur newFournisseurTuilePointCouleur =
                new FournisseurTuilePointCouleur(newEvenements);
        carteTuilesComponent.ajoutFournisseurTuile(newFournisseurTuilePointCouleur);
    }

    /**
     * @return Le JComponent de la carte interactive
     */
    public JComponent createCenterPanel() {
        final JViewport viewPort = new JViewport();
        viewPort.setView(carteTuilesComponent);
        PointOSM positionInitialeOSM = POSITION_INITIALE.toOSM(carteTuilesComponent.zoom());
        viewPort.setViewPosition(new Point(
                positionInitialeOSM.arrondiX(),
                positionInitialeOSM.arrondiY()));

        final JPanel copyrightPanel = createCopyrightPanel();

        final JLayeredPane layeredPane = new JLayeredPane();

        layeredPane.add(viewPort, new Integer(0));
        layeredPane.add(copyrightPanel, new Integer(1));

        // permet d'adapter les dimension à la taille du panel prévu pour
        // la carte dans la fenêtre principale
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Rectangle newBounds = layeredPane.getBounds();
                viewPort.setBounds(newBounds);
                copyrightPanel.setBounds(newBounds);
                viewPort.revalidate();
                copyrightPanel.revalidate();
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
                    Point position = SwingUtilities
                            .convertPoint(e.getComponent(), lPPos, carteTuilesComponent);
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

    private JPanel createCopyrightPanel() {
        JLabel copyrightLabel = new JLabel(
                "© Les contributeurs d'OpenStreetMap",
                SwingConstants.CENTER);
        copyrightLabel.setOpaque(true);
        copyrightLabel.setForeground(new Color(1f, 1f, 1f, 0.6f));
        copyrightLabel.setBackground(new Color(0f, 0f, 0f, 0.4f));
        copyrightLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 5, 0));

        JPanel copyrightPanel = new JPanel(new BorderLayout());
        copyrightPanel.add(copyrightLabel, BorderLayout.PAGE_END);
        copyrightPanel.setOpaque(false);
        return copyrightPanel;
    }
}
