package ch.smartcity.graphique;

import ch.smartcity.database.controllers.DatabaseAccess;
import ch.smartcity.database.controllers.access.*;
import ch.smartcity.database.models.*;
import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private static final String REGEX_ALPHA_NUMERIQUE = "[a-zA-ZÀ-ÿ0-9 \\-']*";
    private static final String REGEX_NUMERIQUE = "[0-9]*";

    // résumé ragex latitude : optionnel signe '+' ou '-' puis nombre entre 0 et 90 compris, avec max 6 décimales. '0' superflus interdits.
    // (optionnel +/-), puis 90{optionnel .00000} OU [ (de 0 à 9) ou (de 10 à 89)] {et optionnel .dddddd}
    private static final String REGEX_LATITUDE = "^[\\+-]?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$";
    // pour la longitude, comme latitude mais entre 0 et 180 compris
    private static final String REGEX_LONGITUDE = "^[\\+-]?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$";
    private static final String REGEX_DATE = "^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$";

    // messages d'erreurs
    private static final String MESSAGE_ERREUR_NOM = "- Nom doit être constitué de lettre et de chiffres et avoir une taille maximum de " + TAILLE_MAX_NOM + " caractères\n";
    private static final String MESSAGE_ERREUR_RUE = "- Rue doit être constitué de lettre et de chiffres et avoir une taille maximum de " + TAILLE_MAX_RUE + " caractères\n";
    private static final String MESSAGE_ERREUR_NUMERO_RUE = "- N°Rue doit être constitué de chiffres uniquement et avoir une taille maximum de " + TAILLE_MAX_NUMERO_RUE + " caractères\n";
    private static final String MESSAGE_ERREUR_LATITUDE = "- Latitude doit avoir le format degrés signés et au plus 6 décimales\n";
    private static final String MESSAGE_ERREUR_LONGITUDE = "- Longitude doit avoir le format degrés signés et au plus 6 décimales\n";
    private static final String MESSAGE_ERREUR_DETAILS = "- Détails doivent avoir une taille maxmimum de: " + TAILLE_MAX_DETAILS + " caractères\n";
    private static final String MESSAGE_ERREUR_DATE = "- La date doit être du format jj/mm/aaaa et la date de fin doit être postérieur à la date de début et à la date d'aujourd'hui\n";


    private static final String ENTETE_ERREUR_SAISIE = "Erreur(s) lors de la saisie:\nTout les champs doivent êtres remplis\n";
    public JFrame fenetre;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    JComboBox<String> comboBoxEvenements;
    JComboBox<String> comboBoxPriorite;
    List<Evenement> evenementList;
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
    private JComboBox<String> comboBoxRubrique;
    private Evenement evenementSelectionne = null;
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

        // fermeture de la fenetre lors de l'appuis sur la croix
        fenetre.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        fenetre.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //TODO rafraichireCarte();, voir pour appeler methode de la fenetre principale
                fenetre.dispose();
            }
        });

        JPanel panelModification = new JPanel();
        panelModification.setForeground(Color.LIGHT_GRAY);
        fenetre.getContentPane().add(panelModification, BorderLayout.CENTER);
        panelModification.setLayout(null);

        comboBoxEvenements = new JComboBox<>();
        comboBoxEvenements.setMaximumRowCount(16);

        comboBoxEvenements.setBounds(61, 46, 701, 41);

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

        comboBoxNpa = new JComboBox<>();
        List<Npa> listNpa = DatabaseAccess.get(Npa.class);

        String[] npas = new String[listNpa.size()];
        for(int i = 0; i < npas.length ; i++) {
            npas[i] = listNpa.get(i).toString();
        }

        comboBoxNpa.setModel(new DefaultComboBoxModel<String>(npas));
        comboBoxNpa.setBounds(539, 12, 183, 37);
        panelAjoutEvenement.add(comboBoxNpa);

        JLabel labelNpa = new JLabel("NPA");
        labelNpa.setBounds(442, 23, 70, 15);
        panelAjoutEvenement.add(labelNpa);

        comboBoxPriorite = new JComboBox<String>();

        List<Priorite> listPriorite = DatabaseAccess.get(Priorite.class);
        String[] priorites = new String[listPriorite.size()];
        for(int i = 0; i < priorites.length; i++)
        {
            priorites[i] = listPriorite.get(i).toString();
        }


        comboBoxPriorite.setModel(new DefaultComboBoxModel<String>(priorites));
        comboBoxPriorite.setBounds(539, 79, 183, 37);
        panelAjoutEvenement.add(comboBoxPriorite);

        comboBoxRubrique = new JComboBox<String>();
        List<RubriqueEnfant> listRubriqueEnfant = DatabaseAccess.get(RubriqueEnfant.class);

        String[] rubriques = new String[listRubriqueEnfant.size()];
        for(int i = 0; i < rubriques.length ; i++)
        {
            rubriques[i] = listRubriqueEnfant.get(i).toString();
        }
        comboBoxRubrique.setModel(new DefaultComboBoxModel<String>(rubriques));

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

                    if(comboBoxEvenements.getSelectedIndex() == 0) // nouvel evenement
                    {
                        ajouterEvenement();
                    } else // evenement deja exsistant
                    {
                        modifierEvenement();
                    }
                    chargementListeEvenements(context); // mise a jour de la liste
                    videChamps();

                }
            }
        });
        boutonValider.setBounds(59, 412, 117, 25);
        panelAjoutEvenement.add(boutonValider);


        JButton boutonRefuser = new JButton("Refuser");
        boutonRefuser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(comboBoxEvenements.getSelectedIndex() != 0)
                {
                    refuserEvenement(); // statut en refuser et change date de fin pour etre en etat supprimmer
                    chargementListeEvenements(context); // mise a jour de la liste
                    videChamps();
                }

            }
        });
        boutonRefuser.setBounds(59, 470, 117, 25);
        panelAjoutEvenement.add(boutonRefuser);

        JButton boutonSupprimer = new JButton("Supprimer");
        boutonSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(evenementSelectionne != null && comboBoxEvenements.getSelectedIndex() != 0)
                {
                    DatabaseAccess.delete(evenementSelectionne); // supprime de la base de donnée
                    chargementListeEvenements(context); // mise a jour de la liste
                    videChamps();

                    int confirmed = JOptionPane.showConfirmDialog(null,
                            "Evenement supprimé avec succès", "Evénement supprimé",
                            JOptionPane.DEFAULT_OPTION);
                }

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

        // init liste des evenements
        chargementListeEvenements(context);

        // lorsque l'on choisi un evenement dans la liste, on remplis les chapms
        comboBoxEvenements.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {


                int index = comboBoxEvenements.getSelectedIndex();

                // TODO : constante context
                if (index != 0) // un evenement de la base de donnée
                {

                    if (context == Constantes.CONTEXTE_EN_ATTENTE)
                    {
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

                } else if (context == Constantes.CONTEXTE_EN_ATTENTE && index == 0) // TODO: constantes
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


        JButton btnFermer = new JButton("Fermer");
        // ferme la fenetre lorsque le bouton est appuie
        btnFermer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //TODO rafraichireCarte();
                fenetre.dispose();
            }
        });
        btnFermer.setBounds(12, 12, 117, 25);
        panelModification.add(btnFermer);


        if (context == Constantes.CONTEXTE_AJOUTER)
        {
            fenetre.setTitle(TITRE_MODIFICATION);
            boutonRefuser.setVisible(false);
            boutonRefuser.setEnabled(false);
        } else if (context == Constantes.CONTEXTE_EN_ATTENTE) //TODO constantes
        {
            fenetre.setTitle(TITRE_EN_ATTENTE);
            boutonSupprimer.setVisible(false);
            boutonSupprimer.setEnabled(false);
        } else {
            //TODO Leve une exeption
        }


    } // fin initialize

    /**
     * charge les evenements dans la liste ainsi que les preview
     * @param context
     */
    private void chargementListeEvenements(int context) {

        List<String> previews;

        if (context == 0) // ajout/modif TODO: constantes
        {
            //fixme il faut retourner les evements traités dont la date de fin est egual ou supérieur a la date d'aujourd hui
            evenementList = EvenementAccess.getActif();

            //TODO trier la liste par rapport au IDs

            previews = Utils.previewEvenement(evenementList); // previsualisation des evenements


            previews.add(0, "Ajouter un événement");
        } else // en attente
        {

            evenementList = EvenementAccess.getEnAttente(); // recupere tout les evenements en attente

            //TODO trier la liste par rapport aux ids

            previews = Utils.previewEvenement(evenementList); // previsualisation des evenements

            previews.add(0, "Selectionner");
            etatChamps(false);

        }

        comboBoxEvenements.setModel(new DefaultComboBoxModel<>(previews.toArray(new String[0])));

    }

    /**
     * Refuse l'evenement et supprimer l'evenement
     * //TODO differencier en attente et actif
     * //TODO tester
     */
    private void refuserEvenement() {

        evenementSelectionne.setStatut( StatutAccess.get(Statut_.REFUSE).get(0)); // statur refuser

        EvenementAccess.update(evenementSelectionne.getIdEvenement(),null,null,null,null,null,null,null,null,null,null, evenementSelectionne.getStatut()); // met a jour l evenemnt avec le statut refuse
        DatabaseAccess.delete(evenementSelectionne); // change date de fin

    }

    private void modifierEvenement() {

        //TODO modifier evenement de la base de données


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

        // voir tout les champs qui ont ete modifier
        Evenement evenementBase = getEvenementSelectionne();

        Npa newNpa;
        List<Npa> listNewNpa = NpaAccess.get(npa);
        if(listNewNpa == null || listNewNpa.isEmpty())
        {
            newNpa = new Npa(npa); // créer
        }
        else
        {
            newNpa = evenementBase.getAdresse().getNpa(); // prend celui existant dans la DB
        }


        Rue newRue;
        List<Rue> listNewRue = RueAccess.get(nomRue);
        if(listNewRue == null || listNewRue.isEmpty())
        {
            newRue = new Rue(nomRue);
        }
        else
        {
            newRue = evenementBase.getAdresse().getRue();
        }


        Adresse newAdresse;
        List<Adresse> listNewAdresse = AdresseAccess.get(newRue,numeroRue,newNpa);
        if(listNewAdresse == null || listNewAdresse.isEmpty())
        {
            newAdresse = new Adresse(newRue,numeroRue,newNpa);
        }
        else
        {
            newAdresse = evenementBase.getAdresse();
        }


        RubriqueEnfant newRubrique = RubriqueEnfantAccess.get("", nomEnfant).get(0);
        Statut newStatut = StatutAccess.get(Statut_.TRAITE).get(0);
        String[] elementsPriorite = comboBoxPriorite.getSelectedItem().toString().split(" - "); // separe niveau et nom de la priorité
        Priorite newPriorite = PrioriteAccess.get(elementsPriorite[1], Integer.valueOf(elementsPriorite[0])).get(0);


        try {
            calDebut.setTime(dateFormat.parse(textFieldDateDebut.getText()));
            calFin.setTime(dateFormat.parse(textFieldDateFin.getText()));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }


        EvenementAccess.update(evenementBase.getIdEvenement(),newRubrique,null,nomEvenement,newAdresse,latitude,longitude,calDebut,calFin,details,newPriorite,newStatut);


        int confirmed = JOptionPane.showConfirmDialog(null,
                "Evenement modifié avec succès", "Evénement modifié",
                JOptionPane.DEFAULT_OPTION);

    }

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
        String[] elementsPriorite = comboBoxPriorite.getSelectedItem().toString().split(" - "); // separe niveau et nom de la priorité

        // controle si l evenement exsite deja

        List<Evenement> evenementsExsistants = EvenementAccess.get(nomEnfant,null,nomEvenement,nomRue,numeroRue,npa,latitude,longitude,calDebut,calFin,details,elementsPriorite[1],null,null);
        if(evenementsExsistants == null || evenementsExsistants.isEmpty()) // n'exsiste pas
        {

            EvenementAccess.save(nomEnfant, 1, nomEvenement, nomRue, numeroRue, npa, latitude, longitude, calDebut, calFin, details, elementsPriorite[1], Integer.valueOf(elementsPriorite[0]), Statut_.EN_ATTENTE);
            System.out.println("evenement ajouté");
            JOptionPane.showConfirmDialog(null,
                    "Evenement ajouté avec succès", "Evénement ajouté",
                    JOptionPane.DEFAULT_OPTION);
        }
        else
        {
            JOptionPane.showConfirmDialog(null,
                    "Evenement déjà dans la base de donnée", "Evénement présent",
                    JOptionPane.DEFAULT_OPTION);
        }

    }

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


        if (valide) // saisie valide fermeture du panel de mauvaise saisie
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


        Date dateAujourdhui = new Date();
        Date dateDebut = null;
        Date dateFin = null;
        try {
            dateDebut = dateFormat.parse(textFieldDateDebut.getText());
            dateFin = dateFormat.parse(textFieldDateFin.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dateFin.before(dateAujourdhui) || dateFin.before(dateDebut) || dateFin.equals(dateDebut)) {
            return false;
        }
        return true;


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

    public Evenement getEvenementSelectionne() {
        return evenementSelectionne;
    }

    public void setEvenementSelectionne(Evenement evenementSelectionne) {
        this.evenementSelectionne = evenementSelectionne;
    }
}
