package ch.smartcity.graphique;

import ch.smartcity.database.controllers.access.EvenementAccess;
import ch.smartcity.database.models.Adresse;
import ch.smartcity.database.models.Evenement;
import ch.smartcity.database.models.Npa;
import ch.smartcity.database.models.Statut_;
import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FenetreModification {


    // constantes
    private static final String TITRE_MODIFICATION = "SmartCity - Ajout / Modification";
    private static final String TITRE_EN_ATTENTE = "SmartCity - En Attente";

    // tailles maximum
    private static final int TAILLE_MAX_NOM = 90;
    private static final int TAILLE_MAX_RUE = 45;
    private static final int TAILLE_MAX_NUMERO_RUE = 4;
    private static final int TAILLE_MAX_DETAILS = 50000;
    // controle des caracteres de la saisie
    //TODO : accepter apostrphe et tiret
    private static final String REGEX_ALPHA_NUMERIQUE = "[a-zA-ZÀ-ÿ0-9]";
    private static final String REGEX_NUMERIQUE = "[0-9]*";

    private static final String REGEX_LATITUDE = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$";
    private static final String REGEX_LONGITUDE = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$";
    private static final String REGEX_DATE = "^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$";

    // messages d'erreurs
    private static final String MESSAGE_ERREUR_NOM = "- Nom doit être constitué de lettre et de chiffres et avoir une taille maximum de " + TAILLE_MAX_NOM + " caractères\n";
    private static final String MESSAGE_ERREUR_RUE = "- Rue doit être constitué de lettre et de chiffres et avoir une taille maximum de " + TAILLE_MAX_RUE + " caractères\n";
    private static final String MESSAGE_ERREUR_NUMERO_RUE = "- N°Rue doit être constitué de chiffres uniquement et avoir une taille maximum de " + TAILLE_MAX_NUMERO_RUE + " caractères\n";
    private static final String MESSAGE_ERREUR_LATITUDE = "- Latitude doit avoir le format degrés signés\n";
    private static final String MESSAGE_ERREUR_LONGITUDE = "- Longitude doit avoir le format degrés signés\n";
    private static final String MESSAGE_ERREUR_DETAILS = "- Détails doivent avoir une taille maxmimum de: " + TAILLE_MAX_DETAILS + " caractères\n";
    private static final String MESSAGE_ERREUR_DATE = "- La date doit être du format jj/mm/aaaa et la date de fin doit être postérieur à la date de début et à la date d'aujourd'hui\n";


    private static final String ENTETE_ERREUR_SAISIE = "Erreur(s) lors de la saisie:\nTout les champs doivent êtres remplis\n";
    public JFrame fenetre;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    JComboBox<String> comboBoxEvenements;
    JComboBox<String> comboBoxPriorite;
    private JTextField textFieldNom;
    private JTextField textFieldRue;
    private JTextField textFieldNumRue;
    private JTextField textFieldLatitude;
    private JTextField textFieldLongitude;
    private JTextField textFieldDateDebut;
    private JTextField textFieldDateFin;
    private JTextArea textAreaDetails;
    private JLabel labelNom;
    private JLabel labelRue;
    private JLabel labelNumRue;
    private JLabel labelLongitude;
    private JLabel labelLatitude;
    private JLabel labelDateDebut;
    private JLabel labelDateFin;
    private JLabel labelDetails;
    private JPanel panelErreurSaisie;
    private JTextPane ErreurSaisiePane;
    private JComboBox<String> comboBoxNpa;

    /**
     * Create the application.
     */
    public FenetreModification(int contexte) {
        initialize(contexte);

    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize(int context) {
        fenetre = new JFrame();
        fenetre.setAlwaysOnTop(true);


        fenetre.setResizable(false);
        fenetre.setBounds(0, 0, 1200, 800);
        fenetre.setLocationRelativeTo(null);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelModification = new JPanel();
        panelModification.setForeground(Color.LIGHT_GRAY);
        fenetre.getContentPane().add(panelModification, BorderLayout.CENTER);
        panelModification.setLayout(null);

        comboBoxEvenements = new JComboBox<>();
        comboBoxEvenements.setMaximumRowCount(16);

        comboBoxEvenements.setBounds(61, 46, 701, 41);
        //System.out.println(comboBoxEvenements.getSelectedIndex()); // ajout d'un nouvel événement par défaut

        JPanel panelAjoutEvenement = new JPanel();
        panelAjoutEvenement.setForeground(Color.LIGHT_GRAY);
        panelAjoutEvenement.setBounds(12, 207, 1170, 552);
        panelModification.add(panelAjoutEvenement);
        panelAjoutEvenement.setLayout(null);

        textFieldNom = new JTextField();
        textFieldNom.setBounds(147, 12, 183, 37);
        panelAjoutEvenement.add(textFieldNom);
        textFieldNom.setColumns(10);

        labelNom = new JLabel("Nom");
        labelNom.setBounds(59, 23, 70, 15);
        panelAjoutEvenement.add(labelNom);

        textFieldRue = new JTextField();
        textFieldRue.setColumns(10);
        textFieldRue.setBounds(147, 139, 183, 37);
        panelAjoutEvenement.add(textFieldRue);

        textFieldNumRue = new JTextField();
        textFieldNumRue.setColumns(10);
        textFieldNumRue.setBounds(147, 199, 183, 37);
        panelAjoutEvenement.add(textFieldNumRue);

        textFieldLatitude = new JTextField();
        textFieldLatitude.setColumns(10);
        textFieldLatitude.setBounds(147, 257, 183, 37);
        panelAjoutEvenement.add(textFieldLatitude);

        textFieldLongitude = new JTextField();
        textFieldLongitude.setColumns(10);
        textFieldLongitude.setBounds(147, 316, 183, 37);
        panelAjoutEvenement.add(textFieldLongitude);

        labelLatitude = new JLabel("Latitude");
        labelLatitude.setBounds(59, 268, 70, 15);
        panelAjoutEvenement.add(labelLatitude);

        JLabel labelRubrique = new JLabel("Rubrique");
        labelRubrique.setBounds(59, 84, 70, 15);
        panelAjoutEvenement.add(labelRubrique);

        labelLongitude = new JLabel("longitude");
        labelLongitude.setBounds(59, 327, 70, 15);
        panelAjoutEvenement.add(labelLongitude);

        labelRue = new JLabel("Rue");
        labelRue.setBounds(59, 150, 70, 15);
        panelAjoutEvenement.add(labelRue);

        labelNumRue = new JLabel("N°Rue");
        labelNumRue.setBounds(59, 210, 70, 15);
        panelAjoutEvenement.add(labelNumRue);

        comboBoxNpa = new JComboBox<String>();
        comboBoxNpa.setModel(new DefaultComboBoxModel<String>(new String[]{"1000", "1001", "1002", "1003", "1004", "1005", "1006", "1007", "1010", "1011", "1012", "1015", "1018"}));
        comboBoxNpa.setBounds(539, 12, 183, 37);
        panelAjoutEvenement.add(comboBoxNpa);

        JLabel labelNpa = new JLabel("NPA");
        labelNpa.setBounds(442, 23, 70, 15);
        panelAjoutEvenement.add(labelNpa);

        comboBoxPriorite = new JComboBox<String>();
        comboBoxPriorite.setModel(new DefaultComboBoxModel<String>(new String[]{"0 - Mineur", "1 - Gênant", "2 - Préoccupant", "3 - Important", "4 - Urgent "}));
        comboBoxPriorite.setBounds(539, 79, 183, 37);
        panelAjoutEvenement.add(comboBoxPriorite);

        JComboBox<String> comboBoxRubrique = new JComboBox<String>();
        comboBoxRubrique.setModel(new DefaultComboBoxModel<String>(new String[]{"Accidents", "Travaux", "Manifestations", "Rénovations", "Constructions", "Doléances"}));
        comboBoxRubrique.setBounds(147, 79, 183, 37);
        panelAjoutEvenement.add(comboBoxRubrique);

        JLabel labelPriorite = new JLabel("Priorité");
        labelPriorite.setBounds(442, 84, 70, 15);
        panelAjoutEvenement.add(labelPriorite);

        textFieldDateDebut = new JTextField();
        textFieldDateDebut.setText("jj/mm/aaaa");
        textFieldDateDebut.setColumns(10);
        textFieldDateDebut.setBounds(539, 138, 183, 37);
        panelAjoutEvenement.add(textFieldDateDebut);

        labelDateDebut = new JLabel("Date Début");
        labelDateDebut.setBounds(442, 150, 81, 15);
        panelAjoutEvenement.add(labelDateDebut);

        textFieldDateFin = new JTextField();
        textFieldDateFin.setText("jj/mm/aaaa");
        textFieldDateFin.setColumns(10);
        textFieldDateFin.setBounds(539, 199, 183, 37);
        panelAjoutEvenement.add(textFieldDateFin);

        labelDateFin = new JLabel("Date Fin");
        labelDateFin.setBounds(442, 210, 81, 15);
        panelAjoutEvenement.add(labelDateFin);

        labelDetails = new JLabel("détails");
        labelDetails.setBounds(453, 385, 70, 15);
        panelAjoutEvenement.add(labelDetails);

        JScrollPane scrollPaneDetails = new JScrollPane();
        scrollPaneDetails.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneDetails.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneDetails.setBounds(539, 257, 631, 295);
        panelAjoutEvenement.add(scrollPaneDetails);

        textAreaDetails = new JTextArea();
        textAreaDetails.setLineWrap(true);
        scrollPaneDetails.setViewportView(textAreaDetails);

        JButton boutonValider = new JButton("Valider");
        boutonValider.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                if (controleSaisie()) {
                    System.out.println("Evenement valide");
                    //TODO: mettre evenement dans la base de données
                }
            }
        });
        boutonValider.setBounds(59, 412, 117, 25);
        panelAjoutEvenement.add(boutonValider);

        JButton boutonRefuser = new JButton("Refuser");
        boutonRefuser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO refuser l'évenenement,
                // le supprime de la base de donées
                // rafraichi la page de modification
            }
        });
        boutonRefuser.setBounds(59, 470, 117, 25);
        panelAjoutEvenement.add(boutonRefuser);

        JButton boutonSupprimer = new JButton("Supprimer");
        boutonSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO - supprime l evenement de la base de données
                // rafraichit la page
            }
        });
        boutonSupprimer.setBounds(213, 412, 117, 25);
        panelAjoutEvenement.add(boutonSupprimer);

        panelErreurSaisie = new JPanel();
        panelErreurSaisie.setBounds(734, 12, 436, 237);
        panelAjoutEvenement.add(panelErreurSaisie);
        panelErreurSaisie.setLayout(new CardLayout(0, 0));

        ErreurSaisiePane = new JTextPane();
        ErreurSaisiePane.setEditable(false);
        panelErreurSaisie.add(ErreurSaisiePane, "name_2674233502760");
        panelErreurSaisie.setEnabled(false);
        panelErreurSaisie.setVisible(false);


        panelModification.add(comboBoxEvenements);

        JPanel panelCalendrier = new JPanel();
        panelCalendrier.setBackground(Color.DARK_GRAY);
        panelCalendrier.setBounds(774, 12, 408, 195);
        panelModification.add(panelCalendrier);
        panelCalendrier.setLayout(new CardLayout(0, 0));

        final JCalendar calendrier = new JCalendar();
        calendrier.getDayChooser().setAlwaysFireDayProperty(true);
        //calendrier.getDayChooser().setAlwaysFireDayProperty(true); // permet d'ajouter la date lorsqu'on clique sur la date d'aujourd'hui
        calendrier.getDayChooser().addPropertyChangeListener("day", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {

                Date valDate = calendrier.getDate();
                String date = dateFormat.format(valDate);


                // remplis le champs date debut si celui-ci n'est pas valide, sinon remplit la date de fin
                if (!controlSaisie(textFieldDateDebut.getText(), REGEX_DATE)) // date debut
                {
                    textFieldDateDebut.setText(date);
                } else if (!controlSaisieDateFin(textFieldDateFin.getText(), REGEX_DATE)) // date fin
                {
                    textFieldDateFin.setText(date);
                }


            }

        });

        panelCalendrier.add(calendrier, "name_15195119934482");

        List<Evenement> evenementList;
        List<String> previews;
        if (context == 0) // ajout/modif TODO: constantes
        {
            //TODO recuperer seulements les evenements encore acifs

            //TODO <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            evenementList = EvenementAccess.getByFin(Calendar.getInstance());
//            evenementList = DatabaseAccess.get(Evenement.class); // recupere tout les evenements actifs
            //TODO <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

            previews = previewEvenement(evenementList); // previsualisation des evenements
            previews.add(0, "Ajouter un événement");
        } else // en attente
        {
            //TODO recuperation de la liste des evenements en attente pour la fenetre de en attente

            //TODO <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            evenementList = EvenementAccess.getByStatut(Statut_.EN_ATTENTE); // recupere tout les evenements en attente
//            evenementList = DatabaseAccess.get(Evenement.class); // recupere tout les evenements actifs
            //TODO <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

            previews = previewEvenement(evenementList); // previsualisation des evenements
            previews.add(0, "Selectionner");
            etatChamps(false);


        }
        // final pour utiliser dans actionPerformed
        //final List<Evenement> evenements = evenementList;

        comboBoxEvenements.setModel(new DefaultComboBoxModel<String>(previews.toArray(new String[0])));
        comboBoxEvenements.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {


                int index = comboBoxEvenements.getSelectedIndex();
                // TODO : constante context
                if (index != 0) // un evenement de la base de donnée
                {

                    if (context == 1) //TODO; constante
                    {
                        etatChamps(true); // deverouille les champs
                    }

                    // recupration de l evenement
                    Evenement evenement = evenementList.get(index - 1); // TODO: depent du context

                    // remplissage des champs
                    textFieldNom.setText(evenement.getNomEvenement());
                    textFieldRue.setText(evenement.getAdresse().getRue().getNomRue());
                    textFieldNumRue.setText(evenement.getAdresse().getNumeroDeRue());
                    textFieldLatitude.setText(evenement.getLatitude().toString());
                    textFieldLongitude.setText(evenement.getLongitude().toString());

                    //rubrique TODO: essayer pour toutes les rubriques enfants
                    // et tester ajout dynamique de la rubrique doleances
                    comboBoxRubrique.setSelectedIndex(evenement.getRubriqueEnfant().getIdRubriqueEnfant() - 1);
                    int i = getIndexNpa(evenement.getAdresse().getNpa());
                    comboBoxNpa.setSelectedIndex(i);
                    comboBoxPriorite.setSelectedIndex(evenement.getPriorite().getNiveau());

                    Calendar c = evenement.getDebut();
                    String date = dateFormat.format(c.getTime());
                    textFieldDateDebut.setText(date);
                    c = evenement.getFin();
                    date = dateFormat.format(c.getTime());
                    textFieldDateFin.setText(date);
                    textAreaDetails.setText(evenement.getDetails());

                } else if (context == 1 && index == 0) // TODO: constantes
                {
                    // verouille les champs afin de forcer l utilisateur a modifier un evenement qui est en attente
                    etatChamps(false);
                    videChamps();
                } else {
                    videChamps();
                }

            }

            private int getIndexNpa(Npa npa) {

                String n = npa.getNumeroNpa();
                int index;
                switch (n) {
                    case "1000":
                        index = 0;
                        break;
                    case "1001":
                        index = 1;
                        break;
                    case "1002":
                        index = 2;
                        break;
                    case "1003":
                        index = 3;
                        break;
                    case "1004":
                        index = 4;
                        break;
                    case "1005":
                        index = 5;
                        break;
                    case "1006":
                        index = 6;
                        break;
                    case "1007":
                        index = 7;
                        break;
                    case "1010":
                        index = 8;
                        break;
                    case "1011":
                        index = 9;
                        break;
                    case "1012":
                        index = 10;
                        break;
                    case "1015":
                        index = 12;
                        break;
                    case "1018":
                        index = 13;
                        break;
                    default:
                        index = 0;
                        break;
                }
                return index;
            }
        });


        JButton btnFermer = new JButton("Fermer");
        // ferme la fenetre lorsque le bouton est appuie
        btnFermer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fenetre.dispose();
            }
        });
        btnFermer.setBounds(12, 12, 117, 25);
        panelModification.add(btnFermer);


        if (context == 0) //TODO constantes
        {
            fenetre.setTitle(TITRE_MODIFICATION);
            boutonRefuser.setVisible(false);
            boutonRefuser.setEnabled(false);
        } else if (context == 1) //TODO constantes
        {
            fenetre.setTitle(TITRE_EN_ATTENTE);
            boutonSupprimer.setVisible(false);
            boutonSupprimer.setEnabled(false);
        } else {
            //TODO
            // Leve une exeption
        }


    } // fin initialize

    /**
     * Cette methode controle la saisie de tout les champs de la fenetre lors de l'appuis du bouton valider
     *
     * @return vrai si la saisie est correcte, faux sinon
     */
    private boolean controleSaisie() {

        // nettoyage labels rubriques et messages d'erreur de saisie
        ErreurSaisiePane.setText(ENTETE_ERREUR_SAISIE); // entete des messages d'erreurs de saisie

        labelNom.setForeground(Color.BLACK);
        labelRue.setForeground(Color.BLACK);
        labelNumRue.setForeground(Color.BLACK);
        labelLatitude.setForeground(Color.BLACK);
        labelLongitude.setForeground(Color.BLACK);
        labelDateDebut.setForeground(Color.BLACK);
        labelDateFin.setForeground(Color.BLACK);
        labelDetails.setForeground(Color.BLACK);

        boolean valide = true;

        // controle du nom de l'evenement
        if (!controleSaisie(textFieldNom.getText(), TAILLE_MAX_NOM, REGEX_ALPHA_NUMERIQUE)) {
            // saisie incorrect, affichage du nom de la rubrique en rouge
            labelNom.setForeground(Color.RED);
            ErreurSaisiePane.setText(ErreurSaisiePane.getText() + MESSAGE_ERREUR_NOM);


            valide = false;
        }

        // controle de la rue
        if (!controleSaisie(textFieldRue.getText(), TAILLE_MAX_RUE, REGEX_ALPHA_NUMERIQUE)) {
            // saisie incorrect, affichage du nom de la rubrique en rouge
            labelRue.setForeground(Color.RED);
            ErreurSaisiePane.setText(ErreurSaisiePane.getText() + MESSAGE_ERREUR_RUE);
            valide = false;
        }

        //controle numero rue
        if (!controleSaisie(textFieldNumRue.getText(), TAILLE_MAX_NUMERO_RUE, REGEX_NUMERIQUE)) {
            labelNumRue.setForeground(Color.RED);
            ErreurSaisiePane.setText(ErreurSaisiePane.getText() + MESSAGE_ERREUR_NUMERO_RUE);
            valide = false;
        }

        // controle latitude
        if (!controlSaisie(textFieldLatitude.getText(), REGEX_LATITUDE)) {
            labelLatitude.setForeground(Color.RED);
            ErreurSaisiePane.setText(ErreurSaisiePane.getText() + MESSAGE_ERREUR_LATITUDE);
            valide = false;
        }

        // controle longitude
        if (!controlSaisie(textFieldLongitude.getText(), REGEX_LONGITUDE)) {
            labelLongitude.setForeground(Color.RED);
            ErreurSaisiePane.setText(ErreurSaisiePane.getText() + MESSAGE_ERREUR_LONGITUDE);
            valide = false;
        }

        // controle details
        if (!controlSaisie(textAreaDetails.getText(), TAILLE_MAX_DETAILS)) {
            labelDetails.setForeground(Color.RED);
            ErreurSaisiePane.setText(ErreurSaisiePane.getText() + MESSAGE_ERREUR_DETAILS);
            valide = false;
        }

        // controle date de debut
        if (!controlSaisie(textFieldDateDebut.getText(), REGEX_DATE)) {
            labelDateDebut.setForeground(Color.RED);
            ErreurSaisiePane.setText(ErreurSaisiePane.getText() + MESSAGE_ERREUR_DATE);
            valide = false;
        }

        // controle date de fin
        if (!controlSaisieDateFin(textFieldDateFin.getText(), REGEX_DATE)) {
            labelDateFin.setForeground(Color.RED);
            ErreurSaisiePane.setText(ErreurSaisiePane.getText() + MESSAGE_ERREUR_DATE);
            valide = false;
        }


        if (valide == true) // saisie valide fermeture du panel de mauvaise saisie
        {

            ErreurSaisiePane.setEnabled(false);
            //ErreurSaisiePane.setText(ENTETE_ERREUR_SAISIE); // nettoye les messages d'erreurs

            panelErreurSaisie.setVisible(false);
            panelErreurSaisie.setEnabled(false);

        } else {
            ErreurSaisiePane.setEnabled(true);
            ErreurSaisiePane.setVisible(true);
            panelErreurSaisie.setVisible(true);
            panelErreurSaisie.setEnabled(true);
        }

        return valide;

    }

    /**
     * @param texte
     * @param regex
     * @return
     * @brief
     */
    private boolean controlSaisie(String texte, String regex) {

        if (texte.isEmpty() || !texte.matches(regex)) {
            return false;
        }

        return true;
    }

    /**
     * @param texte
     * @param taillemax
     * @return
     * @brief
     */
    private boolean controlSaisie(String texte, int taillemax) {

        if (texte.isEmpty() || texte.length() > taillemax) {
            return false;
        }

        return true;
    }

    /**
     * @param texte
     * @param tailleMax
     * @param regex
     * @return
     * @brief
     */
    private boolean controleSaisie(String texte, int tailleMax, String regex) {

        // controle taille
        if (texte.length() > tailleMax || texte.isEmpty()) {
            return false;
        }
        // controle lettres et chiffres
        if (!texte.matches(regex)) {
            return false;
        }

        return true;
    }

    /**
     * @param texte
     * @param regex
     * @return
     * @brief
     */
    private boolean controlSaisieDateFin(String texte, String regex) {
        if (!controlSaisie(textFieldDateDebut.getText(), REGEX_DATE)) // date de debut non valide, on ne peut pas tester la date de fin
        {
            return false;
        }


        if (texte.isEmpty() || !texte.matches(regex)) {
            return false;
        }

        Date date = new Date();
        String dateAujourdhui = dateFormat.format(date); // conversion

        String partiesDate[] = texte.split("/");
        String partiesDateAujourdhui[] = dateAujourdhui.split("/");

        String dateDebut = textFieldDateDebut.getText();
        String partieDateDebut[] = dateDebut.split("/");

        // controle que la date saisie soit au plus tot aujourd'hui
        //  et que la date saisie soit au plus tot le meme jour que la date de debut
        for (int i = 2; i >= 0; i--) {

            if (Integer.parseInt(partiesDate[i]) < Integer.parseInt(partiesDateAujourdhui[i]) || Integer.parseInt(partiesDate[i]) < Integer.parseInt(partieDateDebut[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param liste liste d'evenement
     * @return liste des previews des evenements
     * @brief Recoit une liste d'événement afin d'en afficher une prévisualisation
     * comprenant moins de détails que l'evenement complet
     */
    private List<String> previewEvenement(List<Evenement> liste) {
        ArrayList<String> preview = new ArrayList<>();
        if (liste == null || liste.isEmpty()) {
            return preview;
        }

        // parcours la liste des evenements
        for (Evenement e : liste) {
            String str = "";
            str += e.getIdEvenement() + " / ";
            str += e.getNomEvenement() + " / ";

            // adresse
            Adresse a = e.getAdresse();
            str += a.getRue().getNomRue() + " " + a.getNumeroDeRue() + " / ";

            // dates
            Calendar c = e.getDebut();
            //System.out.println(c);
            String date = dateFormat.format(c.getTime());
            str += date + " / ";
            c = e.getFin();
            //TODO: mettre not null pour date de fin dans mysql
            // et supprimer ce test
            if (c == null) {
                str += " null ";
            } else {
                date = dateFormat.format(c.getTime());
                str += date;
            }
            //str += date;

            preview.add(str); // ajout a la liste
        }
        return preview;
    }

    public void etatChamps(boolean b) {
        textFieldNom.setEditable(b);
        textFieldRue.setEditable(b);
        textFieldNumRue.setEditable(b);
        textFieldLatitude.setEditable(b);
        textFieldLongitude.setEditable(b);
        comboBoxNpa.setEditable(b);
        comboBoxPriorite.setEditable(b);
        ;
        textFieldDateDebut.setEditable(b);
        textFieldDateFin.setEditable(b);
        textAreaDetails.setEditable(b);

    }

    public void videChamps() {
        textFieldNom.setText("");
        textFieldRue.setText("");
        textFieldNumRue.setText("");
        textFieldLatitude.setText("");
        textFieldLongitude.setText("");
        comboBoxNpa.setSelectedIndex(0);
        comboBoxPriorite.setSelectedIndex(0);
        textFieldDateDebut.setText("jj/mm/aaaa");
        textFieldDateFin.setText("jj/mm/aaaa");
        textAreaDetails.setText("");

    }
}
