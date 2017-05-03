package ch.smartcity.database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Rue.class)
public class Rue_ {

    public static volatile SingularAttribute<Rue, Integer> idRue;
    public static volatile SingularAttribute<Rue, String> nomRue;
    public static volatile SingularAttribute<Rue, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Rue, Adresse> adresseSet;
}
