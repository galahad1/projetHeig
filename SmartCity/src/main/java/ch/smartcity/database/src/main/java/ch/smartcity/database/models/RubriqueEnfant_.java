package ch.smartcity.database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(RubriqueEnfant.class)
public class RubriqueEnfant_ {

    public static volatile SingularAttribute<RubriqueEnfant, Integer> idRubriqueEnfant;
    public static volatile SingularAttribute<RubriqueEnfant, RubriqueParent> rubriqueParent;
    public static volatile SingularAttribute<RubriqueEnfant, String> nomRubriqueEnfant;
    public static volatile SingularAttribute<RubriqueEnfant, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<RubriqueEnfant, Evenement> evenementSet;
    public static volatile SetAttribute<RubriqueEnfant, Confiance> confianceSet;
}
