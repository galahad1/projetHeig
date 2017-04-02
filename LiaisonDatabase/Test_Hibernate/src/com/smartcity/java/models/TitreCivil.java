package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "TitreCivil")
public class TitreCivil {

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    Calendar derniereMiseAJour;
    @Id
    @Column(name = "idTitreCivil")
    private int idTitreCivil;
    @Column(name = "titre", length = 45)
    private String titre;
    @Column(name = "abreviation", length = 6)
    private String abreviation;

    public TitreCivil() {

    }

    public TitreCivil(int idTitreCivil, String titre, String abreviation, Calendar derniereMiseAjour) {
        this.idTitreCivil = idTitreCivil;
        this.titre = titre;
        this.abreviation = abreviation;
        this.derniereMiseAJour = derniereMiseAjour;
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
        this.titre = titre;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "TitreCivil{" +
                "idTitreCivil=" + idTitreCivil +
                ", titre='" + titre + '\'' +
                ", abreviation='" + abreviation + '\'' +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
