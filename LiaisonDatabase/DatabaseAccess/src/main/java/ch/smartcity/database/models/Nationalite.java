package database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class Nationalite implements Serializable {

    private Integer idNationalite;
    private String nomNationalite;
    private Calendar derniereMiseAJour;
    private Set<Utilisateur> utilisateurSet;

    public Nationalite() {
    }

    public Nationalite(String nomNationalite) {
        this.nomNationalite = nomNationalite.toLowerCase();
    }

    public Integer getIdNationalite() {
        return idNationalite;
    }

    public void setIdNationalite(Integer idNationalite) {
        this.idNationalite = idNationalite;
    }

    public String getNomNationalite() {
        return nomNationalite;
    }

    public void setNomNationalite(String nomNationalite) {
        this.nomNationalite = nomNationalite.toLowerCase();
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public Set<Utilisateur> getUtilisateurSet() {
        return utilisateurSet;
    }

    public void setUtilisateurSet(Set<Utilisateur> utilisateurSet) {
        this.utilisateurSet = utilisateurSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Nationalite)) return false;

        Nationalite that = (Nationalite) o;

        return new EqualsBuilder()
                .append(idNationalite, that.idNationalite)
                .append(nomNationalite, that.nomNationalite)
                .append(derniereMiseAJour, that.derniereMiseAJour)
                .append(utilisateurSet, that.utilisateurSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idNationalite)
                .append(nomNationalite)
                .append(derniereMiseAJour)
                .append(utilisateurSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Nationalite{" +
                "idNationalite=" + idNationalite +
                ", nomNationalite='" + nomNationalite + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
