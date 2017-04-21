package database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class RubriqueParent implements Serializable {

    private Integer idRubriqueParent;
    private String nomRubriqueParent;
    private Calendar derniereMiseAJour;
    private Set<RubriqueEnfant> rubriqueEnfantSet;

    public RubriqueParent() {
    }

    public RubriqueParent(String nomRubriqueParent) {
        this.nomRubriqueParent = nomRubriqueParent.toLowerCase();
    }

    public Integer getIdRubriqueParent() {
        return idRubriqueParent;
    }

    public void setIdRubriqueParent(Integer idRubriqueParent) {
        this.idRubriqueParent = idRubriqueParent;
    }

    public String getNomRubriqueParent() {
        return nomRubriqueParent;
    }

    public void setNomRubriqueParent(String nomRubriqueParent) {
        this.nomRubriqueParent = nomRubriqueParent.toLowerCase();
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public Set<RubriqueEnfant> getRubriqueEnfantSet() {
        return rubriqueEnfantSet;
    }

    public void setRubriqueEnfantSet(Set<RubriqueEnfant> rubriqueEnfantSet) {
        this.rubriqueEnfantSet = rubriqueEnfantSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof RubriqueParent)) return false;

        RubriqueParent that = (RubriqueParent) o;

        return new EqualsBuilder()
                .append(idRubriqueParent, that.idRubriqueParent)
                .append(nomRubriqueParent, that.nomRubriqueParent)
                .append(derniereMiseAJour, that.derniereMiseAJour)
                .append(rubriqueEnfantSet, that.rubriqueEnfantSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idRubriqueParent)
                .append(nomRubriqueParent)
                .append(derniereMiseAJour)
                .append(rubriqueEnfantSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "RubriqueParent{" +
                "idRubriqueParent=" + idRubriqueParent +
                ", nomRubriqueParent='" + nomRubriqueParent + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
