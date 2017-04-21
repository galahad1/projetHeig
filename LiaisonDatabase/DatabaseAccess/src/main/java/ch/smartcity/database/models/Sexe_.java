package database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Sexe.class)
public class Sexe_ {

    public static volatile SingularAttribute<Sexe, Integer> idSexe;
    public static volatile SingularAttribute<Sexe, String> nomSexe;
    public static volatile SingularAttribute<Sexe, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<Sexe, Utilisateur> utilisateurSet;
}
