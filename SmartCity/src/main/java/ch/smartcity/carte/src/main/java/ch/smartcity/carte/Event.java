package ch.smartcity.carte;

import ch.smartcity.database.models.Adresse;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Modélise unévénement nommé et positionné dans l'espace
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class Event {
    private int id;
    private String priorite;
    private String nom;
    private Calendar debut;
    private Calendar fin;
    private Adresse adresse;
    private String description;
    private PointWGS84 position;
    private int categorie;

    /**
     * Construit un nouvel événement avec le nom et la getPosition donnés
     *
     * @param nom      Le nom de l'événement
     * @param position La getPosition de l'événement instance de la classe WGS84
     */
    public Event(int id, String priorite, String nom, Calendar debut, Calendar fin, Adresse adresse,
                 String description, PointWGS84 position, int categorie) {
        this.id = id;
        this.priorite = priorite;
        this.nom = nom;
        this.debut = debut;
        this.fin = fin;
        this.adresse = adresse;
        this.description = description;
        this.position = position;
        this.categorie = categorie;
    }


    public String getNom() {
        return nom;
    }

    public PointWGS84 getPosition() {
        return position;
    }

    public int getCategorie() {
        return categorie;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String debutEvents = sdf.format(debut.getTime());
        String finEvents = sdf.format(fin.getTime());

        return "-> " + id + ": " + nom + '\n' +
                "Priorité: " + priorite + "\n" +
                "Date: " + debutEvents + " - " + finEvents + "\n" +
                "NPA: " + adresse.getNpa().getNumeroNpa() + "\n" +
                "Adresse: " + adresse.getRue().getNomRue() + " " + adresse.getNumeroDeRue() + "\n" +
                "Description: " + description + "\n\n";
    }
}
