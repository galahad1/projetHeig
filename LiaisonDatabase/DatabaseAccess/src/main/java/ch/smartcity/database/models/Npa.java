package database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class Npa implements Serializable {

    private Integer idNpa;
    private String numeroNpa;
    private Calendar derniereMiseAJour;
    private Set<Adresse> adressesSet;

    public Npa() {
    }

    public Npa(String numeroNpa) {
        this.numeroNpa = numeroNpa.toLowerCase();
    }

    public Integer getIdNpa() {
        return idNpa;
    }

    public void setIdNpa(Integer idNpa) {
        this.idNpa = idNpa;
    }

    public String getNumeroNpa() {
        return numeroNpa;
    }

    public void setNumeroNpa(String numeroNpa) {
        this.numeroNpa = numeroNpa.toLowerCase();
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public Set<Adresse> getAdressesSet() {
        return adressesSet;
    }

    public void setAdressesSet(Set<Adresse> adressesSet) {
        this.adressesSet = adressesSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Npa)) return false;

        Npa npa = (Npa) o;

        return new EqualsBuilder()
                .append(idNpa, npa.idNpa)
                .append(numeroNpa, npa.numeroNpa)
                .append(derniereMiseAJour, npa.derniereMiseAJour)
                .append(adressesSet, npa.adressesSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idNpa)
                .append(numeroNpa)
                .append(derniereMiseAJour)
                .append(adressesSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Npa{" +
                "idNpa=" + idNpa +
                ", numeroNpa='" + numeroNpa + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
