package ch.smartcity.database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * Modélise une sexe au sein de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class Sexe implements Serializable {

    private Integer idSexe;
    private String nomSexe;
    private Calendar derniereMiseAJour;
    private Set<Utilisateur> utilisateurSet;

    public Sexe() {
    }

    public Sexe(String nomSexe) {
        this.nomSexe = nomSexe.toLowerCase();
    }

    public Integer getIdSexe() {
        return idSexe;
    }

    public void setIdSexe(Integer idSexe) {
        this.idSexe = idSexe;
    }

    public String getNomSexe() {
        return nomSexe;
    }

    public void setNomSexe(String nomSexe) {
        this.nomSexe = nomSexe.toLowerCase();
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

        if (!(o instanceof Sexe)) return false;

        Sexe sexe = (Sexe) o;

        return new EqualsBuilder()
                .append(idSexe, sexe.idSexe)
                .append(nomSexe, sexe.nomSexe)
                .append(derniereMiseAJour, sexe.derniereMiseAJour)
                .append(utilisateurSet, sexe.utilisateurSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idSexe)
                .append(nomSexe)
                .append(derniereMiseAJour)
                .append(utilisateurSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Sexe{" +
                "idSexe=" + idSexe +
                ", nomSexe='" + nomSexe + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
