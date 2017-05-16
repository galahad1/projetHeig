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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FenetrePrincipale {

    private final JPanel panelCalendrier = new JPanel();
    private final JTextField textRubriques = new JTextField();

    private final JCheckBox chckbxAccidents = new JCheckBox("Accidents");
    private final JCheckBox chckbxTravaux = new JCheckBox("Travaux");
    private final JCheckBox chckbxManifestations = new JCheckBox("Manifestations");
    private final JCheckBox chckbxRenovation = new JCheckBox("Rénovations");
    private final JCheckBox chckbxConstruction = new JCheckBox("Constructions");
    private final JCheckBox chckboxDoleances = new JCheckBox("Doléances");
    private final JLabel labelTrafic = new JLabel("TRAFIC");
    private final JLabel labelManifestations = new JLabel("MANIFESTATIONS");
    private final JLabel labelChantiers = new JLabel("CHANTIERS");
    private final JLabel labelDoleances = new JLabel("DOLEANCES");
    private final JPanel panelMenu = new JPanel();
    private final JLabel lblNbrNotification = new JLabel("Notifications");
    private final JPanel panelLogo = new JPanel();
    private final JTextArea txtrDescription = new JTextArea();
    public JFrame fenetre;
    JPanel panelRubriques = new JPanel();
    JPanel panelPrincipal = new JPanel();
    JPanel panelCarte = new JPanel();
    JPanel panelNotifications = new JPanel();
    JButton btnAjouter = new JButton("Ajouter/Modifier");
    JButton btnPdf = new JButton("PDF");
    JButton btnEnAttente = new JButton("En attente");
    // création dynamique des checkboxes des doléances
    // TODO

    // récupération d'un nombre de requetes a traiter
    // TODO
    GroupLayout gl_panelMenu = new GroupLayout(panelMenu);
    JScrollPane scrollPaneDescription = new JScrollPane();
    JCalendar calendrier = new JCalendar();
    private JList listEvenementsEnAttente;
    private Carte carte = null;
    //liste totale des différents événements
    private List<Event> allEvents = new ArrayList<>();
    private List<Event> listeAccidents = new ArrayList<>();
    private List<Event> listeTravaux = new ArrayList<>();
    private List<Event> listeManifestations = new ArrayList<>();
    private List<Event> listeRenovations = new ArrayList<>();
    private List<Event> listeConstructions = new ArrayList<>();
    private List<Event> listeDoleances = new ArrayList<>();

    private Calendar dateSelectionne;

    private JTextField textDescription = new JTextField();

    /**
     * Create the application.
     */
    public FenetrePrincipale() {
        initialize();
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

        //pop up de confirmation avant de quitter la fenetre
        fenetre.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        fenetre.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Etes vous sûrs de vouloir quitter ?", "Confirmer",
                        JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                fenetre.dispose();
                }
            }
        });
        fenetre.getContentPane().setLayout(null);

        btnAjouter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FenetreModification fenetre2 = new FenetreModification(Constantes.CONTEXTE_AJOUTER);
                fenetre2.fenetre.setVisible(true);
            }
        });
        btnEnAttente.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FenetreModification fenetre2 = new FenetreModification(Constantes.CONTEXTE_EN_ATTENTE);
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
        panelNotifications.setLayout(new CardLayout(0, 0));

        listEvenementsEnAttente = new JList();

        //TODO remplir liste avec evenements en attente
        /*Exemple de comment remplir la liste*/
        String[] values = new String[] {"evenement 1", "evenement 2"};
        DefaultListModel model = new DefaultListModel();
        for(String v : values)
        {
            model.addElement(v);
        }
        listEvenementsEnAttente.setModel(model);


        panelNotifications.add(listEvenementsEnAttente, "name_56412892408382");


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

                if (chckbxAccidents.isSelected()) {
                    allEvents.removeAll(listeAccidents);
                    listeAccidents = wrapperEvenement(EvenementAccess.getActif("accidents", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeAccidents);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }

                if (chckbxTravaux.isSelected()) {
                    allEvents.removeAll(listeTravaux);
                    listeTravaux = wrapperEvenement(EvenementAccess.getActif("travaux", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeTravaux);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }

                if (chckbxManifestations.isSelected()) {
                    allEvents.removeAll(listeManifestations);
                    listeManifestations = wrapperEvenement(EvenementAccess.getActif("manifestations", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeManifestations);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }

                if (chckbxRenovation.isSelected()) {
                    allEvents.removeAll(listeRenovations);
                    listeRenovations = wrapperEvenement(EvenementAccess.getActif("rénovations", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeRenovations);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }

                if (chckbxConstruction.isSelected()) {
                    allEvents.removeAll(listeConstructions);
                    listeConstructions = wrapperEvenement(EvenementAccess.getActif("constructions", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeConstructions);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }

                if (chckboxDoleances.isSelected()) {
                    allEvents.removeAll(listeDoleances);
                    listeDoleances = wrapperEvenement(EvenementAccess.getActif("doléances", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeDoleances);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }

            }

        });

        JScrollPane scrollPaneRubriques = new JScrollPane();
        scrollPaneRubriques.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneRubriques.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneRubriques.setBounds(5, 260, 218, 705);
        panelPrincipal.add(scrollPaneRubriques);
        scrollPaneRubriques.setViewportView(panelRubriques);
        panelRubriques.setLayout(null);

        //TRAFIC et ses checkbox accidents et travaux
        labelTrafic.setFont(new Font("Dialog", Font.BOLD, 16));
        labelTrafic.setBounds(12, 37, 107, 29);
        panelRubriques.add(labelTrafic);

        chckbxAccidents.setBounds(20, 74, 190, 23);
        panelRubriques.add(chckbxAccidents);

        chckbxTravaux.setBounds(20, 102, 190, 23);
        panelRubriques.add(chckbxTravaux);

        //MANIFESTATION et sa checkbox
        labelManifestations.setFont(new Font("Dialog", Font.BOLD, 16));
        labelManifestations.setBounds(12, 152, 164, 29);
        panelRubriques.add(labelManifestations);

        chckbxManifestations.setBounds(20, 189, 190, 23);
        panelRubriques.add(chckbxManifestations);


        //CHANTIERS et ses checkbox renovation et construction
        labelChantiers.setFont(new Font("Dialog", Font.BOLD, 16));
        labelChantiers.setBounds(12, 230, 107, 29);
        panelRubriques.add(labelChantiers);

        chckbxRenovation.setBounds(20, 267, 129, 23);
        panelRubriques.add(chckbxRenovation);

        chckbxConstruction.setBounds(20, 294, 129, 23);
        panelRubriques.add(chckbxConstruction);

        // DOLEANCES et sa checkbox
        labelDoleances.setFont(new Font("Dialog", Font.BOLD, 16));
        labelDoleances.setBounds(12, 338, 107, 29);
        panelRubriques.add(labelDoleances);

        chckboxDoleances.setBounds(20, 375, 129, 23);
        panelRubriques.add(chckboxDoleances);


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


                if (((AbstractButton) e.getSource()).isSelected()) {
                    allEvents.addAll(listeAccidents = wrapperEvenement(EvenementAccess.getActif("accidents", dateSelectionne, Statut_.TRAITE)));
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

                if (((AbstractButton) e.getSource()).isSelected()) {
                    allEvents.addAll(listeTravaux = wrapperEvenement(EvenementAccess.getActif("travaux", dateSelectionne, Statut_.TRAITE)));
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

                if (((AbstractButton) e.getSource()).isSelected()) {
                    listeManifestations = wrapperEvenement(EvenementAccess.getActif("manifestations", dateSelectionne, Statut_.TRAITE));
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

                if (((AbstractButton) e.getSource()).isSelected()) {
                    listeRenovations = wrapperEvenement(EvenementAccess.getActif("rénovations", dateSelectionne, Statut_.TRAITE));
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

                if (((AbstractButton) e.getSource()).isSelected()) {
                    listeConstructions = wrapperEvenement(
                            EvenementAccess.getActif("constructions", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeConstructions);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                } else {
                    allEvents.removeAll(listeConstructions);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }
            }
        });

        chckboxDoleances.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (((AbstractButton) e.getSource()).isSelected()) {
                    listeDoleances = wrapperEvenement(
                            EvenementAccess.getActif("doléances", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeDoleances);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                } else {
                    allEvents.removeAll(listeDoleances);
                    carte.updateEvenement((ArrayList<Event>) allEvents);
                }
            }
        });

    }

    private ArrayList<Event> wrapperEvenement(List<Evenement> listeEvenement) {
        ArrayList<Event> evenements = new ArrayList<>();

        for (Evenement e : listeEvenement) {
            PointWGS84 point = new PointWGS84(e.getLatitude(), e.getLongitude());
            evenements.add(new Event(e.getNomEvenement(), point, e.getRubriqueEnfant().getIdRubriqueEnfant()));
        }

        return evenements;
    }

}