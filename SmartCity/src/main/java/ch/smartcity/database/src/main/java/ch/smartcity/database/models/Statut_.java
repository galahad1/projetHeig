package ch.smartcity.database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Statut.class)
public class Statut_ {

    public static volatile SingularAttribute<Statut, Integer> idStatut;
    public static volatile SingularAttribute<Statut, String> nomStatut;
    public static volatile SingularAttribute<Statut, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Statut, Evenement> evenementSet;
}
