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
    private final ColoredPointTileProvider coloredPointTileProvider;
    private final TiledMapComponent tiledMapComponent;
    private Point mousePos = new Point();
    private Point viewPos = new Point();

    public Carte() throws IOException {
        tiledMapComponent = new TiledMapComponent(INITIAL_ZOOM);

        TileProvider bgTileProvider = new CachedTileProvider(new OSMTileProvider(OSM_TILE_URL));
        tiledMapComponent.addTileProviders(bgTileProvider);

        // construire ici l'ArrayList des Events avec une EventsBuilder
        ArrayList<Event> events = new ArrayList<>();
        PointWGS84 p1 = new PointWGS84(Math.toRadians(6.6303347592), Math.toRadians(46.5206173997));
        PointWGS84 p2 = new PointWGS84(Math.toRadians(6.62774113075), Math.toRadians(46.5100746361));
        PointWGS84 p3 = new PointWGS84(Math.toRadians(6.63031), Math.toRadians(46.5068));
        PointWGS84 p4 = new PointWGS84(Math.toRadians(6.63374113075), Math.toRadians(46.52400746361));
        PointWGS84 p5 = new PointWGS84(Math.toRadians(6.628), Math.toRadians(46.511));
        PointWGS84 p6 = new PointWGS84(Math.toRadians(6.625), Math.toRadians(46.515));
        Event e1 = new Event("Accidents", p1, 1);
        Event e2 = new Event("Travaux", p2, 2);
        Event e3 = new Event("Manifestations", p3, 3);
        Event e4 = new Event("Rénovations", p4, 4);
        Event e5 = new Event("Constructions", p5, 5);
        Event e6 = new Event("Doléances", p6, 6);
        events.add(e1);
        events.add(e2);
        events.add(e3);
        events.add(e4);
        events.add(e5);
        events.add(e6);

        coloredPointTileProvider = new ColoredPointTileProvider(events);

        tiledMapComponent.addTileProviders(coloredPointTileProvider);

    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Carte().start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private JComponent createCenterPanel() {
        final JViewport viewPort = new JViewport();
        viewPort.setView(tiledMapComponent);
        PointOSM startingPosOSM = INITIAL_POSITION.toOSM(tiledMapComponent.zoom());
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
                int newZoom = tiledMapComponent.zoom() - wheelRot;
                if (tiledMapComponent.setZoom(newZoom)) {
                    Point lPPos = e.getPoint();
                    Point mousePos = SwingUtilities.convertPoint(e.getComponent(), lPPos, tiledMapComponent);
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

    private void start() {
        JFrame frame = new JFrame("Carte");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(createCenterPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
