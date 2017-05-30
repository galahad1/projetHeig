package ch.smartcity.graphique;

import ch.smartcity.database.controllers.access.EvenementAccess;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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

    private static final int MAX_ELEMENT = 7;

    private List<String> liste;
    private JList notif;
    private JLabel nbNotif;

    public ThreadMiseAjourNotifications(JList notif, JLabel nbNotif) {
        this.liste = new ArrayList<>();
        this.notif = notif;
        this.nbNotif = nbNotif;
    }

    @Override
    public void run() {
        liste.clear();
        liste.addAll(Utils.previewEvenement(EvenementAccess.getInstance().getEnAttente()));

        DefaultListModel model = new DefaultListModel();

        if (liste.size() > MAX_ELEMENT) {
            shuffle(liste);
        }

        for (String v : liste) {
            JTextField ligne = new JTextField(v);

            ligne.setBackground(Color.RED);

            if (model.getSize() <= MAX_ELEMENT) {
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
     * retourne la Jlist des notifcations
     *
     * @return une Jlist
     */
    public JList getJliste() {
        return notif;
    }


    /**
     * retourne le panel qui contient la liste des notifications
     *
     * @return le panel avec les notifications
     */
    public JLabel getPanelNotifications() {
        return nbNotif;
    }
}
