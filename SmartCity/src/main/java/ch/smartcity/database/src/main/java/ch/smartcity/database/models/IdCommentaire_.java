package ch.smartcity.database.models;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Modélise les attributs de l'identifiant de la table commentaire de la base de données
 *
 * @author Lassalle Loan
 * @since 25.03.2017
 */
@StaticMetamodel(IdCommentaire.class)
public class IdCommentaire_ {

    public static volatile SingularAttribute<IdCommentaire, Evenement> evenement;
    public static volatile SingularAttribute<IdCommentaire, Utilisateur> utilisateur;
}
