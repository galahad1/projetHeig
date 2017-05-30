package ch.smartcity.database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Modélise l'identifiant de la table confiance au sein de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class IdConfiance implements Serializable {

    private Utilisateur utilisateur;
    private RubriqueEnfant rubriqueEnfant;

    public IdConfiance() {
    }

    public IdConfiance(Utilisateur utilisateur, RubriqueEnfant rubriqueEnfant) {
        this.utilisateur = utilisateur;
        this.rubriqueEnfant = rubriqueEnfant;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public RubriqueEnfant getRubriqueEnfant() {
        return rubriqueEnfant;
    }

    public void setRubriqueEnfant(RubriqueEnfant rubriqueEnfant) {
        this.rubriqueEnfant = rubriqueEnfant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof IdConfiance)) return false;

        IdConfiance that = (IdConfiance) o;

        return new EqualsBuilder()
                .append(utilisateur, that.utilisateur)
                .append(rubriqueEnfant, that.rubriqueEnfant)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(utilisateur)
                .append(rubriqueEnfant)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "IdConfiance{" +
                "utilisateur=" + utilisateur +
                ", rubriqueEnfant=" + rubriqueEnfant +
                '}';
    }
}
