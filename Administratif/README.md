# Choix sujet PRO  

## Gestionnaire administratif d'une ville  

Auteurs: Camilo __Pineda Serna__, Jérémie __Zanone__, Loan __Lassalle__, Luana __Martelli__, Tano __Iannetta__ et Wojciech __Myszkorowski__  

Notre projet à pour but de gérer des propositions de citoyens, la gestion d'évènements officiels, la génération de rapports et de statistiques d'une région, typiquement une ville, avec l'aide visuelle d'une carte interactive.  

Nous voulons implémenter une application permettant à l'administration d'une région (commune, ville, ...) d'organiser les propositions (de réparations, d'évènements, ...) des citoyens et lui permettre d'avoir une vue spécifique sur une carte de la région, afin de mieux prendre des décisions (de réaliser des chantiers, d'informer les divers départements, ...).  

> Il est à noter que nous réalisons la partie _administrative_ de l'application; c'est-à-dire que notre projet sera utilisé par la ville et non pas par les citoyens de la ville. Nous gardons l'application mobile client comme fonctionnalité future, qui n'entre pas dans le cadre du cours PRO.


### Fonctionnalités principales  
#### Organisation des propositions  
1. Gestion dans une base de données de toutes les propositions/notifications remontées par les citoyens    
	* Les propositions appartiendront à une catégorie (accident, demande de réparation, évènement, ...)  
	* L'utilisateur de notre application administrative pourra gérer les catégories. D'ailleurs, celles des citoyens ne sont pas forcément les mêmes que celles des utilisateurs de notre application (à traiter, proposition en cours de traitement, wontfix, ...)  
* Recensement, statistiques des propositions faites par les citoyens  
	* Projets proposés (exemple: garderie)  
	* chantiers en cours  
* Gestion d'un agenda  
	* pour toutes les proprositions/notifications chacune a une date de création et pour les travaux une date de fin estimé.
	* organisation de la carte peut etre par date (jour principalement).
* Gestion des notifications faites par les utilisateurs  
	* Réparations/dégâts  
	* Accidents 
	* Information d'évènements  
* Ajout d’événements de la ville  
* (Ajout de lieu d'intérêts de la ville  ) ... |pas sur que se soit vraiment notre but..à discuter ulterieurement je pense|
* Ajout de news


####  Implémentation d'une carte interactive  
1. Affichage interactif de la carte (zoom, déplacement manuel, déplacement par recherche de coordonnées)  
* génération de coordonnées de propositions    
* ajout d'icones (flags) sur la carte  
* gestion des secteurs sur la carte (quartiers, communes, zone gérée par le service de police de l'ouest lausannois, ...)
* filtre des propositions par date  
* coloriage de la carte selon des critères de secteurs  


#### GUI  
1. Filtres des éléments à afficher sur la carte  
	* Information générales  
	* Évènements  
	* Travaux
	* Accidents
	* Statistiques
	* Services publiques
* La carte avec les boutons d'interaction  
* Fenêtre de gestion des propositions (marquer en cours, fini, ...)
* Option pour ajouter des lieux/icones  


### Fonctionnalités supplémentaires (suivant possibilité)  
1. génération de fichiers PDF afin d'avoir un rapport ou des statistiques sur une catégorie précise  
* Dessins sur la carte  (cercles, lignes, texte)  

### Fonctionnalité future  
 1. Application client mobile  
