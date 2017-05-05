package ch.smartcity.graphique;

import ch.smartcity.carte.Carte;
import com.toedter.calendar.JCalendar;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


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
        Carte carte = null;
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

    }
}