package ch.smartcity.database.models;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(TitreCivil.class)
public class TitreCivil_ {

    public static volatile SingularAttribute<TitreCivil, Integer> idTitreCivil;
    public static volatile SingularAttribute<TitreCivil, String> titre;
    public static volatile SingularAttribute<TitreCivil, String> abreviation;
    public static volatile SingularAttribute<TitreCivil, Calendar> derniereMiseAJour;
    public static volatile SetAttribute<TitreCivil, Utilisateur> utilisateurSet;
}
