package ch.smartcity;

import ch.smartcity.graphique.FenetrePrincipale;

/**
 * Lance l'application et ouvre la fenêtre principale
 */
public class SmartCity {

    public static void main(String[] args) throws Exception {
        new FenetrePrincipale().fenetre.setVisible(true);
    }
}
