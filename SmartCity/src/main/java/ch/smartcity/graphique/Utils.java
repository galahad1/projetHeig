package ch.smartcity.graphique;

import ch.smartcity.database.controllers.access.EvenementAccess;
import ch.smartcity.database.models.Adresse;
import ch.smartcity.database.models.Evenement;
import ch.smartcity.graphique.controllers.ConfigurationManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Met a disposition des méthodes utilitaires pour les fenêtres de l'application
 * - Méthode pour voir une prévisualisation des événements
 * - Méthodes contrôlant la saisie des champs d'un formulaire
 * - Méthode rafraichissant la liste des événements en attente
 *
 * @author Tano Iannetta
 * @author Wojciech Myszkorowsk
 */
final class Utils {

    private static DateFormat dateFormat = new SimpleDateFormat(ConfigurationManager.getInstance()
            .getString("date.format"));

    /**
     * Recoit une liste d'événements afin d'en afficher une prévisualisation
     * comprenant moins de détails que l'evenement complet
     *
     * @param liste liste d'evenement
     * @return liste des previews des evenements
     */
    static List<String> previewEvenement(List<Evenement> liste) {
        ArrayList<String> preview = new ArrayList<>();

        if (liste == null || liste.isEmpty()) {
            return preview;
        }

        // parcours la liste des evenements
        for (Evenement e : liste) {

            // récupère les attributs à afficher
            String str = "";
            str += e.getIdEvenement() + " / ";
            str += e.getNomEvenement() + " / ";

            // adresse
            Adresse a = e.getAdresse();
            str += a.getRue().getNomRue() + " " + a.getNumeroDeRue() + " / ";

            // priorité
            str += e.getPriorite() + " / ";
            // dates
            Calendar c = e.getDebut();
            String date = dateFormat.format(c.getTime());
            str += date + " / ";
            c = e.getFin();

            if (c == null) {
                str += " null ";
            } else {
                date = dateFormat.format(c.getTime());
                str += date;
            }

            preview.add(str); // ajout a la liste
        }
        return preview;
    }

    /**
     * Vide la liste passé en parametre et renvoie une nouvelle liste avec les données actuel de base de données
     *
     * @param liste liste des evenement
     * @return une liste actuel des evenements
     */
    static List<String> refreshListAcess(List<String> liste) {
        liste.clear();
        return Utils.previewEvenement(EvenementAccess.getInstance().getEnAttente());
    }

    /**
     * Controle si le texte correspond à l'expression régulière
     *
     * @param texte texte à contrôler
     * @param regex expression de contrôle
     * @return vrai si le texte est valide, faux sinon
     */
    static boolean controlSaisie(String texte, String regex) {
        return !texte.isEmpty() && texte.matches(regex);
    }

    /**
     * Contrôle si le texte ne dépasse pas la taille donnée
     *
     * @param texte     texte à contôler
     * @param taillemax taille maximum du texte
     * @return vrai si le texte ne dépasse pas la taille donnée, faux sinon
     */
    static boolean controlSaisie(String texte, int taillemax) {
        return !texte.isEmpty() && texte.length() <= taillemax;
    }

    /**
     * Controle si le texte ne depasse pas la taille donnée et s'il correspond
     * à l'expression régulière données
     *
     * @param texte     texte à controler
     * @param tailleMax taille maximum du texte
     * @param regex     expression régulière de contrôle
     * @return vrai si le texte est conforme aux restrctions, faux sinon
     */
    static boolean controleSaisie(String texte, int tailleMax, String regex) {
        return texte.length() <= tailleMax && !texte.isEmpty() && texte.matches(regex);
    }
}
