package ch.smartcity.graphique;

import ch.smartcity.database.controllers.access.EvenementAccess;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.TimerTask;

import static java.util.Collections.shuffle;

/**
 * Modélise un timer qui permet de mettre à jour une liste dans un panel
 *
 * @author Wojciech Myskorowski
 */

public class MiseAjour extends TimerTask {
    private final int maxElement = 10;
    private List<String> liste;
    private JList notif;
    private JLabel nbNotif;

    public MiseAjour(List<String> listnoftif, JList notif, JLabel nbNotif) {
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
            String s = ligne.getText().substring(0, 1);
            if (s.equals("1")) {
                ligne.setForeground(Color.GREEN);
            } else if (s.equals("2")) {
                ligne.setForeground(Color.DARK_GRAY);
            } else if (s.equals("3")) {
                ligne.setForeground(Color.BLUE);
            } else {
                ligne.setForeground(Color.RED);
                ligne.setBackground(Color.RED);
            }

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
