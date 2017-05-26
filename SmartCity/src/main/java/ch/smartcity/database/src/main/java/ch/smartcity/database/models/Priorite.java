package ch.smartcity.database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

/**
 * Modélise une priorité au sein de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
public class Priorite implements Serializable {

    private Integer idPriorite;
    private String nomPriorite;
    private int niveau;
    private Calendar derniereMiseAJour;
    private Set<Evenement> evenementSet;

    public Priorite() {
    }

    public Priorite(String nomPriorite, int niveau) {
        this.nomPriorite = nomPriorite.toLowerCase();
        this.niveau = niveau;
    }

    public Integer getIdPriorite() {
        return idPriorite;
    }

    public void setIdPriorite(Integer idPriorite) {
        this.idPriorite = idPriorite;
    }

    public String getNomPriorite() {
        return nomPriorite;
    }

    public void setNomPriorite(String nomPriorite) {
        this.nomPriorite = nomPriorite.toLowerCase();
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
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

        if (!(o instanceof Priorite)) return false;

        Priorite priorite = (Priorite) o;

        return new EqualsBuilder()
                .append(idPriorite, priorite.idPriorite)
                .append(niveau, priorite.niveau)
                .append(nomPriorite, priorite.nomPriorite)
                .append(derniereMiseAJour, priorite.derniereMiseAJour)
                .append(evenementSet, priorite.evenementSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idPriorite)
                .append(nomPriorite)
                .append(niveau)
                .append(derniereMiseAJour)
                .append(evenementSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return niveau + " - " + nomPriorite;

    }
}
