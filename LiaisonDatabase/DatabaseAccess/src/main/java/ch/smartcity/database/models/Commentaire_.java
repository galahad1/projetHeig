package database.models;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

@StaticMetamodel(Commentaire.class)
public class Commentaire_ {

    public static volatile SingularAttribute<Commentaire, IdCommentaire> idCommentaire;
    public static volatile SingularAttribute<Commentaire, String> commentaire;
    public static volatile SingularAttribute<Commentaire, Calendar> creation;
    public static volatile SingularAttribute<Commentaire, Calendar> derniereMiseAJour;
}
