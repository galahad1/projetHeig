package ch.smartcity.database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Nationalite.class)
public class Nationalite_ {

    public static volatile SingularAttribute<Nationalite, Integer> idNationalite;
    public static volatile SingularAttribute<Nationalite, String> nomNationalite;
    public static volatile SingularAttribute<Nationalite, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Nationalite, Utilisateur> utilisateurSet;
}
