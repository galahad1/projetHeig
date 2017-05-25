package ch.smartcity.graphique;

import ch.smartcity.graphique.controllers.ConfigurationManager;
import ch.smartcity.database.controllers.access.EvenementAccess;
import ch.smartcity.database.models.Adresse;
import ch.smartcity.database.models.Evenement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class Utils {

    private static DateFormat dateFormat = new SimpleDateFormat(ConfigurationManager.getInstance().getString("date.format"));


    private Utils() {
    }


    /**
     * Recoit une liste d'événement afin d'en afficher une prévisualisation
     * comprenant moins de détails que l'evenement complet
     *
     * @param liste liste d'evenement
     * @return liste des previews des evenements
     */
    public static List<String> previewEvenement(List<Evenement> liste) {
        ArrayList<String> preview = new ArrayList<>();
        if (liste == null || liste.isEmpty()) {
            return preview;
        }

        // parcours la liste des evenements
        for (Evenement e : liste) {
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

    public static List<String> refreshListAcess(List<String> liste) {
        for (int i = 0; i < liste.size(); i++) {
            liste.remove(i);
        }
        return Utils.previewEvenement(EvenementAccess.getEnAttente());
    }

    /**
     * @param texte
     * @param regex
     * @return
     * @brief
     */
    public static boolean controlSaisie(String texte, String regex) {

        if (texte.isEmpty() || !texte.matches(regex)) {
            return false;
        }

        return true;
    }

    /**
     * @param texte
     * @param taillemax
     * @return
     * @brief
     */
    public static boolean controlSaisie(String texte, int taillemax) {

        return !(texte.isEmpty() || texte.length() > taillemax);
    }

    /**
     * @param texte
     * @param tailleMax
     * @param regex
     * @return
     * @brief
     */
    public static boolean controleSaisie(String texte, int tailleMax, String regex) {

        // controle taille
        if (texte.length() > tailleMax || texte.isEmpty()) {
            return false;
        }
        // controle lettres et chiffres
        return texte.matches(regex);
    }
}
