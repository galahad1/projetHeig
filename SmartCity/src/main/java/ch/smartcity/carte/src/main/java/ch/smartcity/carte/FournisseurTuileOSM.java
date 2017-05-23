package ch.smartcity.carte;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Représente un fournisseur de tuiles qui obtient ces dernières depuis un
 * serveur utilisant les conventions de nommage des tuiles du projet
 * OpenStreetMap
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class FournisseurTuileOSM implements FournisseurTuile {
    private static final String ERROR_TILE_PNG_FILE =
            "ch/smartcity/carte/resources/image/error-tile.png";
    final String serveurTuileURL;

    /**
     * Construit un fournisseur de tuiles OSM
     *
     * @param serveurURL L'URL du serveur de tuile
     */
    public FournisseurTuileOSM(String serveurURL) {
        this.serveurTuileURL = serveurURL;
    }

    @Override
    public Tuile getTuile(int zoom, int x, int y) {
        BufferedImage image = null;
        try {
            URL url = new URL(serveurTuileURL + "/" + zoom + "/" + x + "/" + y + ".png");
            image = ImageIO.read(url);
        } catch (IOException e) {
            try {
                image = ImageIO.read(getClass().getClassLoader()
                        .getResource(ERROR_TILE_PNG_FILE));
            } catch (IOException imageReadingError) {
                System.out.print("Erreur lor de la lecture de la ressource image: ");
                imageReadingError.printStackTrace();
                System.exit(1);
            }
        }
        return new Tuile(zoom, x, y, image);
    }

}
