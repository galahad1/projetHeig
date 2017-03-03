# brouillon CC.
> le vrai est dessous

il se logue  
il peut choisir les rubriques a afficher  
on peut zoomer sur la carte  
tout a des priorités : haute, dérangeant, faible  
notifications en attente : comptes privilégiés (genre les notifs accident proposées par les TCS sont directement validés)  
pdf : selon la vue active : résumé et un fichier par "event", si plus que 5 events à générer, demande confirmation ?  
calendrier : tu cliques sur la date et cela t'affiche tout ce qui s'est passé à cette date.  
pdf : détail des rubriques. une page par rubrique ? une page de résumé ?

citoyen propose manifestation  
elle est a modérée : acceptée ->


filtre de grossiertées, max 2 par jour, coté client ??





# __Cahier des charges__ du Gestionnaire administratif d'une ville - Smartcity


# Auteurs: Camilo __Pineda Serna__, Jérémie __Zanone__, Loan __Lassalle__, Luana __Martelli__, Tano __Iannetta__ et Wojciech __Myszkorowski__  

## 1 But du cahier des charges  
Ce cahier des charges décrit les objectifs à atteindre au terme de la réalisation de notre projet.

## 2 Situation de départ  
Dans le cadre de l'unité « Projet de semestre », nous avons comme tâche de réaliser un projet par équipes. Notre projet a pour but de gérer des propositions de citoyens, la gestion d'évènements officiels, la génération de rapports et de statistiques d'une ville, avec l'aide visuelle d'une carte interactive.


## 3 Description du projet 
Nous voulons implémenter une application permettant à l'administration d'une ville d'organiser les propositions (de réparations, d'évènements, ...) de ses citoyens. Chaque requête doit être validée par un administrateur et ajoutée à une rubrique dédiée. Il est ensuite possible de consulter ces différentes rubriques et de visualiser au moyen d'une carte interactive les endroits concernés. Pour une meilleure visibilité, chaque rubrique est associée à un filtre, rendant ainsi plus aisé la lecture de la carte. Il est aussi possible pour l'adminitrateur de donner des priorités aux événements (haute ou à titre informative) afin de traiter plus efficacement les requêtes. L'utilisateur peut aussi consulter la carte selon une date précise. Finalement, l'administateur peut générer des PDF contenant des informations et statistiques relatives aux événements. 

## 3.1	Modèle conceptuel de données  
(Schéma relationnel ??????)
Schéma EA 

> Modèle conceptuel de données (MCD) – et ceci même si le projet ne comporte pas de base de données !  
Description des données traitées. Modèle conceptuel (MCD) de la base de données, description des « Entités » et des liens entre elles.

## 3.2	Maquette fonctionnelles  

> Maquettes d’interface utilisateur. A ce stade il ne doit pas y avoir de détails ; il s’agit de montrer le nombre d’écrans/fenêtres différents, les informations qu’ils contiendront et leur organisation générale  
Maquettes fonctionnelles (dessins et explications permettant de comprendre comment le logiciel fonctionnera).

## 4 Fonctionnalités principales  

1. Gestion, dans une base de données, de toutes les propositions/notifications remontées par les citoyens    
	* Les propositions appartiendront à une catégorie (accident, demande de réparation, évènement, ...)  
	* L'utilisateur de notre application administrative pourra gérer les catégories. D'ailleurs, celles des citoyens ne sont pas forcément les mêmes que celles des utilisateurs de notre application (à traiter, proposition en cours de traitement, wontfix, ...) 
	* Un événement à une priorité (haute, dérangeant, faible)  
* Gestion d'un agenda  
	* Pour toutes les proprositions/notifications chacune a une date de création et pour les travaux une date de fin estimée.
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


2. Implémentation d'une carte interactive  
* Affichage interactif de la carte (zoom, déplacement manuel, déplacement par recherche de coordonnées)  
* Génération de coordonnées de propositions    
* Ajout d'icones (flags) sur la carte  
* Gestion des secteurs sur la carte (quartiers, communes, zone gérée par le service de police de l'ouest lausannois, ...)
* Filtre des propositions par date  


3. GUI  
* Filtres des éléments à afficher sur la carte selon les différentes catégories  
	* Information générales  
	* Évènements  
	* Travaux
	* Accidents
	* Statistiques
	* Services publiques
* Ajout de ping sur la carte selon les événements
* La carte avec les boutons d'interaction  
* Fenêtre de gestion des propositions (marquer en cours, fini, ...)
* Option pour ajouter des lieux/icones  


4. Génération d'un PDF 
* Génération d'un PDF selon un filtre choisi 
* Contient le résumé des informations pratiques d'une catégorie (contexte, date, message...)
* Statistiques basés sur les données archivées selon le filtre choisi
* Si plusieurs filtres ont été selectionné, alors une option est de génération automatique de plusieurs PDF  


## 1.2	Fonctionnalités supplémentaires (suivant possibilité)  

1. Gestion de plusieurs comptes utilisateurs  
2. Dessins et coloriages sur la carte selon des critères de secteurs (cercles, lignes, texte)  
3. Ajout d'un screenshot dans le PDF de l'état de la carte 

## 1.3	Fonctionnalité future

 1. Application client mobile
 	* Ajout d'un filtre à spam pour supprimer les messages non désiré 





## 1.7	Description de l'intrface graphique
Mettre la GUI ici ? 




## 3.3	Contraintes

> Description des contraintes que l’outil réalisé subira.
L'application produite doit être fonctionnelle sur les machines Windows.

## 3.4	Situation actuelle

> Description de la situation actuelle de l’outil utilisé.

## 3.5	Situation future

> Description de la situation future avec l’outil réalisé et mis en service.




## 3.6	Exigences envers le système

> Description des exigences à validées envers l’outil, des standards techniques, physique et moral du système qui accueillera l’outil.

## 3.7	Architecture centrale

> Modèle conceptuel de données, schéma, dessin ou plan de l’architecture technique du système.

## 3.8	Planning prévisionnel

> Planning prévisionnel que le soumissionnaire devra respecter.
Les dates de début, de fin et des étapes principales seront mises en évidence.
Ces éléments peuvent être repris de la fiche signalétique

>Phase	Date début	Date fin  
Administration		  
Dossier de projet		
Analyse		
Conception		
Réalisation		
Test		
Documentation		
Rendu		

## 3.9	Organisation

Description de l’organisation du projet, des référents et responsables

## 7	Annexes
