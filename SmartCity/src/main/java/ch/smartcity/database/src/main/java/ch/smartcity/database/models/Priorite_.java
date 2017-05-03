package ch.smartcity.database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Priorite.class)
public class Priorite_ {

    public static volatile SingularAttribute<Priorite, Integer> idPriorite;
    public static volatile SingularAttribute<Priorite, String> nomPriorite;
    public static volatile SingularAttribute<Priorite, Integer> niveau;
    public static volatile SingularAttribute<Priorite, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Priorite, Evenement> evenementSet;
}
