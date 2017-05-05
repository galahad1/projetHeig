/**
 * Journal de bord
 * 03 mars 2017 :
 * Import de la bibliothèque itext pour générer des PDF
 * Création de la classe PDF, premier test pour générer un PDF tout simple
 * FIXME : le chemin de destination du fichier : comment faire ?
 * <p>
 * 05 mars 2017 :
 * Déplacement des dépendances dans le dossier du projet
 * <p>
 * 07 mars 2017 :
 * Création d'une branche pdf sur git
 * Le PDF généré commence à ressembler à quelque chose
 * Ajout du texte PDF en dur dans le code
 * FIXME : nom du PDF généré automatiquement ?
 * FIXME : statistiques : image ? génération d'un jpeg et ajout dans le pdf ?
 * <p>
 * <p>
 * <p>
 * Traffic
 * Accidents      -> nb s'accidents
 * Travaux        -> durée des travaux selon un intervalle de temps ? en général ?
 * -> nb de travaux
 * -> lieu les plus touchés
 * Chantier
 * Constructions  -> nb de contructions selon un intervalle de temps
 * Rénovations    -> pareil que pour construction
 * Culture
 * Manifestations -> nb manifestations
 * -> nb participants ?
 * Doléances         -> nb commentaires selon les sujets
 * <p>
 * 27 mars 2017 :
 * <p>
 * insertion du graphique dans le pdf
 * ajout d'un random, soit piechart, soit en barre
 * écrire en bas du document ? rien trouvé à ce sujet
 * -> trop de contraintes, obligation de ne pas dépasser certaines tailles
 * -> recherche de mise en pages jolies ou juste potable
 * Voir avec Loan pour récup des infos de la base de donnée
 * <p>
 * Analyse vite fait :
 * génération d'un pdf et de graphes
 * utilisation de librairies existantes -> les plus connues ? iText et JFreeChart
 * FIXME: problème de taille avec le graphique en barres : tous les mois ne sont pas montrés
 * <p>
 * 31 mars 2017 :
 * <p>
 * réunion de groupe
 * <p>
 * 4 avril 2017 :
 * <p>
 * <p>
 * 25 avril :
 * Présentation intermédiaire
 */

package ch.smartcity.pdf;

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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GenerateurPDF {

    /* FIXME : Gestion du chemin !!!! */
    public static final String DEST;
    public static final URL LOGO;
    // private String nomRapport = getFromDataBaseName();
    private static String LIEU = "Lausanne";

    // !! TODO: LOAN : Vérifier le bon fonctionnement des modifications !!
    static {
        DEST = System.getProperty("user.home") + File.separator + "Documents" + File.separator
                + "Smartcity" + File.separator + "PDF" + File.separator + "test.pdf";

        try {
            LOGO = GenerateurPDF.class.getClassLoader()
                    .getResource("ch/smartcity/pdf/resources/logo.png");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    /* FIXME : Le main doit disparaitre */
    public static void main(String[] args) throws Exception {

        /* Creation of a PDF */
        try {
            new GenerateurPDF().createPdf(DEST);
        } catch (IOException e) {
            System.out.println("Error while creating PDF");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Database method. Create a new PDF
     *
     * @param dest name of the destination's file
     * @throws IOException
     */
    public void createPdf(String dest) throws Exception {
        File file = new File(dest);
        file.getParentFile().mkdirs();
        file.createNewFile();

        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        PageSize pagesize = PageSize.A4;

        int nb_pages = 0;

        Document document = new Document(pdf, pagesize);

        /* Informations principales sur le PDF */
        document.setFont(PdfFontFactory.createFont(FontConstants.TIMES_ROMAN));
        document.setFontSize(12);
        document.setMargins(55, 75, 55, 75);
        document.setTextAlignment(TextAlignment.JUSTIFIED);


        /* Première partie du PDF */
        Table page1 = new Table(1);

        /* EN-TÈTE */
        /* Contains logo and name of project */
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
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate now = LocalDate.now();
        Cell lieuDate = new Cell().add(LIEU + ", le " + date.format(now));
        lieuDate.setBorder(null);
        lieuDate.setTextAlignment(TextAlignment.RIGHT);
        page1.addCell(lieuDate);

        /* TITRE */
        Cell titre = new Cell().add("Avis de travaux");
        titre.setBorder(null);
        titre.setFont(PdfFontFactory.createFont(FontConstants.TIMES_BOLD));
        titre.setFontSize(20);
        titre.setMarginTop(25);
        titre.setMarginBottom(25);
        page1.addCell(titre);


        // FIXME : Boucle pour tous les evenements


        /* INFORMATIONS_1 */
        Cell info1 = new Cell().add("Travaux sur le Pont Chaudron\nDu 15.02.2017 au 15.02.2018");
        info1.setBorder(null);
        info1.setFont(PdfFontFactory.createFont(FontConstants.TIMES_ITALIC));
        info1.setMarginBottom(25);
        page1.addCell(info1);

        /* TEXTE_1 */
        Cell texte1 = new Cell().add("La commune de Lausanne annonce la mise au norme du Pont Chaudron. En effet," +
                "depuis son inauguration en 1959, le Pont n'a subit aucune rénovation majeur. La durée des travaux est" +
                "prévue pour un an. Pendant cette période, il ne sera plus possible de se rendre directement sur l'" +
                "avenue de Chaudron.");
        texte1.setBorder(null);
        texte1.setMarginBottom(25);
        page1.addCell(texte1);


        /* INFORMATIONS_2 */
        Cell info2 = new Cell().add("Travaux devant la Migros d'Ouchy\nDu 05.05.2017 au 17.09.2017");
        info2.setBorder(null);
        info2.setFont(PdfFontFactory.createFont(FontConstants.TIMES_ITALIC));
        info2.setMarginBottom(25);
        page1.addCell(info2);

        /* TEXTE_2 */
        Cell texte2 = new Cell().add("Des travaux auront lieu cet été devant la Migros d'Ouchy. Les rénovations " +
                "dureront 3 mois. Durant toute cette période, il ne sera malheureusement pas possible pour les promeneurs " +
                "d'aller s'acheter une collation dans leur magasin préféré.");
        texte2.setBorder(null);
        texte2.setMarginBottom(25);
        page1.addCell(texte2);

        /* PIED DE PAGE */
        ++nb_pages;
        Cell pied = new Cell().add(String.valueOf(nb_pages));
        pied.setBorder(null);
        pied.setTextAlignment(TextAlignment.RIGHT);
        pied.setWidth(pagesize.getWidth());
        //pied.setMarginTop(25);
        page1.addFooterCell(pied);


        // FIXME: gérer le nombre de pages !
        document.add(page1);
        document.add(new AreaBreak());


        /* Deuième partie du PDF */
        Table page2 = new Table(1);

        /* STATISTIQUE */
        // FIXME : changer travaux en variable
        Cell information = new Cell().add("Statistiques des travaux en ville de " + LIEU);
        information.setBorder(null);
        information.setFont(PdfFontFactory.createFont(FontConstants.TIMES_BOLD));

        GenerateurGraphique graphe = new GenerateurGraphique();
        Image image = new Image(ImageDataFactory.create(graphe.CHEMIN_IMAGE));
        //image.setAutoScale(true);
        image.scaleAbsolute(350, 250);
        image.setMargins(25, 55, 15, 55);
        information.add(image);
        page2.addCell(information);

        Cell stat = new Cell().add("Depuis le 1er janvier 2017, il a eu 12 chantiers à Lausanne.\n9 sont " +
                "terminés. En moyenne, un chantier dure 9 mois. Et je complète ce paragraphe par des mots inutiles " +
                "qui se suivent pour pouvoir voir en gros comment les choses seront présentées, mais ça ne restera " +
                "pas comme ça. Voilà.");
        stat.setBorder(null);
        page2.addCell(stat);

        document.add(page2);


        document.close();
    }
}