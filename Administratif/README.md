# Choix sujet PRO  

## Gestionnaire administratif d'une ville - Smartcity

Auteurs: Camilo __Pineda Serna__, Jérémie __Zanone__, Loan __Lassalle__, Luana __Martelli__, Tano __Iannetta__ et Wojciech __Myszkorowski__  

Notre projet a pour but de gérer des propositions de citoyens, la gestion d'évènements officiels, la génération de rapports et de statistiques d'une ville, avec l'aide visuelle d'une carte interactive.  


#### Description du projet
Nous voulons implémenter une application permettant à l'administration d'une ville d'organiser les propositions (de réparations, d'évènements, ...) de ses citoyens. Chaque requête doit être validée par un administrateur et ajoutée à une rubrique dédiée. Il est ensuite possible de consulter ces différentes rubriques et de visualiser au moyen d'une carte interactive les endroits concernés. Pour une meilleure visibilité, chaque rubrique est associée à un filtre, rendant ainsi plus aisé la lecture de la carte. Il est aussi possible pour l’administrateur de donner des priorités aux événements (haute ou à titre informative) afin de traiter plus efficacement les requêtes. L'utilisateur peut aussi consulter la carte selon une date précise. Finalement, l’administrateur peut générer des PDF contenant des informations et statistiques relatives aux événements.

#### Spécification
> Il est à noter que nous réalisons la partie _administrative_ de l'application; c'est-à-dire que notre projet sera utilisé par la ville et non pas par les citoyens de la ville. Nous gardons l'application mobile client comme fonctionnalité future, qui n'entre pas dans le cadre du cours PRO.



#### Organisation des propositions  

#### Fonctionnalités principales  
1. Gestion, dans une base de données, de toutes les propositions/notifications remontées par les citoyens    
	* Les propositions appartiendront à une catégorie (accident, demande de réparation, évènement, ...)  
	* L'utilisateur de notre application administrative pourra gérer les catégories. D'ailleurs, celles des citoyens ne sont pas forcément les mêmes que celles des utilisateurs de notre application (à traiter, proposition en cours de traitement, wontfix, ...)
	* Un événement à une priorité (haute, dérangeant, faible)  
* Gestion d'un agenda  
	* Pour toutes les propositions/notifications chacune a une date de création et pour les travaux une date de fin estimée.
	* Organisation de la carte, peut être par date (jour principalement).
* Gestion des notifications faites par les utilisateurs  
	* Réparations/dégâts  
	* Accidents
	* Information d'évènements
* Filtrage des notifications
	* Gestion des événements (acceptation de notifications, suppression...)
* Ajout d’événements de la ville  
* Ajout de lieu d'intérêts de la ville
* Ajout de informations générales


#### Implémentation d'une carte interactive  
1. Affichage interactif de la carte (zoom, déplacement manuel, déplacement par recherche de coordonnées)  
* Génération de coordonnées de propositions    
* Ajout d’icônes (flags) sur la carte  
* Gestion des secteurs sur la carte (quartiers, communes, zone gérée par le service de police de l'ouest lausannois, ...)
* Filtre des propositions par date  


#### GUI  
1. Filtres des éléments à afficher sur la carte selon les différentes catégories  
	* Information générales  
	* Évènements  
	* Travaux
	* Accidents
	* Statistiques
	* Services publiques
* Ajout de ping sur la carte selon les événements
* La carte avec les boutons d'interaction  
* Fenêtre de gestion des propositions (marquer en cours, fini, ...)
* Option pour ajouter des lieux/icônes  


#### Génération d'un PDF
1. Génération d'un PDF selon un filtre choisi
* Contient le résumé des informations pratiques d'une catégorie (contexte, date, message...)
* Statistiques basés sur les données archivées selon le filtre choisi
* Si plusieurs filtres ont été selectionné, alors une option est de génération automatique de plusieurs PDF


#### Fonctionnalités supplémentaires (suivant possibilité)  
1. Gestion de plusieurs comptes utilisateurs  
2. Dessins et coloriages sur la carte selon des critères de secteurs (cercles, lignes, texte)  
3. Ajout d'un screenshot dans le PDF de l'état de la carte

#### Fonctionnalité future  
 1. Application client mobile
 	* Ajout d'un filtre à spam pour supprimer les messages non désiré
