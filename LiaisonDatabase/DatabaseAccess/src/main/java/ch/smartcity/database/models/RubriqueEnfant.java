package database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class RubriqueEnfant implements Serializable {

    private Integer idRubriqueEnfant;
    private RubriqueParent rubriqueParent;
    private String nomRubriqueEnfant;
    private Calendar derniereMiseAJour;
    private Set<Evenement> evenementSet;
    private Set<Confiance> confianceSet;

    public RubriqueEnfant() {
    }

    public RubriqueEnfant(RubriqueParent rubriqueParent, String nomRubriqueEnfant) {
        this.rubriqueParent = rubriqueParent;
        this.nomRubriqueEnfant = nomRubriqueEnfant.toLowerCase();
    }

    public Integer getIdRubriqueEnfant() {
        return idRubriqueEnfant;
    }

    public void setIdRubriqueEnfant(Integer idRubriqueEnfant) {
        this.idRubriqueEnfant = idRubriqueEnfant;
    }

    public RubriqueParent getRubriqueParent() {
        return rubriqueParent;
    }

    public void setRubriqueParent(RubriqueParent rubriqueParent) {
        this.rubriqueParent = rubriqueParent;
    }

    public String getNomRubriqueEnfant() {
        return nomRubriqueEnfant;
    }

    public void setNomRubriqueEnfant(String nomRubriqueEnfant) {
        this.nomRubriqueEnfant = nomRubriqueEnfant.toLowerCase();
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public Set<Evenement> getEvenementSet() {
        return evenementSet;
    }

    public void setEvenementSet(Set<Evenement> evenementSet) {
        this.evenementSet = evenementSet;
    }

    public Set<Confiance> getConfianceSet() {
        return confianceSet;
    }

    public void setConfianceSet(Set<Confiance> confianceSet) {
        this.confianceSet = confianceSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof RubriqueEnfant)) return false;

        RubriqueEnfant that = (RubriqueEnfant) o;

        return new EqualsBuilder()
                .append(idRubriqueEnfant, that.idRubriqueEnfant)
                .append(rubriqueParent, that.rubriqueParent)
                .append(nomRubriqueEnfant, that.nomRubriqueEnfant)
                .append(derniereMiseAJour, that.derniereMiseAJour)
                .append(evenementSet, that.evenementSet)
                .append(confianceSet, that.confianceSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idRubriqueEnfant)
                .append(rubriqueParent)
                .append(nomRubriqueEnfant)
                .append(derniereMiseAJour)
                .append(evenementSet)
                .append(confianceSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "RubriqueEnfant{" +
                "idRubriqueEnfant=" + idRubriqueEnfant +
                ", rubriqueParent=" + rubriqueParent +
                ", nomRubriqueEnfant='" + nomRubriqueEnfant + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
