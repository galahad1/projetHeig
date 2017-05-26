package ch.smartcity.graphique;

import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.access.*;
import ch.smartcity.database.models.*;
import ch.smartcity.graphique.controllers.ConfigurationManager;
import com.toedter.calendar.JCalendar;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * La fenêtre de modification est ouverte lors de l'appuis d'un bouton de la fenêtre principale.
 * Cette fenêtre fourni un formulaire qui permet d'ajouter ou de modifier un événement
 * de la base de donnée.
 * Elle permet aussi de valider les événements en attente, ainsi que de les
 * modifier avant de les valider.
 */
class FenetreModification {

    // controle des caractères de la saisie
    private static final String REGEX_ALPHA_NUMERIQUE = "[a-zA-ZÀ-ÿ0-9 \\-']*";
    private static final String REGEX_NUMERIQUE = "[0-9]*";
    private static final String REGEX_LATITUDE =
        "^[\\+-]?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$";
    private static final String REGEX_LONGITUDE =
        "^[\\+-]?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$";
    private static final String REGEX_DATE =
        "^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$";

    JFrame fenetre;
    private ConfigurationManager configurationManager = ConfigurationManager.getInstance();
    private DateFormat dateFormat = new SimpleDateFormat(configurationManager
            .getString("date.format"));
    private JComboBox<String> comboBoxEvenements;
    private JComboBox<String> comboBoxPriorite;
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
    private JScrollPane scrollPaneErreurSaisie;
    private JTextPane erreurSaisieTextPane;
    private JComboBox<String> comboBoxNpa;
    private JComboBox<String> comboBoxRubrique;
    private List<Evenement> evenementList;
    private Evenement evenementSelectionne = null;

    /**
     * Créer la fenêtre de modification des événements de la base de données
     * @param contexte définit si la fenêtre traite des événements en attente de validation
     * ou des événements déja validés
     * @param appelant fenêtre principale par la quelle la cette fenêtre est ouverte
     */
    FenetreModification(int contexte, FenetrePrincipale appelant) {
        initialize(contexte, appelant);
    }

    /**
     * Initialise la fenêtre de modification des événements de la base de données
     * @param context définit si la fenêtre traite des événements en attente de validation
     * ou des événements déja validés
     * @param appelant fenêtre principale par la quelle la cette fenêtre est ouverte
     */
    private void initialize(int context, FenetrePrincipale appelant) {
        fenetre = new JFrame();
        fenetre.setResizable(false);
        fenetre.setBounds(0, 0, 1200, 800);
        fenetre.setLocationRelativeTo(null);

        // fermeture de la fenetre lors de l'appuis sur la croix
        fenetre.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        fenetre.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                fenetre.dispose();
            }
        });

        JPanel panelModification = new JPanel();
        fenetre.getContentPane().add(panelModification, BorderLayout.CENTER);
        panelModification.setLayout(null);

        comboBoxEvenements = new JComboBox<>();
        comboBoxEvenements.setMaximumRowCount(16);
        comboBoxEvenements.setBounds(61, 46, 701, 41);
        panelModification.add(comboBoxEvenements);

        JPanel panelAjoutEvenement = new JPanel();
        panelAjoutEvenement.setBounds(12, 207, 1170, 552);
        panelModification.add(panelAjoutEvenement);
        panelAjoutEvenement.setLayout(null);

        textFieldNom = new JTextField();
        textFieldNom.setBounds(147, 12, 183, 37);
        panelAjoutEvenement.add(textFieldNom);
        textFieldNom.setColumns(10);

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

        labelLatitude = new JLabel(configurationManager.getString("label.latitude"));
        labelLatitude.setBounds(50, 268, 70, 15);
        panelAjoutEvenement.add(labelLatitude);

        JLabel labelRubrique = new JLabel(configurationManager.getString("label.rubrique"));
        labelRubrique.setBounds(50, 84, 80, 15);
        panelAjoutEvenement.add(labelRubrique);

        labelNom = new JLabel(configurationManager.getString("label.nom"));
        labelNom.setBounds(50, 23, 80, 15);
        panelAjoutEvenement.add(labelNom);

        labelLongitude = new JLabel(configurationManager.getString("label.longitude"));
        labelLongitude.setBounds(50, 327, 80, 15);
        panelAjoutEvenement.add(labelLongitude);

        labelRue = new JLabel(configurationManager.getString("label.rue"));
        labelRue.setBounds(50, 150, 80, 15);
        panelAjoutEvenement.add(labelRue);

        labelNumRue = new JLabel(configurationManager.getString("label.numRue"));
        labelNumRue.setBounds(50, 210, 80, 15);
        panelAjoutEvenement.add(labelNumRue);

        comboBoxNpa = new JComboBox<>();
        // liste déroulante des npa possibles de la base de données
        List<Npa> listNpa = DatabaseAccess.get(Npa.class);
        String[] npas = new String[listNpa.size()];
        for (int i = 0; i < npas.length; i++) {
            npas[i] = listNpa.get(i).toString();
        }
        comboBoxNpa.setModel(new DefaultComboBoxModel<>(npas));
        comboBoxNpa.setBounds(539, 12, 183, 37);
        panelAjoutEvenement.add(comboBoxNpa);

        JLabel labelNpa = new JLabel(configurationManager.getString("label.npa"));
        labelNpa.setBounds(442, 23, 70, 15);
        panelAjoutEvenement.add(labelNpa);

        comboBoxPriorite = new JComboBox<>();
        // liste déroulante des priorités possibles de la base de données
        List<Priorite> listPriorite = DatabaseAccess.get(Priorite.class);
        String[] priorites = new String[listPriorite.size()];
        for (int i = 0; i < priorites.length; i++) {
            priorites[i] = listPriorite.get(i).toString();
        }
        comboBoxPriorite.setModel(new DefaultComboBoxModel<>(priorites));
        comboBoxPriorite.setBounds(539, 79, 183, 37);
        panelAjoutEvenement.add(comboBoxPriorite);

        JLabel labelPriorite = new JLabel(configurationManager.getString("label.priorite"));
        labelPriorite.setBounds(442, 84, 70, 15);
        panelAjoutEvenement.add(labelPriorite);

        comboBoxRubrique = new JComboBox<>();
        // liste déroulante des rubriques possibles de la base de données

        List<RubriqueEnfant> listRubriqueEnfant = DatabaseAccess.get(RubriqueEnfant.class);
        String[] rubriques = new String[listRubriqueEnfant.size()];
        for (int i = 0; i < rubriques.length; i++) {
            rubriques[i] = listRubriqueEnfant.get(i).toString();
        }
        comboBoxRubrique.setModel(new DefaultComboBoxModel<>(rubriques));
        comboBoxRubrique.setBounds(147, 79, 183, 37);
        panelAjoutEvenement.add(comboBoxRubrique);

        textFieldDateDebut = new JTextField();
        textFieldDateDebut.setText(configurationManager.getString("date.format"));
        textFieldDateDebut.setColumns(10);
        textFieldDateDebut.setBounds(539, 138, 183, 37);
        panelAjoutEvenement.add(textFieldDateDebut);

        labelDateDebut = new JLabel(configurationManager.getString("label.dateDebut"));
        labelDateDebut.setBounds(442, 150, 81, 15);
        panelAjoutEvenement.add(labelDateDebut);

        textFieldDateFin = new JTextField();
        textFieldDateFin.setText(configurationManager.getString("date.format"));
        textFieldDateFin.setColumns(10);
        textFieldDateFin.setBounds(539, 199, 183, 37);
        panelAjoutEvenement.add(textFieldDateFin);

        labelDateFin = new JLabel(configurationManager.getString("label.dateFin"));
        labelDateFin.setBounds(442, 210, 81, 15);
        panelAjoutEvenement.add(labelDateFin);

        labelDetails = new JLabel(configurationManager.getString("label.details"));
        labelDetails.setBounds(453, 385, 70, 15);
        panelAjoutEvenement.add(labelDetails);

        JScrollPane scrollPaneDetails = new JScrollPane();
        scrollPaneDetails.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneDetails.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneDetails.setBounds(539, 257, 631, 295);
        panelAjoutEvenement.add(scrollPaneDetails);

        textAreaDetails = new JTextArea();
        textAreaDetails.setLineWrap(true);
        scrollPaneDetails.setViewportView(textAreaDetails);

        JButton btnFermer = new JButton(configurationManager.getString("bouton.fermer"));
        // ferme la fenetre lorsque le bouton est appuie
        btnFermer.addActionListener(e -> fenetre.dispose());
        btnFermer.setBounds(12, 12, 117, 25);
        panelModification.add(btnFermer);

        JButton boutonValider = new JButton(configurationManager.getString("bouton.valider"));
        boutonValider.addActionListener(e -> {
            if (controleSaisie()) {

                if (comboBoxEvenements.getSelectedIndex() == 0) // nouvel événement
                {
                    ajouterEvenement();
                } else // événement déjà exsistant
                {
                    modifierEvenement();
                }

                chargementListeEvenements(context); // mise a jour de la liste
                videChamps();
                // mise a jour de la liste des événements de la fenêtre appelante
                appelant.recuperationEvenements();
            }
        });
        boutonValider.setBounds(50, 412, 117, 25);
        panelAjoutEvenement.add(boutonValider);

        JButton boutonRefuser = new JButton(configurationManager.getString("bouton.refuser"));
        boutonRefuser.addActionListener(e -> {
            if (comboBoxEvenements.getSelectedIndex() != 0) {
                // statut en refuser et change date de fin pour etre en etat supprimé
                refuserEvenement();
                chargementListeEvenements(context); // mise a jour de la liste
                videChamps();

                JOptionPane.showConfirmDialog(null,
                        configurationManager.getString("popup.texteRefuser"),
                        configurationManager.getString("popup.titreRefuser"),
                        JOptionPane.DEFAULT_OPTION);

            }
        });
        boutonRefuser.setBounds(50, 470, 117, 25);
        panelAjoutEvenement.add(boutonRefuser);

        JButton boutonSupprimer = new JButton(
                configurationManager.getString("bouton.supprimer"));

        boutonSupprimer.addActionListener(e -> {
            if (evenementSelectionne != null && comboBoxEvenements.getSelectedIndex() != 0) {
                DatabaseAccess.delete(evenementSelectionne); // supprime de la base de donnée
                chargementListeEvenements(context); // mise a jour de la liste
                videChamps();

                JOptionPane.showConfirmDialog(null,
                        configurationManager.getString("popup.texteSupprimer"),
                        configurationManager.getString("popup.titreSupprimer"),
                        JOptionPane.DEFAULT_OPTION);
                // mise à jour de la liste des événements de la fenêtre appelante
                appelant.recuperationEvenements();
            }
        });
        boutonSupprimer.setBounds(213, 412, 117, 25);
        panelAjoutEvenement.add(boutonSupprimer);

        // détermine dans quel contexte la fenêtre est ouverte, pour afficher
        // les boutons en conséquence
        if (context == Contexte.CONTEXTE_AJOUTER) {
            fenetre.setTitle(configurationManager.getString("graphique.titreModification"));
            boutonRefuser.setVisible(false);
            boutonRefuser.setEnabled(false);
        } else { // en attente
            fenetre.setTitle(configurationManager.getString("graphique.titreEnAttente"));
            boutonSupprimer.setVisible(false);
            boutonSupprimer.setEnabled(false);
        }

        scrollPaneErreurSaisie = new JScrollPane();
        scrollPaneErreurSaisie.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneErreurSaisie.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneErreurSaisie.setBounds(734, 12, 436, 237);
        panelAjoutEvenement.add(scrollPaneErreurSaisie);

        erreurSaisieTextPane = new JTextPane();
        erreurSaisieTextPane.setEditable(false);
        scrollPaneErreurSaisie.setViewportView(erreurSaisieTextPane);
        scrollPaneErreurSaisie.setEnabled(false);
        scrollPaneErreurSaisie.setVisible(false);

        JPanel panelCalendrier = new JPanel();
        panelCalendrier.setBackground(Color.DARK_GRAY);
        panelCalendrier.setBounds(774, 12, 408, 195);
        panelModification.add(panelCalendrier);
        panelCalendrier.setLayout(new CardLayout(0, 0));

        final JCalendar calendrier = new JCalendar();
        calendrier.getDayChooser().setAlwaysFireDayProperty(true);
        // appuis sur une date du calendrier
        calendrier.getDayChooser().addPropertyChangeListener("day", evt -> {

            Date valDate = calendrier.getDate();
            String date = dateFormat.format(valDate);

            // remplis le champs date debut si celui-ci n'est pas valide
            // sinon remplit la date de fin
            if (!Utils.controlSaisie(textFieldDateDebut.getText(), REGEX_DATE)) // date debut
            {
                textFieldDateDebut.setText(date);
            } else if (!controlSaisieDateFin(textFieldDateFin.getText(), REGEX_DATE)) { // date fin
                textFieldDateFin.setText(date);
            }
        });

        panelCalendrier.add(calendrier, "name_15195119934482");

        // initialise la liste des evenements
        chargementListeEvenements(context);

        // lorsque l'on choisi un evenement dans la liste, on remplis les champs
        comboBoxEvenements.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBoxEvenements.getSelectedIndex();
                if (index != 0) // un evenement de la base de donnée
                {
                    if (context == Contexte.CONTEXTE_EN_ATTENTE) {
                        etatChamps(true); // deverouille les champs
                    }

                    // recupration de l evenement
                    Evenement evenement = evenementList.get(index - 1);
                    setEvenementSelectionne(evenement); // evenement dans le champs

                    // remplissage des champs
                    textFieldNom.setText(evenement.getNomEvenement());
                    textFieldRue.setText(evenement.getAdresse().getRue().getNomRue());
                    textFieldNumRue.setText(evenement.getAdresse().getNumeroDeRue());
                    textFieldLatitude.setText(evenement.getLatitude().toString());
                    textFieldLongitude.setText(evenement.getLongitude().toString());
                    comboBoxRubrique.setSelectedIndex(evenement.getRubriqueEnfant()
                            .getIdRubriqueEnfant() - 1);
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

                } else if (context == Contexte.CONTEXTE_EN_ATTENTE) {
                    // verouille les champs afin de forcer l utilisateur a chosir un événement
                    // qui est en attente
                    etatChamps(false); // verouille les champs
                    videChamps();
                } else { // aucun événement sélectionné
                    videChamps();
                }
            }

            /**
             * En fonction de l'NPA de l'événement, retourne le bon index pour setter
             * la liste déroulante de NPA sur la fenêtre
             * @param npa npa de l'événement
             * @return index de l'NPA de l'événement
             */
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
                        index = 11;
                        break;
                    case "1018":
                        index = 12;
                        break;
                    default:
                        index = 0;
                        break;
                }
                return index;
            }
        });

    } // fin initialize

    /**
     * Charge les previews des événements dans la liste déroulante
     * @param contexte définit si la fenêtre traite des événements en attente de validation
     * ou des événements déja validés
     */
    private void chargementListeEvenements(int contexte) {
        List<String> previews;

        if (contexte == Contexte.CONTEXTE_AJOUTER) { // ajout/modification
            evenementList = EvenementAccess.getActif();
            previews = Utils.previewEvenement(evenementList); // previsualisation des evenements
            previews.add(0, configurationManager.getString("liste.ajouter"));
        } else {  // en attente
            // recupere tout les evenements en attente
            evenementList = EvenementAccess.getEnAttente();

            previews = Utils.previewEvenement(evenementList); // previsualisation des evenements

            previews.add(0, configurationManager.getString("liste.selectionner"));
            etatChamps(false);
        }

        comboBoxEvenements.setModel(new DefaultComboBoxModel<>(previews.toArray(new String[0])));
    }

    /**
     * Refuse l'événement et le supprime
     */
    private void refuserEvenement() {

        evenementSelectionne.setStatut(StatutAccess.get(Statut_.REFUSE).get(0)); // statut refusé
        EvenementAccess.update(evenementSelectionne.getIdEvenement(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                evenementSelectionne.getStatut()); // met a jour l événement avec le statut refusé
        DatabaseAccess.delete(evenementSelectionne); // suprimme l'événement
    }

    /**
     * Modifie l'événement en fonction des nouvelles valeurs des champs du formulaire
     */
    private void modifierEvenement() {
        String nomEnfant = comboBoxRubrique.getSelectedItem().toString();
        String nomEvenement = textFieldNom.getText();
        String nomRue = textFieldRue.getText();
        String numeroRue = textFieldNumRue.getText();
        String npa = comboBoxNpa.getSelectedItem().toString();
        Double latitude = Double.valueOf(textFieldLatitude.getText());
        Double longitude = Double.valueOf(textFieldLongitude.getText());
        Calendar calDebut = Calendar.getInstance();
        Calendar calFin = Calendar.getInstance();
        String details = textAreaDetails.getText();

        Evenement evenementBase = getEvenementSelectionne();

        Npa newNpa = NpaAccess.get(npa).get(0);

        Rue newRue;
        List<Rue> listNewRue = RueAccess.get(nomRue);
        if (listNewRue == null || listNewRue.isEmpty()) {
            newRue = new Rue(nomRue);
        } else {
            newRue = listNewRue.get(0);
        }

        Adresse newAdresse;
        List<Adresse> listNewAdresse = AdresseAccess.get(newRue, numeroRue, newNpa);
        if (listNewAdresse == null || listNewAdresse.isEmpty()) {
            newAdresse = new Adresse(newRue, numeroRue, newNpa);
        } else {
            newAdresse = listNewAdresse.get(0);
        }

        RubriqueEnfant newRubrique = RubriqueEnfantAccess.get("",
                nomEnfant).get(0);
        Statut newStatut = StatutAccess.get(Statut_.TRAITE).get(0);

        // sépare niveau et nom de la priorité
        String[] elementsPriorite =
                comboBoxPriorite.getSelectedItem().toString().split(" - ");
        Priorite newPriorite = PrioriteAccess.get(
                elementsPriorite[1],
                Integer.valueOf(elementsPriorite[0])).get(0);

        try {
            calDebut.setTime(dateFormat.parse(textFieldDateDebut.getText()));
            calFin.setTime(dateFormat.parse(textFieldDateFin.getText()));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        EvenementAccess.update(evenementBase.getIdEvenement(),
                newRubrique,
                null,
                nomEvenement,
                newAdresse,
                latitude,
                longitude,
                calDebut,
                calFin,
                details,
                newPriorite,
                newStatut);

        JOptionPane.showConfirmDialog(null,
                configurationManager.getString("popup.texteModifier"),
                configurationManager.getString("popup.titreModifier"),
                JOptionPane.DEFAULT_OPTION);
    }

    /**
     * Ajoute l'événement à la base de donées en fonction des champs du formulaire
     */
    private void ajouterEvenement() {
        String nomEnfant = comboBoxRubrique.getSelectedItem().toString();
        String nomEvenement = textFieldNom.getText();
        String nomRue = textFieldRue.getText();
        String numeroRue = textFieldNumRue.getText();
        String npa = comboBoxNpa.getSelectedItem().toString();
        Double latitude = Double.valueOf(textFieldLatitude.getText());
        Double longitude = Double.valueOf(textFieldLongitude.getText());
        Calendar calDebut = Calendar.getInstance();
        Calendar calFin = Calendar.getInstance();
        String details = textAreaDetails.getText();

        try {
            calDebut.setTime(dateFormat.parse(textFieldDateDebut.getText()));
            calFin.setTime(dateFormat.parse(textFieldDateFin.getText()));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        // sé pare niveau et nom de la priorité
        String[] elementsPriorite = comboBoxPriorite.getSelectedItem().toString().split(" - ");

        // controle si l evenement exsite deja
        List<Evenement> evenementsExsistants = EvenementAccess.get(nomEnfant,
                null,
                nomEvenement,
                nomRue,
                numeroRue,
                npa,
                latitude,
                longitude,
                calDebut,
                calFin,
                details,
                elementsPriorite[1],
                null,
                null);
        if (evenementsExsistants == null || evenementsExsistants.isEmpty()) { // n'exsiste pas
            // ajoute l'événement
            EvenementAccess.save(nomEnfant,
                    1,
                    nomEvenement,
                    nomRue,
                    numeroRue,
                    npa,
                    latitude,
                    longitude,
                    calDebut,
                    calFin,
                    details,
                    elementsPriorite[1],
                    Integer.valueOf(elementsPriorite[0]),
                    Statut_.TRAITE);

            JOptionPane.showConfirmDialog(null,
                    configurationManager.getString("popup.texteAjouter"),
                    configurationManager.getString("popup.titreAjouter"),
                    JOptionPane.DEFAULT_OPTION);
        } else {
            JOptionPane.showConfirmDialog(null,
                    configurationManager.getString("popup.textePresent"),
                    configurationManager.getString("popup.titrePresent"),
                    JOptionPane.DEFAULT_OPTION);
        }
    }

    /**
     * Cette methode controle la saisie de tout les champs du formulaire
     * @return vrai si la saisie est correcte, faux sinon
     */
    private boolean controleSaisie() {

        // nettoyage labels des rubriques et des messages d'erreur de saisie
        erreurSaisieTextPane.setText(configurationManager.getString("erreur.saisie"));
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
        if (!Utils.controleSaisie(
                textFieldNom.getText(),
                Integer.valueOf(configurationManager.getString("tailleMax.nom")),
                REGEX_ALPHA_NUMERIQUE)) {

            // saisie incorrect, affichage du nom de la rubrique en rouge
            labelNom.setForeground(Color.RED);
            erreurSaisieTextPane.setText(erreurSaisieTextPane.getText() + "\n"
                    + configurationManager.getString("erreur.nom"));
            valide = false;
        }

        // controle de la rue
        if (!Utils.controleSaisie(
                textFieldRue.getText(),
                Integer.valueOf(configurationManager.getString("tailleMax.rue")),
                REGEX_ALPHA_NUMERIQUE)) {

            // saisie incorrect
            labelRue.setForeground(Color.RED);
            erreurSaisieTextPane.setText(erreurSaisieTextPane.getText() + "\n"
                    + configurationManager.getString("erreur.rue"));
            valide = false;
        }

        //controle numero rue
        if (!Utils.controleSaisie(
                textFieldNumRue.getText(),
                Integer.valueOf(configurationManager.getString("tailleMax.numeroRue")),
                REGEX_NUMERIQUE)) {

            labelNumRue.setForeground(Color.RED);
            erreurSaisieTextPane.setText(erreurSaisieTextPane.getText() + "\n"
                    + configurationManager.getString("erreur.numeroRue"));
            valide = false;
        }

        // controle latitude
        if (!Utils.controlSaisie(textFieldLatitude.getText(), REGEX_LATITUDE)) {
            labelLatitude.setForeground(Color.RED);
            erreurSaisieTextPane.setText(erreurSaisieTextPane.getText() + "\n"
                    + configurationManager.getString("erreur.latitude"));
            valide = false;
        }

        // controle longitude
        if (!Utils.controlSaisie(textFieldLongitude.getText(), REGEX_LONGITUDE)) {
            labelLongitude.setForeground(Color.RED);
            erreurSaisieTextPane.setText(erreurSaisieTextPane.getText() + "\n"
                    + configurationManager.getString("erreur.longitude"));
            valide = false;
        }

        // controle details
        if (!Utils.controlSaisie(
                textAreaDetails.getText(),
                Integer.valueOf(configurationManager.getString("tailleMax.details")))) {
            labelDetails.setForeground(Color.RED);
            erreurSaisieTextPane.setText(erreurSaisieTextPane.getText() + "\n"
                    + configurationManager.getString("erreur.details"));
            valide = false;
        }

        // controle date de debut
        if (!Utils.controlSaisie(textFieldDateDebut.getText(), REGEX_DATE)) {
            labelDateDebut.setForeground(Color.RED);
            erreurSaisieTextPane.setText(erreurSaisieTextPane.getText()  + "\n"
                    + configurationManager.getString("erreur.date"));
            valide = false;
        }

        // controle date de fin
        if (!controlSaisieDateFin(textFieldDateFin.getText(), REGEX_DATE)) {
            labelDateFin.setForeground(Color.RED);
            erreurSaisieTextPane.setText(erreurSaisieTextPane.getText() + "\n"
                    + configurationManager.getString("erreur.date"));
            valide = false;
        }

        if (valide) // saisie valide fermeture du panel de mauvaise saisie
        {
            erreurSaisieTextPane.setEnabled(false);
            scrollPaneErreurSaisie.setVisible(false);
            scrollPaneErreurSaisie.setEnabled(false);

        } else { // affichages des erreurs de saisie
            erreurSaisieTextPane.setEnabled(true);
            erreurSaisieTextPane.setVisible(true);
            scrollPaneErreurSaisie.setVisible(true);
            scrollPaneErreurSaisie.setEnabled(true);
        }

        return valide;
    }

    /**
     * Controle si la saisie de la date de fin est au format correct
     * et si la date est postérieur à la date
     * d'aujourd'hui et à la date de début de l'événement
     * @param texte date à contrer
     * @param regex format de la date
     * @return vrai si la date de fin est valide, faux sinon
     */
    private boolean controlSaisieDateFin(String texte, String regex) {

        // date de debut non valide, on ne peut pas tester la date de fin
        if (!Utils.controlSaisie(textFieldDateDebut.getText(), REGEX_DATE)) {
            return false;
        }

        if (texte.isEmpty() || !texte.matches(regex)) {
            return false;
        }

        // controle si la date de fin est postérieur à aujourd'hui et a la date de début
        Date dateAujourdhui = new Date();
        Date dateDebut = null;
        Date dateFin = null;
        try {
            dateDebut = dateFormat.parse(textFieldDateDebut.getText());
            dateFin = dateFormat.parse(textFieldDateFin.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateFin != null && dateDebut != null && !(dateFin.before(dateAujourdhui)
                || dateFin.before(dateDebut) || dateFin.equals(dateDebut));
    }

    /**
     * verrouille ou déverrouille les champs du formulaire
     * @param v false pour verrouiller les champs et true pour les déverrouiller
     */
    private void etatChamps(boolean v) {
        textFieldNom.setEditable(v);
        textFieldRue.setEditable(v);
        textFieldNumRue.setEditable(v);
        textFieldLatitude.setEditable(v);
        textFieldLongitude.setEditable(v);
        comboBoxNpa.setEditable(v);
        comboBoxPriorite.setEditable(v);
        textFieldDateDebut.setEditable(v);
        textFieldDateFin.setEditable(v);
        textAreaDetails.setEditable(v);

    }

    /**
     * Vide les champs du formulaire
     */
    private void videChamps() {
        textFieldNom.setText("");
        textFieldRue.setText("");
        textFieldNumRue.setText("");
        textFieldLatitude.setText("");
        textFieldLongitude.setText("");
        comboBoxNpa.setSelectedIndex(0);
        comboBoxPriorite.setSelectedIndex(0);
        textFieldDateDebut.setText(configurationManager.getString("date.format"));
        textFieldDateFin.setText(configurationManager.getString("date.format"));
        textAreaDetails.setText("");
    }

    /**
     * Retourne l'événement sélectionné dans la liste déroulante
     * @return événement sélectionné
     */
    private Evenement getEvenementSelectionne() {
        return evenementSelectionne;
    }

    /**
     * Recupère l'événement sélectionné dans la liste déroulante
     * @param evenementSelectionne événement sélectionné de la liste déroulante
     */
    private void setEvenementSelectionne(Evenement evenementSelectionne) {
        this.evenementSelectionne = evenementSelectionne;
    }
}
