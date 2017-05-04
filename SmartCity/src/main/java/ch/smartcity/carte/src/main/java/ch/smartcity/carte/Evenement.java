package ch.smartcity.carte;

/**
 * Modélise unévénement nommé et positionné dans l'espace
 *
 * @author Wojciech Myskorowski
 * @author Jérémie Zanone
 */
public final class Evenement {
    private String name;
    private PointWGS84 position;
    private int categorie;

    /**
     * Construit un nouvel événement avec le nom et la position donnés
     *
     * @param name     Le nom de l'événement
     * @param position La position de l'événement instance de la classe WGS84
     */
    Evenement(String name, PointWGS84 position, int categorie) {
        this.name = name;
        this.position = position;
        this.categorie = categorie;
    }

    /**
     * Retourne le nom de l'événement
     *
     * @return le nom de l'événement
     */
    public String name() {
        return name;
    }

    /**
     * Retourne la position de l'événement
     *
     * @return la position de l'événement
     */
    public PointWGS84 position() {
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
