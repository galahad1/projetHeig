package ch.smartcity;

import ch.smartcity.graphique.FenetrePrincipale;
import ch.smartcity.graphique.controllers.ConfigurationManager;

/**
 * Lance l'application et ouvre la fenêtre principale
 */
public class SmartCity {

    public static void main(String[] args) throws Exception {

        // Supprime la journalisation de la librairie C3P0 dans la console
        ConfigurationManager.getInstance().c3p0Logging();
        new FenetrePrincipale().fenetre.setVisible(true);
    }
}
