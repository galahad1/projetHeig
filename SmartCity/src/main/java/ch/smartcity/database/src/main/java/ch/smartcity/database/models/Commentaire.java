package ch.smartcity.database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Modélise un commentaire au sein de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class Commentaire implements Serializable {

    private IdCommentaire idCommentaire;
    private String commentaire;
    private Calendar creation;
    private Calendar derniereMiseAJour;

    public Commentaire() {
    }

    public Commentaire(IdCommentaire idCommentaire, String commentaire) {
        this.idCommentaire = idCommentaire;
        this.commentaire = commentaire.toLowerCase();
    }

    public IdCommentaire getIdCommentaire() {
        return idCommentaire;
    }

    public void setIdCommentaire(IdCommentaire idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    public Evenement getEvenement() {
        return idCommentaire.getEvenement();
    }

    public void setEvenement(Evenement evenement) {
        idCommentaire.setEvenement(evenement);
    }

    public Utilisateur getUtilisateur() {
        return idCommentaire.getUtilisateur();
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        idCommentaire.setUtilisateur(utilisateur);
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire.toLowerCase();
    }

    public Calendar getCreation() {
        return creation;
    }

    public void setCreation(Calendar creation) {
        this.creation = creation;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Commentaire)) return false;

        Commentaire that = (Commentaire) o;

        return new EqualsBuilder()
                .append(idCommentaire, that.idCommentaire)
                .append(commentaire, that.commentaire)
                .append(creation, that.creation)
                .append(derniereMiseAJour, that.derniereMiseAJour)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idCommentaire)
                .append(commentaire)
                .append(creation)
                .append(derniereMiseAJour)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "idCommentaire=" + idCommentaire +
                ", commentaire='" + commentaire + '\'' +
                ", creation=" + creation.getTime() +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
