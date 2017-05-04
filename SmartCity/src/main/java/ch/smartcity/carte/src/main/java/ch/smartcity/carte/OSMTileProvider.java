package ch.smartcity.carte;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Représente un fournisseur de tuiles qui obtient ces derniéres depuis un
 * serveur utilisant les conventions de nommage des tuiles du projet
 * OpenStreetMap
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class OSMTileProvider implements TileProvider {
    final String baseServerAdress;

    /**
     * Construit un fournisseur de tuiles OSM
     *
     * @param baseServerAdress L'adresse de base du serveur
     */
    public OSMTileProvider(String baseServerAdress) {
        this.baseServerAdress = baseServerAdress;
    }

    @Override
    public Tile tileAt(int zoom, int x, int y) {
        BufferedImage image = null;
        try {
            URL url = new URL(baseServerAdress + "/" + zoom + "/" + x + "/" + y + ".png");
            image = ImageIO.read(url);
        } catch (IOException e) {
            try {
                image = ImageIO.read(getClass().getResource("resources/image/error-tile.png"));
            } catch (IOException imageReadingError) {
                System.out.print("Erreur lors de la lecture de la ressource image: ");
                imageReadingError.printStackTrace();
                System.exit(1);
            }
        }
        return new Tile(zoom, x, y, image);
    }

}
