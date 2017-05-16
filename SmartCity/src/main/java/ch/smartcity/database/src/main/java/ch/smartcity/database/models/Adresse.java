package ch.smartcity.database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class Adresse implements Serializable {

    private Integer idAdresse;
    private Rue rue;
    private String numeroDeRue;
    private Npa npa;
    private Calendar derniereMiseAJour;
    private Set<Utilisateur> utilisateurSet;
    private Set<Evenement> evenementSet;

    public Adresse() {
    }

    public Adresse(Rue rue, String numeroDeRue, Npa npa) {
        this.rue = rue;
        this.numeroDeRue = numeroDeRue.toLowerCase();
        this.npa = npa;
    }

    public Integer getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(Integer idAdresse) {
        this.idAdresse = idAdresse;
    }

    public Rue getRue() {
        return rue;
    }

    public void setRue(Rue rue) {
        this.rue = rue;
    }

    public String getNumeroDeRue() {
        return numeroDeRue;
    }

    public void setNumeroDeRue(String numeroDeRue) {
        this.numeroDeRue = numeroDeRue.toLowerCase();
    }

    public Npa getNpa() {
        return npa;
    }

    public void setNpa(Npa npa) {
        this.npa = npa;
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

    public Set<Evenement> getEvenementSet() {
        return evenementSet;
    }

    public void setEvenementSet(Set<Evenement> evenementSet) {
        this.evenementSet = evenementSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Adresse)) return false;

        Adresse adresse = (Adresse) o;

        return new EqualsBuilder()
                .append(idAdresse, adresse.idAdresse)
                .append(rue, adresse.rue)
                .append(numeroDeRue, adresse.numeroDeRue)
                .append(npa, adresse.npa)
                .append(derniereMiseAJour, adresse.derniereMiseAJour)
                .append(utilisateurSet, adresse.utilisateurSet)
                .append(evenementSet, adresse.evenementSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idAdresse)
                .append(rue)
                .append(numeroDeRue)
                .append(npa)
                .append(derniereMiseAJour)
                .append(utilisateurSet)
                .append(evenementSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Adresse{" +
                "idAdresse=" + idAdresse +
                ", rue=" + rue +
                ", numeroDeRue='" + numeroDeRue + '\'' +
                ", npa=" + npa +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
