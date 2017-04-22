package database.models;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Confiance.class)
public class Confiance_ {

    public static volatile SingularAttribute<Confiance, IdConfiance> idConfiance;
    public static volatile SingularAttribute<Confiance, Calendar> creation;
}
