package ch.smartcity.graphique;

import ch.smartcity.database.models.Adresse;
import ch.smartcity.database.models.Evenement;
import com.sun.xml.internal.bind.v2.TODO;

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
    private static DateFormat  dateFormat = new SimpleDateFormat("dd/MM/yyyy");


    private Utils() {}


    /**
     * @param liste liste d'evenement
     * @return liste des previews des evenements
     * @brief Recoit une liste d'événement afin d'en afficher une prévisualisation
     * comprenant moins de détails que l'evenement complet
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

            // dates
            Calendar c = e.getDebut();
            //System.out.println(c);
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
}
