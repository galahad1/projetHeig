package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "Priorite")
public class Priorite {

    @Id
    @Column(name = "idPriorite")
    private int idPriorite;

    @Column(name = "nomPriorite", length = 45)
    private String nomPriorite;

    @Column(name = "niveau")
    private int niveau;

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    Calendar derniereMiseAJour;

    public Priorite() {
    }

    public Priorite(int idPriorite, String nomPriorite, int niveau, Calendar derniereMiseAJour) {
        this.idPriorite = idPriorite;
        this.nomPriorite = nomPriorite;
        this.niveau = niveau;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdPriorite() {
        return idPriorite;
    }

    public void setIdPriorite(int idPriorite) {
        this.idPriorite = idPriorite;
    }

    public String getNomPriorite() {
        return nomPriorite;
    }

    public void setNomPriorite(String nomPriorite) {
        this.nomPriorite = nomPriorite;
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

    @Override
    public String toString() {
        return "Priorite{" +
                "idPriorite=" + idPriorite +
                ", nomPriorite='" + nomPriorite + '\'' +
                ", niveau=" + niveau +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
