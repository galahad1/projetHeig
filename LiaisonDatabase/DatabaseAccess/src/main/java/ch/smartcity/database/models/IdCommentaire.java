package database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class IdCommentaire implements Serializable {

    private Evenement evenement;
    private Utilisateur utilisateur;

    public IdCommentaire() {
    }

    public IdCommentaire(Evenement evenement, Utilisateur utilisateur) {
        this.evenement = evenement;
        this.utilisateur = utilisateur;
    }

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof IdCommentaire)) return false;

        IdCommentaire that = (IdCommentaire) o;

        return new EqualsBuilder()
                .append(evenement, that.evenement)
                .append(utilisateur, that.utilisateur)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(evenement)
                .append(utilisateur)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "IdCommentaire{" +
                "evenement=" + evenement +
                '}';
    }
}
