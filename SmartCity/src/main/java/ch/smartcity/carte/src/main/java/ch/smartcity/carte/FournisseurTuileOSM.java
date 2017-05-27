package ch.smartcity.carte;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Représente un fournisseur de tuiles qui obtient ces dernières depuis un
 * serveur utilisant les conventions de nommage des tuiles du projet
 * OpenStreetMap
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class FournisseurTuileOSM implements FournisseurTuile {

    /**
     * Utilisé pour journaliser les actions effectuées
     */
    private static final Logger LOGGER = Logger.getLogger(FournisseurTuileOSM.class.getName());

    private static final String ERROR_TILE_PNG_FILE =
            "ch/smartcity/carte/resources/image/tuile_erreur.png";
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
            } catch (IOException e1) {
                LOGGER.severe("Erreur lors de la lecture de la ressource image");
                LOGGER.log(Level.SEVERE, e1.getMessage(), e1);
                System.exit(1);
            }
        }
        return new Tuile(zoom, x, y, image);
    }

}
