package ch.smartcity.graphique;

import ch.smartcity.carte.Carte;
import ch.smartcity.carte.Event;
import ch.smartcity.carte.PointWGS84;
import ch.smartcity.database.controllers.access.EvenementAccess;
import ch.smartcity.database.models.Evenement;
import ch.smartcity.graphique.controllers.ConfigurationManager;
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

/**
 * Fenêtre principale de l'application
 * Affiche le logo de l'application
 * Affiche à l'écran les différentes checkboxes et le calendrier servant de filtre
 * aux événements à afficher.
 * Afficher une descriptions des événements filtrés
 * Affiche une point de couleur (suivant la rubrique de l'événement) aux coordonnées
 * des points filtrés.
 * Affiche une prévisualisation des événements en attente.
 * Met a disposition des boutons permettant de modifier les événements de la base de données,
 * et de générer un pdf de statistiques
 *
 * @author Tano Iannetta
 * @author Jérémie Zanone
 * @author Wojciech Myszkorowsk
 * @author Luana Martelli
 */
public class FenetrePrincipale {

    private final JPanel panelCalendrier = new JPanel();
    private final JTextField textRubriques = new JTextField();
    private final JPanel panelMenu = new JPanel();
    private final JPanel panelLogo = new JPanel();
    public JFrame fenetre = new JFrame();
    private ConfigurationManager configurationManager = ConfigurationManager.getInstance();
    private final JCheckBox chckbxAccidents =
            new JCheckBox(configurationManager.getString("rubriqueEnfant.accidents"));
    private final JCheckBox chckbxTravaux =
            new JCheckBox(configurationManager.getString("rubriqueEnfant.travaux"));
    private final JCheckBox chckbxManifestations =
            new JCheckBox(configurationManager.getString("rubriqueEnfant.manifestations"));
    private final JCheckBox chckbxRenovation =
            new JCheckBox(configurationManager.getString("rubriqueEnfant.renovations"));
    private final JCheckBox chckbxConstruction =
            new JCheckBox(configurationManager.getString("rubriqueEnfant.constructions"));
    private final JCheckBox chckboxDoleances =
            new JCheckBox(configurationManager.getString("rubriqueEnfant.doleances"));
    private final JLabel labelTrafic =
            new JLabel(configurationManager.getString("rubriqueParent.trafic"));
    private final JLabel labelManifestations =
            new JLabel(configurationManager.getString("rubriqueParent.manifestations"));
    private final JLabel labelChantiers =
            new JLabel(configurationManager.getString("rubriqueParent.chantier"));
    private final JLabel labelDoleances =
            new JLabel(configurationManager.getString("rubriqueParent.doleances"));
    private final JLabel lblNbrNotification =
            new JLabel(configurationManager.getString("entete.notifications"));
    private StringBuilder descriptionsEvents = new StringBuilder();
    private JPanel panelPrincipal = new JPanel();
    private JPanel panelCarte = new JPanel();
    private JPanel panelNotifications = new JPanel();
    private JButton btnAjouter =
            new JButton("ééééé" + configurationManager.getString("bouton.ajouterModifier"));
    private JButton btnPdf =
            new JButton(configurationManager.getString("bouton.pdf"));
    private JButton btnEnAttente = new
            JButton(configurationManager.getString("bouton.attente"));

    private GroupLayout gl_panelMenu = new GroupLayout(panelMenu);
    private JTextArea txtrDescription = new JTextArea();
    private JPanel panelRubriques = new JPanel();
    private JScrollPane scrollPaneDescription = new JScrollPane();
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
     * Utilisé pourl'accès à la base de données
     */
    private EvenementAccess evenementAccess = EvenementAccess.getInstance();

    /**
     * Créer l'application.
     */
    public FenetrePrincipale() {
        initialize();
    }

    /**
     * Initialise le contenu de la fenêtre
     */
    private void initialize() {
        Timer timer = new Timer();
        ThreadMiseAjourNotifications tache = new ThreadMiseAjourNotifications(
                Utils.previewEvenement(evenementAccess.getEnAttente()),
                listEvenementsEnAttente,
                lblNbrNotification);

        timer.scheduleAtFixedRate(tache, 2 * 60 * 100, 2 * 10 * 1000);
        tache.run();
        JList newListe = tache.getJliste();
        JLabel lblNbrNotification = tache.getPanelNotifications();

        gl_panelMenu.setHorizontalGroup(
                gl_panelMenu.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_panelMenu.createSequentialGroup()
                                .addComponent(btnAjouter)
                                .addGap(18)
                                .addComponent(btnPdf)
                                .addPreferredGap(
                                        ComponentPlacement.RELATED,
                                        347,
                                        Short.MAX_VALUE)
                                .addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
                                        .addComponent(lblNbrNotification)
                                        .addComponent(btnEnAttente))
                                .addContainerGap())
        );

        gl_panelMenu.setVerticalGroup(
                gl_panelMenu.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_panelMenu.createSequentialGroup()
                                .addContainerGap(13, Short.MAX_VALUE)
                                .addComponent(lblNbrNotification,
                                        GroupLayout.PREFERRED_SIZE,
                                        25,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_panelMenu.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(btnAjouter)
                                        .addComponent(btnEnAttente)
                                        .addComponent(btnPdf))
                                .addContainerGap())
        );

        //FENETRE PRINCIPALE
        fenetre.setTitle(configurationManager.getString("titre.application"));
        fenetre.setResizable(false);
        fenetre.setBounds(0, 0, 1900, 1000);

        //pop up de confirmation avant de quitter la fenetre
        fenetre.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        fenetre.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        configurationManager.getString("popup.texteQuitter"),
                        configurationManager.getString("popup.titreConfirmer"),
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
            FenetreModification fenetre2 =
                    new FenetreModification(Contexte.CONTEXTE_AJOUTER, this);
            fenetre2.fenetre.setVisible(true);
        });

        btnEnAttente.addActionListener(e -> {
            FenetreModification fenetre2 =
                    new FenetreModification(Contexte.CONTEXTE_EN_ATTENTE, this);
            fenetre2.fenetre.setVisible(true);
        });

        btnPdf.addActionListener(e -> {
            List<String> eventName = new ArrayList<>();
            if (chckbxAccidents.isSelected()) {
                eventName.add(configurationManager.getString("database.rubriqueAccidents"));
            }
            if (chckbxTravaux.isSelected()) {
                eventName.add(configurationManager.getString("database.rubriqueTravaux"));
            }
            if (chckbxManifestations.isSelected()) {
                eventName.add(configurationManager.getString("database.rubriqueManifestations"));
            }
            if (chckbxRenovation.isSelected()) {
                eventName.add(configurationManager.getString("database.rubriqueRenovations"));
            }
            if (chckbxConstruction.isSelected()) {
                eventName.add(configurationManager.getString("database.rubriqueConstructions"));
            }
            if (chckboxDoleances.isSelected()) {
                eventName.add(configurationManager.getString("database.rubriqueDoleances"));
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
        textNotifications.setText(configurationManager.getString("entete.evenementsEnAttente"));
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
        textDescription.setText(configurationManager.getString("entete.description"));
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
        URL imageIconURL = getClass().getClassLoader()
                .getResource("ch/smartcity/graphique/logo.png");
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
        textRubriques.setText(configurationManager.getString("entete.rubrique"));
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
                        .getActif(configurationManager.getString("database.rubriqueAccidents"),
                                dateSelectionne));

                allEvents.addAll(listeAccidents);
            } else {
                allEvents.removeAll(listeAccidents);
            }
            miseAJourAffichage();
        });

        chckbxTravaux.addActionListener(e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                listeTravaux = wrapperEvenement(evenementAccess
                        .getActif(configurationManager.getString("database.rubriqueTravaux"),
                                dateSelectionne));

                allEvents.addAll(listeTravaux);
            } else {
                allEvents.removeAll(listeTravaux);
            }
            miseAJourAffichage();
        });

        chckbxManifestations.addActionListener(e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                listeManifestations = wrapperEvenement(evenementAccess
                        .getActif(configurationManager.getString(
                                "database.rubriqueManifestations"),
                                dateSelectionne));

                allEvents.addAll(listeManifestations);
            } else {
                allEvents.removeAll(listeManifestations);
            }
            miseAJourAffichage();
        });

        chckbxRenovation.addActionListener(e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                listeRenovations = wrapperEvenement(evenementAccess
                        .getActif(configurationManager.getString(
                                "database.rubriqueRenovations"), dateSelectionne));
                allEvents.addAll(listeRenovations);
            } else {
                allEvents.removeAll(listeRenovations);
            }
            miseAJourAffichage();
        });

        chckbxConstruction.addActionListener(e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                listeConstructions = wrapperEvenement(evenementAccess
                        .getActif(configurationManager.getString(
                                "database.rubriqueConstructions"), dateSelectionne));
                allEvents.addAll(listeConstructions);
            } else {
                allEvents.removeAll(listeConstructions);
            }
            miseAJourAffichage();
        });

        chckboxDoleances.addActionListener(e -> {
            if (((AbstractButton) e.getSource()).isSelected()) {
                listeDoleances = wrapperEvenement(evenementAccess
                        .getActif(configurationManager.getString("database.rubriqueDoleances"),
                                dateSelectionne));
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

    /**
     * Utilise les checkboxes comme filtre pour afficher uniquement les événements des
     * rubriques sélectionnées
     */
    void recuperationEvenements() {
        if (chckbxAccidents.isSelected()) {
            allEvents.removeAll(listeAccidents);
            listeAccidents = wrapperEvenement(evenementAccess
                    .getActif(configurationManager.getString("database.rubriqueAccidents"),
                            dateSelectionne));

            allEvents.addAll(listeAccidents);
            miseAJourAffichage();
        }

        if (chckbxTravaux.isSelected()) {
            allEvents.removeAll(listeTravaux);
            listeTravaux = wrapperEvenement(evenementAccess
                    .getActif(configurationManager.getString("database.rubriqueTravaux"),
                            dateSelectionne));

            allEvents.addAll(listeTravaux);
            miseAJourAffichage();
        }

        if (chckbxManifestations.isSelected()) {
            allEvents.removeAll(listeManifestations);
            listeManifestations = wrapperEvenement(evenementAccess
                    .getActif(configurationManager.getString(
                            "database.rubriqueManifestations"), dateSelectionne));
            allEvents.addAll(listeManifestations);
            miseAJourAffichage();
        }

        if (chckbxRenovation.isSelected()) {
            allEvents.removeAll(listeRenovations);
            listeRenovations = wrapperEvenement(evenementAccess
                    .getActif(configurationManager.getString("database.rubriqueRenovations"),
                            dateSelectionne));
            allEvents.addAll(listeRenovations);
            miseAJourAffichage();
        }

        if (chckbxConstruction.isSelected()) {
            allEvents.removeAll(listeConstructions);
            listeConstructions = wrapperEvenement(evenementAccess
                    .getActif(configurationManager.getString("database.rubriqueConstructions"),
                            dateSelectionne));

            allEvents.addAll(listeConstructions);
            miseAJourAffichage();
        }

        if (chckboxDoleances.isSelected()) {
            allEvents.removeAll(listeDoleances);
            listeDoleances = wrapperEvenement(evenementAccess
                    .getActif(configurationManager.getString("database.rubriqueDoleances"),
                            dateSelectionne));

            allEvents.addAll(listeDoleances);
            miseAJourAffichage();
        }
    }
}