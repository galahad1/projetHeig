package database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class Statut implements Serializable {

    private int idStatut;
    private String nomStatut;
    private Calendar derniereMiseAJour;
    private Set<Evenement> evenementSet;

    public Statut() {
    }

    public Statut(String nomStatut) {
        this.nomStatut = nomStatut.toLowerCase();
    }

    public int getIdStatut() {
        return idStatut;
    }

    public void setIdStatut(int idStatut) {
        this.idStatut = idStatut;
    }

    public String getNomStatut() {
        return nomStatut;
    }

    public void setNomStatut(String nomStatut) {
        this.nomStatut = nomStatut.toLowerCase();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Statut)) return false;

        Statut statut = (Statut) o;

        return new EqualsBuilder()
                .append(idStatut, statut.idStatut)
                .append(nomStatut, statut.nomStatut)
                .append(derniereMiseAJour, statut.derniereMiseAJour)
                .append(evenementSet, statut.evenementSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idStatut)
                .append(nomStatut)
                .append(derniereMiseAJour)
                .append(evenementSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Statut{" +
                "idStatut=" + idStatut +
                ", nomStatut='" + nomStatut + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
