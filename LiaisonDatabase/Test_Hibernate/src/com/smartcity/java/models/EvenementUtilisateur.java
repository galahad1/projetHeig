package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "EvenementUtilisateur")
public class EvenementUtilisateur {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUtilisateur")
    private int idUtilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEvenement")
    private int idEvenement;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "creation", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creation;

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar derniereMiseAJour;

    public EvenementUtilisateur() {
    }

    public EvenementUtilisateur(int idUtilisateur, int idEvenement, String commentaire, Calendar creation,
                                Calendar derniereMiseAJour) {
        this.idUtilisateur = idUtilisateur;
        this.idEvenement = idEvenement;
        this.commentaire = commentaire;
        this.creation = creation;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Calendar getCreation() {
        return creation;
    }

    public void setCreation(Calendar creation) {
        this.creation = creation;
    }

    public Calendar getDerniereMiseAJour() {
        return derniereMiseAJour;
    }

    public void setDerniereMiseAJour(Calendar derniereMiseAJour) {
        this.derniereMiseAJour = derniereMiseAJour;
    }

    @Override
    public String toString() {
        return "EvenementUtilisateur{" +
                "idUtilisateur=" + idUtilisateur +
                ", idEvenement=" + idEvenement +
                ", commentaire='" + commentaire + '\'' +
                ", creation=" + creation +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
