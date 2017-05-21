package ch.smartcity.graphique;

import ch.smartcity.database.controllers.access.EvenementAccess;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.TimerTask;

/**
 * Modélise un timer qui permet de mettre à jour une liste dans un panel
 *
 * @author Wojciech Myskorowski
 *
 */

public class MiseAjour extends TimerTask {
    private List<String> liste;
    private JList notif;

    public MiseAjour(List<String> listnoftif, JList notif) {
        liste = listnoftif;
        this.notif = notif;
    }

    @Override
    public void run() {
        liste.clear();

        for (String s : Utils.refreshListAcess(Utils.previewEvenement(EvenementAccess.getEnAttente()))) {
            liste.add(s);
        }
        DefaultListModel model = new DefaultListModel();

        for(String v : liste)
        {
            JLabel ligne = new JLabel(v);
            //model.addElement(v);
            String s = ligne.getText().substring(0, 1);
            if (s.equals("1")) {
                ligne.setForeground(Color.GREEN);
            } else if (s.equals("2")) {
                ligne.setForeground(Color.DARK_GRAY);
            } else if (s.equals("3")) {
                ligne.setForeground(Color.BLUE);
            } else {
                ligne.setForeground(Color.RED);
            }

            model.addElement(ligne.getText());
        }
        notif.setModel(model);
        notif.repaint();
    }

    /**
     * retourne la liste des notification
     * @return liste des notifcations
     */
    public List<String> getListe() {
        return  liste;
    }

    /**
     * retourne la Jlist des notifcations
     * @return une Jlist
     */
    public  JList getJliste() {return notif;}
}
