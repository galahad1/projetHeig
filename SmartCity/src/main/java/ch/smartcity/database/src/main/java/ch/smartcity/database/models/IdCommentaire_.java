package ch.smartcity.database.models;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(IdCommentaire.class)
public class IdCommentaire_ {
    public static volatile SingularAttribute<IdCommentaire, Evenement> evenement;
    public static volatile SingularAttribute<IdCommentaire, Utilisateur> utilisateur;
}
