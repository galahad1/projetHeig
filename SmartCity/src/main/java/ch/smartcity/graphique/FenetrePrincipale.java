package ch.smartcity.graphique;

import ch.smartcity.carte.Carte;
import ch.smartcity.carte.Event;
import ch.smartcity.carte.PointWGS84;
import ch.smartcity.database.controllers.access.EvenementAccess;
import ch.smartcity.database.models.Evenement;
import ch.smartcity.database.models.Statut_;
import ch.smartcity.pdf.GenerateurPDF;
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
import java.util.*;
import java.util.List;
import java.util.Timer;


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

    public JFrame fenetre = new JFrame();
    StringBuilder descriptionsEvents = new StringBuilder();
    JPanel panelPrincipal = new JPanel();
    JPanel panelCarte = new JPanel();
    JPanel panelNotifications = new JPanel();
    JButton btnAjouter = new JButton("Ajouter/Modifier");
    JButton btnPdf = new JButton("PDF");
    JButton btnEnAttente = new JButton("En attente");

    GroupLayout gl_panelMenu = new GroupLayout(panelMenu);
    private JTextArea txtrDescription = new JTextArea();
    private JPanel panelRubriques = new JPanel();
    private JScrollPane scrollPaneDescription = new JScrollPane();

    //rubrique de selections événements
    private JScrollPane scrollPaneRubriques = new JScrollPane();

    private JCalendar calendrier = new JCalendar();
    private JList listEvenementsEnAttente = new JList();
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
    private JTextField textNotifications = new JTextField();

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

        //FENETRE PRINCIPALE
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

        //BOUTONS
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

        btnPdf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                List<String> eventName = new ArrayList<>();
                if (chckbxAccidents.isSelected()) {
                    eventName.add("accidents");
                }
                if (chckbxTravaux.isSelected()) {
                    eventName.add("travaux");
                }
                if (chckbxManifestations.isSelected()) {
                    eventName.add("manifestations");
                }
                if (chckbxRenovation.isSelected()) {
                    eventName.add("rénovations");
                }
                if (chckbxConstruction.isSelected()) {
                    eventName.add("constructions");
                }
                if (chckboxDoleances.isSelected()) {
                    eventName.add("doléances");
                }

                for (String s : eventName) {
                    try {
                        GenerateurPDF.cree(s, dateSelectionne);
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                        // popup erreur
                    }
                    System.out.println("PDF généré !");
                }

            }
        });
        panelPrincipal.setBounds(0, 0, 1900, 1000);
        fenetre.getContentPane().add(panelPrincipal);
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setLayout(null);

        panelPrincipal.add(panelCalendrier);
        panelMenu.setBounds(5, 119, 695, 81);

        panelPrincipal.add(panelMenu);
        panelMenu.setLayout(gl_panelMenu);

        //CARTE
        panelCarte.setBounds(700, 200, 1190, 765);
        panelCarte.setLayout(new BorderLayout());

        try {
            carte = new Carte();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        panelCarte.add(carte.createCenterPanel());

        panelPrincipal.add(panelCarte);

        //titre Notifications
        textNotifications.setText("Notifications en attente de validation");
        textNotifications.setHorizontalAlignment(SwingConstants.CENTER);
        textNotifications.setFont(new Font("Dialog", Font.BOLD, 18));
        textNotifications.setEditable(false);
        textNotifications.setColumns(6);
        textNotifications.setBackground(Color.LIGHT_GRAY);
        textNotifications.setBounds(700, 5, 782, 40);
        panelPrincipal.add(textNotifications);

        //NOTIFICATIONS
        panelNotifications.setBackground(Color.LIGHT_GRAY);
        panelNotifications.setBounds(700, 45, 782, 155);
        panelPrincipal.add(panelNotifications);
        panelNotifications.setLayout(new CardLayout(0, 0));

        //List<String> list = Utils.previewEvenement(EvenementAccess.getEnAttente());
        Timer timer = new Timer();
        MiseAjour tache = new MiseAjour(Utils.previewEvenement(EvenementAccess.getEnAttente()), listEvenementsEnAttente, lblNbrNotification);

        timer.scheduleAtFixedRate(tache, 2 * 60 * 100, 2 * 10 * 1000);
        tache.run();
        List<String> list = tache.getListe();
        JList newListe = tache.getJliste();
        JLabel lblNbrNotification = tache.getNombrePanel();

        panelNotifications.add(newListe, "name_56412892408382");

        //DESCRIPTION TITRE GRIS
        textDescription.setText("Description");
        textDescription.setHorizontalAlignment(SwingConstants.CENTER);
        textDescription.setFont(new Font("Dialog", Font.BOLD, 18));
        textDescription.setEditable(false);
        textDescription.setColumns(6);
        textDescription.setBackground(Color.LIGHT_GRAY);
        textDescription.setBounds(222, 200, 478, 62);
        panelPrincipal.add(textDescription);

        //Description des événements selectionné
        scrollPaneDescription.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneDescription.setViewportBorder(UIManager.getBorder("Button.border"));
        scrollPaneDescription.setBounds(222, 260, 478, 705);
        panelPrincipal.add(scrollPaneDescription);
        txtrDescription.setEditable(false);


        scrollPaneDescription.setViewportView(txtrDescription);


        //CALENDRIER
        panelCalendrier.setBackground(Color.DARK_GRAY);
        panelCalendrier.setBounds(1482, 5, 408, 195);
        panelCalendrier.setLayout(new CardLayout(0, 0));
        panelCalendrier.add(calendrier, "name_9865352109015");
        calendrier.getDayChooser().setAlwaysFireDayProperty(true);
        calendrier.getDayChooser().addPropertyChangeListener("day", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                Date valDate = calendrier.getDate();
                dateSelectionne = Calendar.getInstance();
                dateSelectionne.setTime(valDate);

                //TODO mettre a jour description aussi

                if (chckbxAccidents.isSelected()) {
                    allEvents.removeAll(listeAccidents);
                    listeAccidents = wrapperEvenement(EvenementAccess.getActif("accidents", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeAccidents);
                    miseAJourAffichage();
                }

                if (chckbxTravaux.isSelected()) {
                    allEvents.removeAll(listeTravaux);
                    listeTravaux = wrapperEvenement(EvenementAccess.getActif("travaux", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeTravaux);
                    miseAJourAffichage();
                }

                if (chckbxManifestations.isSelected()) {
                    allEvents.removeAll(listeManifestations);
                    listeManifestations = wrapperEvenement(EvenementAccess.getActif("manifestations", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeManifestations);
                    miseAJourAffichage();
                }

                if (chckbxRenovation.isSelected()) {
                    allEvents.removeAll(listeRenovations);
                    listeRenovations = wrapperEvenement(EvenementAccess.getActif("rénovations", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeRenovations);
                    miseAJourAffichage();
                }

                if (chckbxConstruction.isSelected()) {
                    allEvents.removeAll(listeConstructions);
                    listeConstructions = wrapperEvenement(EvenementAccess.getActif("constructions", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeConstructions);
                    miseAJourAffichage();
                }

                if (chckboxDoleances.isSelected()) {
                    allEvents.removeAll(listeDoleances);
                    listeDoleances = wrapperEvenement(EvenementAccess.getActif("doléances", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeDoleances);
                    miseAJourAffichage();
                }

            }

        });

        ImageIcon imageIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("ch/smartcity/graphique/logo.png"))
                .getImage().getScaledInstance(668, 75, Image.SCALE_DEFAULT));
        JLabel label = new JLabel("", JLabel.CENTER);
        label.setIcon(imageIcon);
        panelLogo.add(label, BorderLayout.CENTER);
        panelLogo.setBackground(Color.white);
        panelLogo.repaint();


        //add(picLabel);
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


        //panelLogo.setBackground(Color.GRAY);
        panelLogo.setBounds(5, 5, 695, 114);

        panelPrincipal.add(panelLogo);


        chckbxAccidents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (((AbstractButton) e.getSource()).isSelected()) {
                    listeAccidents = wrapperEvenement(
                            EvenementAccess.getActif("accidents", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeAccidents);
                } else {
                    allEvents.removeAll(listeAccidents);
                }
                miseAJourAffichage();
            }
        });

        chckbxTravaux.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (((AbstractButton) e.getSource()).isSelected()) {
                    listeTravaux = wrapperEvenement(
                            EvenementAccess.getActif("travaux", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeTravaux);
                } else {
                    allEvents.removeAll(listeTravaux);
                }
                miseAJourAffichage();
            }
        });

        chckbxManifestations.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (((AbstractButton) e.getSource()).isSelected()) {
                    listeManifestations = wrapperEvenement(
                            EvenementAccess.getActif("manifestations", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeManifestations);
                } else {
                    allEvents.removeAll(listeManifestations);
                }
                miseAJourAffichage();
            }
        });

        chckbxRenovation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (((AbstractButton) e.getSource()).isSelected()) {
                    listeRenovations = wrapperEvenement(
                            EvenementAccess.getActif("rénovations", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeRenovations);
                } else {
                    allEvents.removeAll(listeRenovations);
                }
                miseAJourAffichage();
            }
        });

        chckbxConstruction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (((AbstractButton) e.getSource()).isSelected()) {
                    listeConstructions = wrapperEvenement(
                            EvenementAccess.getActif("constructions", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeConstructions);
                } else {
                    allEvents.removeAll(listeConstructions);
                }
                miseAJourAffichage();
            }
        });

        chckboxDoleances.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (((AbstractButton) e.getSource()).isSelected()) {
                    listeDoleances = wrapperEvenement(
                            EvenementAccess.getActif("doléances", dateSelectionne, Statut_.TRAITE));
                    allEvents.addAll(listeDoleances);
                } else {
                    allEvents.removeAll(listeDoleances);
                }
                miseAJourAffichage();
            }
        });

    }

    private ArrayList<Event> wrapperEvenement(List<Evenement> listeEvenement) {
        ArrayList<Event> evenements = new ArrayList<>();

        //construit les nouveaux objets Event en rapport avec la carte
        for (Evenement e : listeEvenement) {
            PointWGS84 point = new PointWGS84(e.getLatitude(), e.getLongitude());
            evenements.add(new Event(e.getIdEvenement(), e.getPriorite().getNomPriorite(), e.getNomEvenement(),
                    e.getDebut(), e.getFin(), e.getAdresse(), e.getDetails(), point,
                    e.getRubriqueEnfant().getIdRubriqueEnfant()));
        }

        return evenements;
    }

    private void miseAJourAffichage() {
        //mise jour de la carte
        carte.updateEvenement((ArrayList<Event>) allEvents);

        //mise a jour de la description
        descriptionsEvents.setLength(0);
        for (Event evenement : allEvents) {
            descriptionsEvents.append(evenement.toString());
        }
        txtrDescription.setText(String.valueOf(descriptionsEvents));
    }

}