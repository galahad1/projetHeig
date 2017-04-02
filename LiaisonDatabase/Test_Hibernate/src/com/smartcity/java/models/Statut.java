package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "Statut")
public class Statut {

    @Id
    @Column(name = "idStatut")
    private int idStatut;

    @Column(name = "nomStatut", length = 45)
    private String nomStatut;

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar derniereMiseAJour;

    public Statut() {

    }

    public Statut(int idStatut, String nomStatut, Calendar derniereMiseAJour) {
        this.idStatut = idStatut;
        this.nomStatut = nomStatut;
        this.derniereMiseAJour = derniereMiseAJour;
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
        this.nomStatut = nomStatut;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "Statut{" +
                "idStatut=" + idStatut +
                ", nomStatut='" + nomStatut + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
