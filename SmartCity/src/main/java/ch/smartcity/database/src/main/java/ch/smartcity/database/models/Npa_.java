package ch.smartcity.database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Npa.class)
public class Npa_ {

    public static volatile SingularAttribute<Npa, Integer> idNpa;
    public static volatile SingularAttribute<Npa, String> numeroNpa;
    public static volatile SingularAttribute<Npa, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Npa, Adresse> adressesSet;
}
