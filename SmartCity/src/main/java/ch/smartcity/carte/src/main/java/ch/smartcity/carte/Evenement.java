package ch.smartcity.carte;

/**
 * Modélise unévénement nommé et positionné dans l'espace
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class Evenement {
    private String nom;
    private PointWGS84 position;
    private int categorie;

    /**
     * Construit un nouvel événement avec le nom et la getPosition donnés
     *
     * @param nom     Le nom de l'événement
     * @param position La getPosition de l'événement instance de la classe WGS84
     */
    Evenement(String nom, PointWGS84 position, int categorie) {
        this.nom = nom;
        this.position = position;
        this.categorie = categorie;
    }

    /**
     * Retourne le nom de l'événement
     *
     * @return le nom de l'événement
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la position de l'événement
     *
     * @return la position de l'événement
     */
    public PointWGS84 getPosition() {
        return position;
    }

    /**
     * Retourne l'id de la catégorie
     *
     * @return l'id de la catégorie
     */
    public int getCategorie() {
        return categorie;
    }

}
