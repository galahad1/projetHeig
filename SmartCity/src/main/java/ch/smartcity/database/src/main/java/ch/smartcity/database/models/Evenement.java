package ch.smartcity.database.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class Evenement implements Serializable {

    private Integer idEvenement;
    private RubriqueEnfant rubriqueEnfant;
    private Utilisateur utilisateur;
    private String nomEvenement;
    private Adresse adresse;
    private Double latitude;
    private Double longitude;
    private Calendar debut;
    private Calendar fin;
    private String details;
    private Priorite priorite;
    private Statut statut;
    private Calendar creation;
    private Calendar derniereMiseAJour;
    private Set<Commentaire> commentaireSet;

    public Evenement() {
    }

    public Evenement(RubriqueEnfant rubriqueEnfant,
                     Utilisateur utilisateur,
                     String nomEvenement,
                     Calendar debut,
                     Priorite priorite,
                     Statut statut) {
        this(rubriqueEnfant,
                utilisateur,
                nomEvenement,
                null,
                null,
                null,
                debut,
                null,
                null,
                priorite,
                statut);
    }

    public Evenement(RubriqueEnfant rubriqueEnfant,
                     Utilisateur utilisateur,
                     String nomEvenement,
                     Adresse adresse,
                     Double latitude,
                     Double longitude,
                     Calendar debut,
                     Calendar fin,
                     String details,
                     Priorite priorite,
                     Statut statut) {
        this.rubriqueEnfant = rubriqueEnfant;
        this.utilisateur = utilisateur;
        this.nomEvenement = nomEvenement.toLowerCase();
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        setMinimumTime(debut);
        this.debut = debut;
        setMinimumTime(fin);
        this.fin = fin;
        this.details = details.toLowerCase();
        this.priorite = priorite;
        this.statut = statut;
    }

    private static void setMinimumTime(Calendar calendar) {
        if (calendar != null) {
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
            calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        }
    }

    public Integer getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(Integer idEvenement) {
        this.idEvenement = idEvenement;
    }

    public RubriqueEnfant getRubriqueEnfant() {
        return rubriqueEnfant;
    }

    public void setRubriqueEnfant(RubriqueEnfant rubriqueEnfant) {
        this.rubriqueEnfant = rubriqueEnfant;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getNomEvenement() {
        return nomEvenement;
    }

    public void setNomEvenement(String nomEvenement) {
        this.nomEvenement = nomEvenement.toLowerCase();
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Calendar getDebut() {
        return debut;
    }

    public void setDebut(Calendar debut) {
        setMinimumTime(debut);
        this.debut = debut;
    }

    public Calendar getFin() {
        return fin;
    }

    public void setFin(Calendar fin) {
        setMinimumTime(fin);
        this.fin = fin;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details.toLowerCase();
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public void setPriorite(Priorite priorite) {
        this.priorite = priorite;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
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

    public Set<Commentaire> getCommentaireSet() {
        return commentaireSet;
    }

    public void setCommentaireSet(Set<Commentaire> commentaireSet) {
        this.commentaireSet = commentaireSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Evenement)) return false;

        Evenement evenement = (Evenement) o;

        return new EqualsBuilder()
                .append(idEvenement, evenement.idEvenement)
                .append(latitude, evenement.latitude)
                .append(longitude, evenement.longitude)
                .append(rubriqueEnfant, evenement.rubriqueEnfant)
                .append(utilisateur, evenement.utilisateur)
                .append(nomEvenement, evenement.nomEvenement)
                .append(adresse, evenement.adresse)
                .append(debut, evenement.debut)
                .append(fin, evenement.fin)
                .append(details, evenement.details)
                .append(priorite, evenement.priorite)
                .append(statut, evenement.statut)
                .append(creation, evenement.creation)
                .append(derniereMiseAJour, evenement.derniereMiseAJour)
                .append(commentaireSet, evenement.commentaireSet)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(idEvenement)
                .append(rubriqueEnfant)
                .append(utilisateur)
                .append(nomEvenement)
                .append(adresse)
                .append(latitude)
                .append(longitude)
                .append(debut)
                .append(fin)
                .append(details)
                .append(priorite)
                .append(statut)
                .append(creation)
                .append(derniereMiseAJour)
                .append(commentaireSet)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "idEvenement=" + idEvenement +
                ", rubriqueEnfant=" + rubriqueEnfant +
                ", utilisateur=" + utilisateur +
                ", nomEvenement='" + nomEvenement + '\'' +
                ", adresse=" + adresse +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", debut=" + debut.getTime() +
                ", fin=" + (fin != null ? fin.getTime() : "null") +
                ", details='" + details + '\'' +
                ", priorite=" + priorite +
                ", statut=" + statut +
                ", creation=" + creation.getTime() +
                ", derniereMiseAJour=" + derniereMiseAJour.getTime() +
                '}';
    }
}
