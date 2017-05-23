package ch.smartcity.graphique;

import ch.smartcity.database.controllers.access.EvenementAccess;
import ch.smartcity.database.models.Adresse;
import ch.smartcity.database.models.Evenement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wojtek on 17.05.17.
 */
public final class Utils {

    //TODO mettre dans config ressources
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


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
        List<String> list = Utils.previewEvenement(EvenementAccess.getEnAttente());
        return list;


    }
}
