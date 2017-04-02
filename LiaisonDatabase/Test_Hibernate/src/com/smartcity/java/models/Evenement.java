package models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Lassalle Loan
 * @since 8/03/2017
 */

@Entity
@Table(name = "Evenement")
public class Evenement {

    @Id
    @Column(name = "idEvenement")
    private int idEvenement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRubriqueEnfant")
    private int idRubriqueEnfant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUtilisateur")
    private int idUtilisateur;

    @Column(name = "nomEvenement", length = 90)
    private String nomEvenement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAdresse")
    private int idAdresse;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "debut", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar debut;

    @Column(name = "fin", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fin;
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPriorite")
    private int idPriorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idStatut")
    private int idStatut;

    @Column(name = "creation", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creation;

    @Column(name = "derniereMiseAJour", length = 40)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar derniereMiseAJour;

    public Evenement() {
    }

    public Evenement(int idEvenement, int idRubriqueEnfant, int idUtilisateur, String nomEvenement, int idAdresse,
                     double latitude, double longitude, Calendar debut, Calendar fin, String details, int idPriorite,
                     int idStatut, Calendar creation, Calendar derniereMiseAJour) {
        this.idEvenement = idEvenement;
        this.idRubriqueEnfant = idRubriqueEnfant;
        this.idUtilisateur = idUtilisateur;
        this.nomEvenement = nomEvenement;
        this.idAdresse = idAdresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.debut = debut;
        this.fin = fin;
        this.details = details;
        this.idPriorite = idPriorite;
        this.idStatut = idStatut;
        this.creation = creation;
        this.derniereMiseAJour = derniereMiseAJour;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public int getIdRubriqueEnfant() {
        return idRubriqueEnfant;
    }

    public void setIdRubriqueEnfant(int idRubriqueEnfant) {
        this.idRubriqueEnfant = idRubriqueEnfant;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNomEvenement() {
        return nomEvenement;
    }

    public void setNomEvenement(String nomEvenement) {
        this.nomEvenement = nomEvenement;
    }

    public int getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(int idAdresse) {
        this.idAdresse = idAdresse;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Calendar getDebut() {
        return debut;
    }

    public void setDebut(Calendar debut) {
        this.debut = debut;
    }

    public Calendar getFin() {
        return fin;
    }

    public void setFin(Calendar fin) {
        this.fin = fin;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getIdPriorite() {
        return idPriorite;
    }

    public void setIdPriorite(int idPriorite) {
        this.idPriorite = idPriorite;
    }

    public int getIdStatut() {
        return idStatut;
    }

    public void setIdStatut(int idStatut) {
        this.idStatut = idStatut;
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
        return "Evenement{" +
                "idEvenement=" + idEvenement +
                ", idRubriqueEnfant=" + idRubriqueEnfant +
                ", idUtilisateur=" + idUtilisateur +
                ", nomEvenement='" + nomEvenement + '\'' +
                ", idAdresse=" + idAdresse +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", debut=" + debut +
                ", fin=" + fin +
                ", details='" + details + '\'' +
                ", idPriorite=" + idPriorite +
                ", idStatut=" + idStatut +
                ", creation=" + creation +
                ", derniereMiseAJour=" + derniereMiseAJour +
                '}';
    }
}
