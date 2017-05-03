package ch.smartcity.database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;

public class Confiance implements Serializable {

    private IdConfiance idConfiance;
    private Calendar creation;

    public Confiance() {
    }

    public Confiance(IdConfiance idConfiance) {
        this.idConfiance = idConfiance;
    }

    public IdConfiance getIdConfiance() {
        return idConfiance;
    }

    public void setIdConfiance(IdConfiance idConfiance) {
        this.idConfiance = idConfiance;
    }

    public Utilisateur getUtilisateur() {
        return idConfiance.getUtilisateur();
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        idConfiance.setUtilisateur(utilisateur);
    }

    public RubriqueEnfant getRubriqueEnfant() {
        return idConfiance.getRubriqueEnfant();
    }

    public void setRubriqueEnfant(RubriqueEnfant rubriqueEnfant) {
        idConfiance.setRubriqueEnfant(rubriqueEnfant);
    }

    public Calendar getCreation() {
        return creation;
    }

    public void setCreation(Calendar creation) {
        this.creation = creation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Confiance)) return false;

        Confiance confiance = (Confiance) o;

        return new EqualsBuilder()
                .append(idConfiance, confiance.idConfiance)
                .append(creation, confiance.creation)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idConfiance)
                .append(creation)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Confiance{" +
                "idConfiance=" + idConfiance +
                ", creation=" + creation.getTime() +
                '}';
    }
}
