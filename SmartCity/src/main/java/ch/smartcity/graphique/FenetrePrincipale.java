package ch.smartcity.graphique;

import ch.smartcity.carte.Carte;
import ch.smartcity.carte.Event;
import ch.smartcity.carte.PointWGS84;
import ch.smartcity.database.controllers.access.EvenementAccess;
import ch.smartcity.database.models.Evenement;
import ch.smartcity.pdf.GenerateurPDF;
import com.toedter.calendar.JCalendar;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
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
    private StringBuilder descriptionsEvents = new StringBuilder();
    private JPanel panelPrincipal = new JPanel();
    private JPanel panelCarte = new JPanel();
    private JPanel panelNotifications = new JPanel();
    private JButton btnAjouter = new JButton("Ajouter/Modifier");
    private JButton btnPdf = new JButton("PDF");
    private JButton btnEnAttente = new JButton("En attente");

    private GroupLayout gl_panelMenu = new GroupLayout(panelMenu);
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

    private EvenementAccess evenementAccess = EvenementAccess.getInstance();

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
        Timer timer = new Timer();
        ThreadMiseAjourNotifications tache = new ThreadMiseAjourNotifications(Utils.previewEvenement(
                evenementAccess.getEnAttente()), listEvenementsEnAttente, lblNbrNotification);

        timer.scheduleAtFixedRate(tache, 2 * 60 * 100, 2 * 10 * 1000);
        tache.run();
        JList newListe = tache.getJliste();
        JLabel lblNbrNotification = tache.getNombrePanel();

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
                    timer.cancel();
                    timer.purge();
                    tache.cancel();
                    fenetre.dispose();
                    System.exit(0);
                }
            }
        });
        fenetre.getContentPane().setLayout(null);

        //BOUTONS
        btnAjouter.addActionListener(e -> {
            FenetreModification fenetre2 = new FenetreModification(Contexte.CONTEXTE_AJOUTER, this);
            fenetre2.fenetre.setVisible(true);
        });

        btnEnAttente.addActionListener(e -> {
            FenetreModification fenetre2 = new FenetreModification(Contexte.CONTEXTE_EN_ATTENTE, this);
            fenetre2.fenetre.setVisible(true);
        });

        btnPdf.addActionListener(e -> {
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
            /* Génération d'un PDF pour chaque filtre seléctionné */
            for (String s : eventName) {
                try {
                    GenerateurPDF.cree(s, dateSelectionne);
                } catch (Exception exception) {
                    exception.printStackTrace();
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

        new Thread(() -> {
            try {
                carte = new Carte();
                panelCarte.add(carte.createCenterPanel());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

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
        dateSelectionne = Calendar.getInstance();
        calendrier.getDayChooser().setAlwaysFireDayProperty(true);
        calendrier.getDayChooser().addPropertyChangeListener("day", e -> {
            Date valDate = calendrier.getDate();
            dateSelectionne.setTime(valDate);
            recuperationEvenements();
        });

        //LOGO PROGRAMME
        URL imageIconURL = getClass().getClassLoader().getResource("ch/smartcity/graphique/logo.png");
        ImageIcon imageIcon = null;

        if (imageIconURL != null) {
            imageIcon = new ImageIcon(new ImageIcon(imageIconURL).getImage());
        }

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

        chckbxAccidents.addActionListener(e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                listeAccidents = wrapperEvenement(evenementAccess
                        .getActif("accidents", dateSelectionne));
                allEvents.addAll(listeAccidents);
            } else {
                allEvents.removeAll(listeAccidents);
            }
            miseAJourAffichage();
        });

        chckbxTravaux.addActionListener(e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                listeTravaux = wrapperEvenement(evenementAccess
                        .getActif("travaux", dateSelectionne));
                allEvents.addAll(listeTravaux);
            } else {
                allEvents.removeAll(listeTravaux);
            }
            miseAJourAffichage();
        });

        chckbxManifestations.addActionListener(e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                listeManifestations = wrapperEvenement(evenementAccess
                        .getActif("manifestations", dateSelectionne));
                allEvents.addAll(listeManifestations);
            } else {
                allEvents.removeAll(listeManifestations);
            }
            miseAJourAffichage();
        });

        chckbxRenovation.addActionListener(e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                listeRenovations = wrapperEvenement(evenementAccess
                        .getActif("rénovations", dateSelectionne));
                allEvents.addAll(listeRenovations);
            } else {
                allEvents.removeAll(listeRenovations);
            }
            miseAJourAffichage();
        });

        chckbxConstruction.addActionListener(e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                listeConstructions = wrapperEvenement(evenementAccess
                        .getActif("constructions", dateSelectionne));
                allEvents.addAll(listeConstructions);
            } else {
                allEvents.removeAll(listeConstructions);
            }
            miseAJourAffichage();
        });

        chckboxDoleances.addActionListener(e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                listeDoleances = wrapperEvenement(evenementAccess
                        .getActif("doléances", dateSelectionne));
                allEvents.addAll(listeDoleances);
            } else {
                allEvents.removeAll(listeDoleances);
            }
            miseAJourAffichage();
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

    void recuperationEvenements() {
        System.out.println("recuperation evenement");

        if (chckbxAccidents.isSelected()) {
            allEvents.removeAll(listeAccidents);
            listeAccidents = wrapperEvenement(evenementAccess
                    .getActif("accidents", dateSelectionne));
            allEvents.addAll(listeAccidents);
            miseAJourAffichage();
        }

        if (chckbxTravaux.isSelected()) {
            allEvents.removeAll(listeTravaux);
            listeTravaux = wrapperEvenement(evenementAccess
                    .getActif("travaux", dateSelectionne));
            allEvents.addAll(listeTravaux);
            miseAJourAffichage();
        }

        if (chckbxManifestations.isSelected()) {
            allEvents.removeAll(listeManifestations);
            listeManifestations = wrapperEvenement(evenementAccess
                    .getActif("manifestations", dateSelectionne));
            allEvents.addAll(listeManifestations);
            miseAJourAffichage();
        }

        if (chckbxRenovation.isSelected()) {
            allEvents.removeAll(listeRenovations);
            listeRenovations = wrapperEvenement(evenementAccess
                    .getActif("rénovations", dateSelectionne));
            allEvents.addAll(listeRenovations);
            miseAJourAffichage();
        }

        if (chckbxConstruction.isSelected()) {
            allEvents.removeAll(listeConstructions);
            listeConstructions = wrapperEvenement(evenementAccess
                    .getActif("constructions", dateSelectionne));
            allEvents.addAll(listeConstructions);
            miseAJourAffichage();
        }

        if (chckboxDoleances.isSelected()) {
            allEvents.removeAll(listeDoleances);
            listeDoleances = wrapperEvenement(evenementAccess
                    .getActif("doléances", dateSelectionne));
            allEvents.addAll(listeDoleances);
            miseAJourAffichage();
        }
    }
}