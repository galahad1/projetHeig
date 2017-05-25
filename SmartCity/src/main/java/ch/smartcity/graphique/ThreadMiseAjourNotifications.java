package ch.smartcity.graphique;

import ch.smartcity.database.controllers.access.EvenementAccess;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.TimerTask;

import static java.util.Collections.shuffle;

/**
 * Modélise un timer qui permet de mettre à jour la liste de notification dans un panel.
 * Il s'agit de rafraichir la liste toute les x temps.
 *
 * @author Wojciech Myskorowski
 */

public class ThreadMiseAjourNotifications extends TimerTask {
    private final int maxElement = 9;
    private List<String> liste;
    private JList notif;
    private JLabel nbNotif;

    public ThreadMiseAjourNotifications(List<String> listnoftif, JList notif, JLabel nbNotif) {
        liste = listnoftif;
        this.notif = notif;
        this.nbNotif = nbNotif;
    }

    @Override
    public void run() {
        liste.clear();

        for (String s : Utils.refreshListAcess(Utils.previewEvenement(EvenementAccess.getEnAttente()))) {
            liste.add(s);
        }
        DefaultListModel model = new DefaultListModel();

        if (liste.size() > maxElement) {
            shuffle(liste);
        }
        for (String v : liste) {
            JTextField ligne = new JTextField(v);

            ligne.setBackground(Color.RED);

            if (model.getSize() <= maxElement) {
                model.addElement(ligne.getText());
            }
        }
        int nombreNotification = liste.size();
        nbNotif.setText(nombreNotification + " Notifications");
        nbNotif.repaint();
        notif.setForeground(Color.RED);
        notif.setModel(model);
        notif.repaint();
    }

    /**
     * retourne la liste des notification
     *
     * @return liste des notifcations
     */
    public List<String> getListe() {
        return liste;
    }

    /**
     * retourne la Jlist des notifcations
     *
     * @return une Jlist
     */
    public JList getJliste() {
        return notif;
    }

    public JLabel getNombrePanel() {
        return nbNotif;
    }
}
