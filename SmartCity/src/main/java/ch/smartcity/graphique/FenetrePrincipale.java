package ch.smartcity.graphique;

import ch.smartcity.carte.Carte;
import ch.smartcity.carte.Event;
import ch.smartcity.carte.PointWGS84;
import ch.smartcity.database.controllers.access.EvenementAccess;
import ch.smartcity.database.models.Evenement;
import ch.smartcity.database.models.Statut_;
import com.toedter.calendar.JCalendar;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FenetrePrincipale {

    private static final int CONTEXTE_AJOUTER = 0;
    private static final int CONTEXTE_EN_ATTENTE = 1;
    private final JPanel panelCalendrier = new JPanel();
    private final JTextField textRubriques = new JTextField();
    private final JCheckBox chckbxAccidents = new JCheckBox("Accidents");
    private final JCheckBox chckbxTravaux = new JCheckBox("Travaux");
    private final JLabel labelManifestations = new JLabel("MANIFESTATIONS");
    private final JLabel labelChantiers = new JLabel("CHANTIERS");
    private final JCheckBox chckbxRenovation = new JCheckBox("Rénovations");
    private final JCheckBox chckbxConstruction = new JCheckBox("Constructions");
    private final JPanel panelMenu = new JPanel();
    private final JLabel lblNbrNotification = new JLabel("Notifications");
    private final JPanel panelLogo = new JPanel();
    private final JTextArea txtrDescription = new JTextArea();
    public JFrame fenetre;
    JPanel panelPrincipal = new JPanel();
    JPanel panelCarte = new JPanel();
    JPanel panelNotifications = new JPanel();
    JButton btnAjouter = new JButton("Ajouter/Modifier");
    JButton btnPdf = new JButton("PDF");
    JButton btnEnAttente = new JButton("En attente");
    // création dynamique des checkboxes des doléances
    // TODO
    JPanel panelRubriques = new JPanel();
    JLabel labelTrafic = new JLabel("TRAFIC");
    // récupération d'un nombre de requetes a traiter
    // TODO
    GroupLayout gl_panelMenu = new GroupLayout(panelMenu);
    JScrollPane scrollPaneDescription = new JScrollPane();
    JCalendar calendrier = new JCalendar();
    private Carte carte = null;
    //liste totale des différents événements
    private List<Event> allEvents = new ArrayList<>(); // tous les événements a afficher sur la carte
    private List<Event> listeAccidents = new ArrayList<>();
    private List<Event> listeTravaux = new ArrayList<>();
    private List<Event> listeManifestations = new ArrayList<>();
    private List<Event> listeRenovations = new ArrayList<>();
    private List<Event> listeConstructions = new ArrayList<>();

    private Calendar dateSelectionne;

    private JTextField textDescription = new JTextField();


    /**
     * Create the application.
     */
    public FenetrePrincipale() {
        initialize();

        listeAccidents = wrapperEvement(EvenementAccess.getByRubriqueEnfant("accidents"));
        listeTravaux = wrapperEvement(EvenementAccess.getByRubriqueEnfant("travaux"));
        listeManifestations = wrapperEvement(EvenementAccess.getByRubriqueEnfant("manifestations"));
        listeRenovations = wrapperEvement(EvenementAccess.getByRubriqueEnfant("renovations"));
        listeConstructions = wrapperEvement(EvenementAccess.getByRubriqueEnfant("constructions"));
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        gl_panelMenu.setHorizontalGroup(
                gl_panelMenu.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_panelMenu.createSequentialGroup()
                                .addComponent(btnAjouter)
                                .addGap(18)
                                .addComponent(btnPdf)
                                .addPreferredGap(ComponentPlacement.RELATED, 347, Short.MAX_VALUE)
                                .addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
                                        .addComponent(lblNbrNotification)
                                        .addComponent(btnEnAttente))
                                .addContainerGap())
        );
        gl_panelMenu.setVerticalGroup(
                gl_panelMenu.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_panelMenu.createSequentialGroup()
                                .addContainerGap(13, Short.MAX_VALUE)
                                .addComponent(lblNbrNotification, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_panelMenu.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(btnAjouter)
                                        .addComponent(btnEnAttente)
                                        .addComponent(btnPdf))
                                .addContainerGap())
        );
        fenetre = new JFrame();
        fenetre.setTitle("SmartCity");
        fenetre.setResizable(false);
        fenetre.setBounds(0, 0, 1900, 1000);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.getContentPane().setLayout(null);

        btnAjouter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FenetreModification fenetre2 = new FenetreModification(CONTEXTE_AJOUTER);
                fenetre2.fenetre.setVisible(true);
            }
        });
        btnEnAttente.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FenetreModification fenetre2 = new FenetreModification(CONTEXTE_EN_ATTENTE);
                fenetre2.fenetre.setVisible(true);

            }
        });
        panelPrincipal.setBounds(0, 0, 1900, 1000);
        fenetre.getContentPane().add(panelPrincipal);
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setLayout(null);
        panelCarte.setBounds(700, 200, 1190, 765);
        //panelCarte.setBackground(Color.GRAY);

        panelCarte.setLayout(new BorderLayout());

        try {
            carte = new Carte();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        panelCarte.add(carte.createCenterPanel());

        panelPrincipal.add(panelCarte);
        panelNotifications.setBackground(Color.LIGHT_GRAY);
        panelNotifications.setBounds(700, 5, 782, 195);
        panelPrincipal.add(panelNotifications);
        panelNotifications.setLayout(null);
        panelCalendrier.setBackground(Color.DARK_GRAY);
        panelCalendrier.setBounds(1482, 5, 408, 195);

        panelPrincipal.add(panelCalendrier);
        panelMenu.setBounds(5, 119, 695, 81);

        panelPrincipal.add(panelMenu);
        panelMenu.setLayout(gl_panelMenu);

        scrollPaneDescription.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneDescription.setViewportBorder(UIManager.getBorder("Button.border"));
        scrollPaneDescription.setBounds(222, 260, 478, 705);
        panelPrincipal.add(scrollPaneDescription);
        txtrDescription.setEditable(false);
        txtrDescription.setText("Description");

        scrollPaneDescription.setViewportView(txtrDescription);

        textDescription.setText("Description");
        textDescription.setHorizontalAlignment(SwingConstants.CENTER);
        textDescription.setFont(new Font("Dialog", Font.BOLD, 18));
        textDescription.setEditable(false);
        textDescription.setColumns(6);
        textDescription.setBackground(Color.LIGHT_GRAY);
        textDescription.setBounds(222, 200, 478, 62);
        panelPrincipal.add(textDescription);
        panelCalendrier.setLayout(new CardLayout(0, 0));

        panelCalendrier.add(calendrier, "name_9865352109015");

        calendrier.getDayChooser().setAlwaysFireDayProperty(true);
        calendrier.getDayChooser().addPropertyChangeListener("day", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                Date valDate = calendrier.getDate();
                dateSelectionne = Calendar.getInstance();
                dateSelectionne.setTime(valDate);

            }

        });

        JScrollPane scrollPaneRubriques = new JScrollPane();
        scrollPaneRubriques.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneRubriques.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneRubriques.setBounds(5, 260, 218, 705);
        panelPrincipal.add(scrollPaneRubriques);
        scrollPaneRubriques.setViewportView(panelRubriques);
        panelRubriques.setLayout(null);


        labelTrafic.setFont(new Font("Dialog", Font.BOLD, 16));
        labelTrafic.setBounds(12, 37, 107, 29);
        panelRubriques.add(labelTrafic);
        chckbxAccidents.setBounds(20, 74, 190, 23);

        panelRubriques.add(chckbxAccidents);
        chckbxTravaux.setBounds(20, 102, 129, 23);

        panelRubriques.add(chckbxTravaux);
        labelManifestations.setFont(new Font("Dialog", Font.BOLD, 16));
        labelManifestations.setBounds(12, 152, 164, 29);

        panelRubriques.add(labelManifestations);
        labelChantiers.setFont(new Font("Dialog", Font.BOLD, 16));
        labelChantiers.setBounds(12, 230, 107, 29);

        panelRubriques.add(labelChantiers);
        chckbxRenovation.setBounds(20, 267, 129, 23);

        panelRubriques.add(chckbxRenovation);
        chckbxConstruction.setBounds(20, 294, 129, 23);

        panelRubriques.add(chckbxConstruction);

        JCheckBox chckbxManifestations = new JCheckBox("Manifestations");
        chckbxManifestations.setBounds(20, 189, 156, 23);
        panelRubriques.add(chckbxManifestations);

        JLabel lblDoleances = new JLabel("DOLEANCES");
        lblDoleances.setFont(new Font("Dialog", Font.BOLD, 16));
        lblDoleances.setBounds(20, 338, 107, 29);
        panelRubriques.add(lblDoleances);
        textRubriques.setBounds(5, 200, 218, 62);
        panelPrincipal.add(textRubriques);
        textRubriques.setHorizontalAlignment(SwingConstants.CENTER);
        textRubriques.setText("Rubriques");
        textRubriques.setFont(new Font("Dialog", Font.BOLD, 18));
        textRubriques.setEditable(false);
        textRubriques.setColumns(6);
        textRubriques.setBackground(Color.LIGHT_GRAY);
        panelLogo.setBackground(Color.GRAY);
        panelLogo.setBounds(5, 5, 695, 114);

        panelPrincipal.add(panelLogo);


        chckbxAccidents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AbstractButton abstractButton = (AbstractButton) e.getSource();
                boolean selected = abstractButton.getModel().isSelected();

                if (selected) {

                    listeAccidents = wrapperEvement(EvenementAccess.getActif("accidents", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeAccidents);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                } else {
                    allEvents.removeAll(listeAccidents);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }
            }
        });

        chckbxTravaux.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AbstractButton abstractButton = (AbstractButton) e.getSource();
                boolean selected = abstractButton.getModel().isSelected();

                if (selected) {
                    allEvents.addAll(listeTravaux = wrapperEvement(EvenementAccess.getActif("travaux", dateSelectionne, Statut_.TRAITE)));
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                } else {
                    allEvents.removeAll(listeTravaux);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }
            }
        });

        chckbxManifestations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AbstractButton abstractButton = (AbstractButton) e.getSource();
                boolean selected = abstractButton.getModel().isSelected();

                if (selected) {
                    listeManifestations = wrapperEvement(EvenementAccess.getActif("manifestations", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeManifestations);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                } else {
                    allEvents.removeAll(listeManifestations);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }
            }
        });

        chckbxRenovation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AbstractButton abstractButton = (AbstractButton) e.getSource();
                boolean selected = abstractButton.getModel().isSelected();

                if (selected) {
                    listeRenovations = wrapperEvement(EvenementAccess.getActif("rénovations", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeRenovations);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                } else {
                    allEvents.removeAll(listeRenovations);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }
            }
        });

        chckbxConstruction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AbstractButton abstractButton = (AbstractButton) e.getSource();
                boolean selected = abstractButton.getModel().isSelected();

                if (selected) {
                    listeConstructions = wrapperEvement(
                            EvenementAccess.getActif("constructions", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeConstructions);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                } else {
                    allEvents.removeAll(listeConstructions);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }
            }
        });

    }

    private ArrayList<Event> wrapperEvement(List<Evenement> listeEvenement) {
        ArrayList<Event> evenements = new ArrayList<>();

        for (Evenement e : listeEvenement) {
            PointWGS84 point = new PointWGS84(e.getLatitude(), e.getLongitude());
            evenements.add(new Event(e.getNomEvenement(), point, e.getRubriqueEnfant().getIdRubriqueEnfant()));
        }

        return evenements;
    }
}