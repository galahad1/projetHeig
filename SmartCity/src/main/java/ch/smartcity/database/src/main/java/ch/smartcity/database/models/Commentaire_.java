package ch.smartcity.database.models;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Calendar;

/**
 * Modélise les attributs de la table comentaires de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
@StaticMetamodel(Commentaire.class)
public class Commentaire_ {

    public static volatile SingularAttribute<Commentaire, IdCommentaire> idCommentaire;
    public static volatile SingularAttribute<Commentaire, String> commentaire;
    public static volatile SingularAttribute<Commentaire, Calendar> creation;
    public static volatile SingularAttribute<Commentaire, Calendar> derniereMiseAJour;
}
