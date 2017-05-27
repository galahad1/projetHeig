package ch.smartcity.pdf;

import ch.smartcity.database.controllers.access.EvenementAccess;
import ch.smartcity.database.models.Evenement;
import ch.smartcity.pdf.graphiques.GenerateurGraphique;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe qui génère un PDF
 *
 * @author Luana Martelli
 */
public class GenerateurPDF {

    /**
     * Utilisé pour journaliser les actions effectuées
     */
    private static final Logger LOGGER = Logger.getLogger(GenerateurPDF.class.getName());

    private static final String LIEU = "Lausanne";
    private static URL LOGO;

    /**
     * Génère un PDF. Méthode appelée depuis l'extérieur
     *
     * @param nomEvenement nom de l'événement sélectionné
     * @param date         date de l'événement sélectionné
     * @throws Exception si il y a un problème avec la génération de PDF
     */
    public static void cree(String nomEvenement, Calendar date) throws Exception {
        String d = new SimpleDateFormat("dd-MM-yyyy").format(date.getTime());

        /* Le rapport sera stocké dans un dossier dans le répertoire utilisateur */
        String DEST = System.getProperty("user.home") + File.separator
                + "Documents" + File.separator
                + "Smartcity" + File.separator
                + "PDF" + File.separator
                + "rapport_" + nomEvenement + "_" + d + ".pdf";

        /* Recherche du logo */
        LOGO = GenerateurPDF.class.getClassLoader()
                .getResource("ch/smartcity/pdf/resources/logo.png");

        /* Création du PDF */
        try {
            new GenerateurPDF().createPdf(DEST, nomEvenement, date);
            LOGGER.info("Success of generation of PDF "
                    + DEST.substring(DEST.lastIndexOf(File.separator) + 1));
            Desktop.getDesktop().open(new File(DEST).getParentFile());
        } catch (Exception e) {
            LOGGER.severe("Error while creating PDF");
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Crée un document sous format PDF
     *
     * @param dest              chemin de destination du PDF
     * @param nomRubriqueEnfant nom de l'événement sélectionné
     * @param dateEvenement     date de l'événement sélectionné
     * @throws Exception si il y a un problème dans la génération du PDF
     */
    @SuppressWarnings("deprecation")
    private void createPdf(String dest, String nomRubriqueEnfant, Calendar dateEvenement)
            throws Exception {

        // Création de l'arborescence de dossier pour contenir les PDFs
        File file = new File(dest);
        file.getParentFile().mkdirs();
        file.createNewFile();

        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        PageSize pagesize = PageSize.A4;

        Document document = new Document(pdf, pagesize);

        /* Informations principales sur le PDF */
        document.setFont(PdfFontFactory.createFont(FontConstants.TIMES_ROMAN));
        document.setFontSize(12);
        document.setMargins(55, 75, 55, 75);
        document.setTextAlignment(TextAlignment.JUSTIFIED);

        /* Première partie du PDF */
        Table page1 = new Table(1);

        /* EN-TÈTE */
        /* Contient l'image et le nom du projet */
        Image logo = new Image(ImageDataFactory.create(LOGO));
        logo.scaleAbsolute(110, 15);

        Cell tete = new Cell().add("SmartCity").add(logo);
        tete.setBorder(null);
        tete.setItalic();
        tete.setFontColor(Color.GRAY);
        tete.setTextAlignment(TextAlignment.RIGHT);
        tete.setMarginBottom(25);
        page1.addHeaderCell(tete);

        /* DATE ET LIEU */
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        Cell lieuDate = new Cell().add(LIEU + ", le " + date.format(dateEvenement.getTime()));
        lieuDate.setBorder(null);
        lieuDate.setTextAlignment(TextAlignment.RIGHT);
        page1.addCell(lieuDate);

        /* Recherche de l'événment dans la base de données */
        List<Evenement> evenements = EvenementAccess.getInstance().get(nomRubriqueEnfant);

        /* Recherche des événements du jour */
        List<Evenement> evenementAujourdhui = new ArrayList<>();
        int statParMois[] = new int[12];
        for (Evenement e : evenements) {
            Calendar d = e.getDebut();
            if ((d.equals(dateEvenement) || d.before(dateEvenement))
                    && (e.getFin().equals(dateEvenement) || e.getFin().after(dateEvenement))) {
                evenementAujourdhui.add(e);
            }
            ++statParMois[d.getTime().getMonth()];
        }

        /* TITRE */
        Cell titre = new Cell().add("Avis de " + nomRubriqueEnfant);
        titre.setBorder(null);
        titre.setFont(PdfFontFactory.createFont(FontConstants.TIMES_BOLD));
        titre.setFontSize(20);
        titre.setMarginTop(25);
        titre.setMarginBottom(25);
        page1.addCell(titre);

        /* Cas ou la base de données est vide */
        if (evenementAujourdhui.size() == 0) {
            Cell erreur = new Cell().add("Aucunes données pour cet événement à cette date !");
            erreur.setBorder(null);
            page1.addCell(erreur);
        } else {
            /* Tous les événements du jours et leurs détails sont ajoutés au document */
            SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
            for (Evenement e : evenementAujourdhui) {

                /* INFORMATIONS_1 */
                Cell info1 = new Cell().add(e.getNomEvenement()
                        + "\nDu " + formatDate.format(e.getDebut().getTime()) +
                        " au " + formatDate.format(e.getFin().getTime()));
                info1.setBorder(null);
                info1.setFont(PdfFontFactory.createFont(FontConstants.TIMES_ITALIC));
                info1.setMarginBottom(25);
                page1.addCell(info1);

                /* TEXTE_1 */
                Cell texte1 = new Cell().add(e.getDetails());
                texte1.setBorder(null);
                texte1.setMarginBottom(25);
                page1.addCell(texte1);
            }
        }

        document.add(page1);

        /* Cas où il n'y a pas d'événements dans la base de données */
        if (evenementAujourdhui.size() == 0) {
            document.close();
            return;
        }

        document.add(new AreaBreak());

        /* Deuième partie du PDF */
        Table page2 = new Table(1);

        /* STATISTIQUE */
        Cell information = new Cell().add("Statistiques des " + nomRubriqueEnfant
                + " en ville de " + LIEU);
        information.setBorder(null);
        information.setFont(PdfFontFactory.createFont(FontConstants.TIMES_BOLD));

        new GenerateurGraphique(statParMois);
        Image image = new Image(ImageDataFactory.create(GenerateurGraphique.CHEMIN_IMAGE));
        //image.setAutoScale(true);
        image.scaleAbsolute(350, 250);
        image.setMargins(25, 55, 15, 55);
        information.add(image);
        page2.addCell(information);

        /* Recherche du nombre d'événements depuis le début de l'année */
        Calendar calStat = Calendar.getInstance();
        calStat.set(Calendar.DAY_OF_YEAR, 1);
        calStat.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));

        for (Evenement e : evenementAujourdhui) {
            if (e.getDebut().compareTo(calStat) == -1) {
                evenementAujourdhui.remove(e);
            }
        }

        int compteur;
        /* Statistique selon la rubrique choisie */
        switch (nomRubriqueEnfant) {
            case "accidents":
                /* Nb accidents par rues principales */
                String ruesPrincipales[] = {"flon", "malley", "ouchy", "beaulieu"};
                for (String ruesPrincipale : ruesPrincipales) {
                    compteur = 0;
                    for (Evenement e : evenementAujourdhui) {
                        if (e.getNomEvenement().contains(ruesPrincipale)) {
                            ++compteur;
                        }
                    }
                    Cell statsRues = new Cell().add("Nombre d'accidents à " + ruesPrincipale
                            + " : " + compteur + "\n");
                    statsRues.setBorder(null);
                    page2.addCell(statsRues);
                }
                break;
            case "travaux":
                compteur = 0;
                for (Evenement e : evenementAujourdhui) {
                    if (e.getFin().compareTo(dateEvenement) <= 0) {
                        ++compteur;
                    }
                }
                Cell statsTravaux = new Cell().add("Depuis le début de l'année, " + compteur
                        + " travaux sont terminés.\n");
                statsTravaux.setBorder(null);
                page2.addCell(statsTravaux);

                long moyenne = 0;
                for (Evenement e : evenementAujourdhui) {
                    moyenne += e.getFin().getTime().getTime() - e.getDebut().getTime().getTime();
                }

                moyenne /= evenementAujourdhui.size();

                long jours = moyenne / (24 * 60 * 60 * 1000);

                Cell statsDuree = new Cell().add("En moyenne, les travaux durent " + jours
                        + " jour" + (jours > 1 ? "s" : "") + ".\n");
                statsDuree.setBorder(null);
                page2.addCell(statsDuree);
                break;
            case "constructions":
            case "rénovations":
                moyenne = 0;
                for (Evenement e : evenementAujourdhui) {
                    moyenne += e.getFin().getTime().getTime() - e.getDebut().getTime().getTime();
                }

                moyenne /= evenementAujourdhui.size();

                jours = moyenne / (24 * 60 * 60 * 1000);

                Cell statsDuree2 = new Cell().add("En moyenne, les travaux durent " + jours
                        + " jour" + (jours > 1 ? "s" : "") + ".\n");
                statsDuree2.setBorder(null);
                page2.addCell(statsDuree2);
                break;
            case "manifestations":
                int nbCommentaires = 0;
                for (Evenement e : evenementAujourdhui) {
                    nbCommentaires += e.getCommentaireSet().size();
                }

                Cell statsComms = new Cell().add("Il y a eu " + nbCommentaires
                        + " commentaire" + (nbCommentaires > 1 ? "s" : "")
                        + " au sujet des manifestations d'aujourd'hui.\n");
                statsComms.setBorder(null);
                page2.addCell(statsComms);

                break;
        }

        document.add(page2);
        document.close();
    }
}