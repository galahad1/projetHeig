package database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Evenement.class)
public class Evenement_ {

    public static volatile SingularAttribute<Evenement, Integer> idEvenement;
    public static volatile SingularAttribute<Evenement, RubriqueEnfant> rubriqueEnfant;
    public static volatile SingularAttribute<Evenement, Utilisateur> utilisateur;
    public static volatile SingularAttribute<Evenement, String> nomEvenement;
    public static volatile SingularAttribute<Evenement, Adresse> adresse;
    public static volatile SingularAttribute<Evenement, Double> latitude;
    public static volatile SingularAttribute<Evenement, Double> longitude;
    public static volatile SingularAttribute<Evenement, Calendar> debut;
    public static volatile SingularAttribute<Evenement, Calendar> fin;
    public static volatile SingularAttribute<Evenement, String> details;
    public static volatile SingularAttribute<Evenement, Priorite> priorite;
    public static volatile SingularAttribute<Evenement, Statut> statut;
    public static volatile SingularAttribute<Evenement, Calendar> creation;
    public static volatile SingularAttribute<Evenement, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Evenement, Commentaire> commentaireSet;
}
