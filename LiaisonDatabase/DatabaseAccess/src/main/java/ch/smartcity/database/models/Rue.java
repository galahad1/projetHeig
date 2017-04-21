package database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class Rue implements Serializable {

    private Integer idRue;
    private String nomRue;
    private Calendar derniereMiseAJour;
    private Set<Adresse> adresseSet;

    public Rue() {
    }

    public Rue(String nomRue) {
        this.nomRue = nomRue.toLowerCase();
    }

    public Integer getIdRue() {
        return idRue;
    }

    public void setIdRue(Integer idRue) {
        this.idRue = idRue;
    }

    public String getNomRue() {
        return nomRue;
    }

    public void setNomRue(String nomRue) {
        this.nomRue = nomRue.toLowerCase();
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public Set<Adresse> getAdresseSet() {
        return adresseSet;
    }

    public void setAdresseSet(Set<Adresse> adresseSet) {
        this.adresseSet = adresseSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Rue)) return false;

        Rue rue = (Rue) o;

        return new EqualsBuilder()
                .append(idRue, rue.idRue)
                .append(nomRue, rue.nomRue)
                .append(derniereMiseAJour, rue.derniereMiseAJour)
                .append(adresseSet, rue.adresseSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idRue)
                .append(nomRue)
                .append(derniereMiseAJour)
                .append(adresseSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Rue{" +
                "idRue=" + idRue +
                ", nomRue='" + nomRue + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
