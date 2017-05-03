package ch.smartcity.database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(RubriqueParent.class)
public class RubriqueParent_ {

    public static volatile SingularAttribute<RubriqueParent, Integer> idRubriqueParent;
    public static volatile SingularAttribute<RubriqueParent, String> nomRubriqueParent;
    public static volatile SingularAttribute<RubriqueParent, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<RubriqueParent, RubriqueEnfant> rubriqueEnfantSet;
}
