package ch.smartcity.carte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.pow;

public final class Carte {
    private static final String URL_TUILE_OSM = "http://a.tile.openstreetmap.org/";
    private static final int ZOOM_INITIAL = 14;
    //  nord sud / ouest est
    private static final PointWGS84 POSITION_INITIALE = new PointWGS84(46.545, 6.58);
    private final CarteTuilesComponent carteTuilesComponent;
    private Point mousePos = new Point();
    private Point viewPos = new Point();

    public Carte() throws IOException {
        carteTuilesComponent = new CarteTuilesComponent(ZOOM_INITIAL);
        FournisseurTuile fournisseurTuileOSM = new FournisseurTuileCache(new FournisseurTuileOSM(URL_TUILE_OSM));
        carteTuilesComponent.ajoutFournisseurTuile(fournisseurTuileOSM);

    }

    public void updateEvenement(ArrayList<Event> newEvenements) {
        FournisseurTuilePointCouleur newFournisseurTuilePointCouleur = new FournisseurTuilePointCouleur(newEvenements);
        carteTuilesComponent.ajoutFournisseurTuile(newFournisseurTuilePointCouleur);
    }


    public JComponent createCenterPanel() {
        final JViewport viewPort = new JViewport();
        viewPort.setView(carteTuilesComponent);
        PointOSM startingPosOSM = POSITION_INITIALE.toOSM(carteTuilesComponent.zoom());
        viewPort.setViewPosition(new Point(startingPosOSM.arrondiX(), startingPosOSM.arrondiY()));

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
