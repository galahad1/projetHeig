package database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class TitreCivil implements Serializable {

    private int idTitreCivil;
    private String titre;
    private String abreviation;
    private Calendar derniereMiseAJour;
    private Set<Utilisateur> utilisateurSet;

    public TitreCivil() {
    }

    public TitreCivil(String titre, String abreviation) {
        this.titre = titre.toLowerCase();
        this.abreviation = abreviation.toLowerCase();
    }

    public int getIdTitreCivil() {
        return idTitreCivil;
    }

    public void setIdTitreCivil(int idTitreCivil) {
        this.idTitreCivil = idTitreCivil;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre.toLowerCase();
    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation.toLowerCase();
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

        if (!(o instanceof TitreCivil)) return false;

        TitreCivil that = (TitreCivil) o;

        return new EqualsBuilder()
                .append(idTitreCivil, that.idTitreCivil)
                .append(titre, that.titre)
                .append(abreviation, that.abreviation)
                .append(derniereMiseAJour, that.derniereMiseAJour)
                .append(utilisateurSet, that.utilisateurSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idTitreCivil)
                .append(titre)
                .append(abreviation)
                .append(derniereMiseAJour)
                .append(utilisateurSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "TitreCivil{" +
                "idTitreCivil=" + idTitreCivil +
                ", titre='" + titre + '\'' +
                ", abreviation='" + abreviation + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
