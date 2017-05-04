package ch.smartcity.carte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.pow;

public final class Carte {
    private static final String OSM_TILE_URL = "http://a.tile.openstreetmap.org/";
    private static final int INITIAL_ZOOM = 14;
    // ouest est / nord sud
    private static final PointWGS84 INITIAL_POSITION = new PointWGS84(Math.toRadians(6.58), Math.toRadians(46.545));
    private final FournisseurTuilePointCouleur fournisseurTuilePointCouleur;
    private final CarteTuilesComponent carteTuilesComponent;
    private Point mousePos = new Point();
    private Point viewPos = new Point();

    public Carte() throws IOException {
        carteTuilesComponent = new CarteTuilesComponent(INITIAL_ZOOM);

        FournisseurTuile bgTileProvider = new FournisseurTuileCache(new FournisseurTuileOSM(OSM_TILE_URL));
        carteTuilesComponent.addTileProviders(bgTileProvider);

        // construire ici l'ArrayList des Events avec une EventsBuilder
        ArrayList<Evenement> evenements = new ArrayList<>();
        PointWGS84 p1 = new PointWGS84(Math.toRadians(6.6303347592), Math.toRadians(46.5206173997));
        PointWGS84 p2 = new PointWGS84(Math.toRadians(6.62774113075), Math.toRadians(46.5100746361));
        PointWGS84 p3 = new PointWGS84(Math.toRadians(6.63031), Math.toRadians(46.5068));
        PointWGS84 p4 = new PointWGS84(Math.toRadians(6.63374113075), Math.toRadians(46.52400746361));
        PointWGS84 p5 = new PointWGS84(Math.toRadians(6.628), Math.toRadians(46.511));
        PointWGS84 p6 = new PointWGS84(Math.toRadians(6.625), Math.toRadians(46.515));
        Evenement e1 = new Evenement("Accidents", p1, 1);
        Evenement e2 = new Evenement("Travaux", p2, 2);
        Evenement e3 = new Evenement("Manifestations", p3, 3);
        Evenement e4 = new Evenement("Rénovations", p4, 4);
        Evenement e5 = new Evenement("Constructions", p5, 5);
        Evenement e6 = new Evenement("Doléances", p6, 6);
        evenements.add(e1);
        evenements.add(e2);
        evenements.add(e3);
        evenements.add(e4);
        evenements.add(e5);
        evenements.add(e6);

        fournisseurTuilePointCouleur = new FournisseurTuilePointCouleur(evenements);

        carteTuilesComponent.addTileProviders(fournisseurTuilePointCouleur);

    }

    public JComponent createCenterPanel() {
        final JViewport viewPort = new JViewport();
        viewPort.setView(carteTuilesComponent);
        PointOSM startingPosOSM = INITIAL_POSITION.toOSM(carteTuilesComponent.zoom());
        viewPort.setViewPosition(new Point(startingPosOSM.roundedX(), startingPosOSM.roundedY()));

        final JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1190, 765));

        layeredPane.add(viewPort, new Integer(0));

        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                final Rectangle newBounds = layeredPane.getBounds();
                viewPort.setBounds(newBounds);
                viewPort.revalidate();

            }
        });

        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePos = e.getLocationOnScreen();
                viewPos = viewPort.getViewPosition();
            }
        });

        layeredPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int posX = viewPos.x - (e.getXOnScreen() - mousePos.x);
                int posY = viewPos.y - (e.getYOnScreen() - mousePos.y);
                viewPort.setViewPosition(new Point(posX, posY));
            }
        });

        layeredPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int wheelRot = e.getWheelRotation();
                int newZoom = carteTuilesComponent.zoom() - wheelRot;
                if (carteTuilesComponent.setZoom(newZoom)) {
                    Point lPPos = e.getPoint();
                    Point mousePos = SwingUtilities.convertPoint(e.getComponent(), lPPos, carteTuilesComponent);
                    int x = (int) (mousePos.x / pow(2, wheelRot)) - lPPos.x;
                    int y = (int) (mousePos.y / pow(2, wheelRot)) - lPPos.y;
                    viewPort.setViewPosition(new Point(x, y));
                }
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(layeredPane);
        return centerPanel;
    }
}
