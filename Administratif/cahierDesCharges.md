# brouillon CC.
> le vrai est dessous


pdf : selon la vue active : résumé et un fichier par "event", si plus que 5 events à générer, demande confirmation ?  
calendrier : tu cliques sur la date et cela t'affiche tout ce qui s'est passé à cette date.  

pdf : détail des rubriques. une page par rubrique ? une page de résumé ?




# __Cahier des charges__ du Gestionnaire administratif d'une ville - Smartcity


Auteurs: Camilo __Pineda Serna__, Jérémie __Zanone__, Loan __Lassalle__, Luana __Martelli__, Tano __Iannetta__ et Wojciech __Myszkorowski__ 

## 1 But du cahier des charges  
Ce cahier des charges décrit les objectifs à atteindre au terme de la réalisation de notre projet.

## 2 Situation de départ  
Dans le cadre de l'unité « Projet de semestre », nous avons comme tâche de réaliser un projet par équipes. Notre projet a pour but de gérer des propositions de citoyens, la gestion d'évènements officiels, la génération de rapports et de statistiques d'une ville, avec l'aide visuelle d'une carte interactive.


## 3 Description du projet 
Nous voulons implémenter une application permettant à l'administration d'une ville (dans notre cas Lausanne) d'organiser les propositions (de réparations, d'évènements, ...) de ses citoyens. Chaque requête doit être validée par un administrateur et ajoutée à une rubrique dédiée. Il est ensuite possible de consulter ces différentes rubriques et de visualiser au moyen d'une carte interactive les endroits concernés. Pour une meilleure visibilité, chaque rubrique est associée à un filtre, rendant ainsi plus aisé la lecture de la carte. Il est aussi possible pour l'administrateur de donner des priorités aux événements (haute ou à titre informative) afin de traiter plus efficacement les requêtes. L'utilisateur peut aussi consulter la carte selon une date précise. Finalement, l'administrateur peut générer des PDF contenant des informations et statistiques relatives aux événements. 

## 3.1	Modèle conceptuel de données 
![Alt text](../Database/smartcity_diagram_ea.png "Smartcity_diagram")

> Modèle conceptuel de données (MCD) – et ceci même si le projet ne comporte pas de base de données !  
Description des données traitées. Modèle conceptuel (MCD) de la base de données, description des « Entités » et des liens entre elles.

## 3.2	Maquette fonctionnelles  

> Maquettes d’interface utilisateur. A ce stade il ne doit pas y avoir de détails ; il s’agit de montrer le nombre d’écrans/fenêtres différents, les informations qu’ils contiendront et leur organisation générale  
Maquettes fonctionnelles (dessins et explications permettant de comprendre comment le logiciel fonctionnera).

## 4 Fonctionnalités principales  

1. Gestion, dans une base de données, de toutes les propositions/notifications remontées par les citoyens    
	* Les propositions appartiendront à une catégorie (accidents, évènements, ...)  
	* L'utilisateur de notre application administrative pourra gérer les catégories. D'ailleurs, celles des citoyens ne sont pas forcément les mêmes que celles des utilisateurs de notre application (à traiter, proposition en cours de traitement, wontfix, ...) 
	* Un événement a une priorité (haute, dérangeant, faible)  
* Gestion d'un agenda  
	* Pour toutes les propositions/notifications chacune a une date de création et pour les travaux une date de fin estimée.
	* Organisation de la carte selon les filtres des rubriques et de la date (jour principalement).
* Gestion des notifications faites par les utilisateurs  
	* Réparations/dégâts  
	* Accidents
	* Information d'évènements
* Filtrage des notifications
	* Gestion des événements (acceptation de notifications, suppression...)
  * Comptes privilégiés (par exemple les accidents proposés par les TCS sont directement validés) 
* Ajout d’événements de la ville  
* Ajout d'informations générales


2. Implémentation d'une carte interactive
* Affichage interactif de la carte (zoom, déplacement manuel)
* Ajout d'icônes (pins) sur la carte pour localiser les événements 
* Filtre des événements par date  



3. Génération d'un PDF 
* Génération d'un PDF selon un filtre choisi 
* Contient le résumé des informations pratiques d'une catégorie (contexte, date, message...)
* Statistiques basés sur les données archivées selon le filtre choisi
* Si plusieurs filtres ont été sélectionné, alors une option est de génération automatique de plusieurs PDF  


## 1.2	Fonctionnalités supplémentaires (suivant possibilité)  

1. Gestion de plusieurs comptes utilisateurs  
2. Dessins et coloriages sur la carte selon des critères de secteurs (cercles, lignes, texte)  
3. Ajout d'un screenshot dans le PDF de l'état de la carte 
4. calcul de chemin les plus court en évitant les zones accidenté ou en travaux.

## 1.3	Fonctionnalité future

 1. Application client mobile
 	* Ajout d'un filtre à spam pour supprimer les messages non désirées et/ou restreindre le nombre de requêtes par citoyen sur une période de temps. Ainsi qu'un contrôle de grossiertées.





## 1.7	Description de l'interface graphique

La fenêtre principale sera composée:

- En haut, un menu permettant d'ajouter des événements, apporter des modifications, générer un fichier PDF ainsi qu'une modération des événements (propositions) en attente de validation reçu des citoyens avec un compteur (notification). Chacune de ces fonctionnalités sera gérées dans une autre fenêtre.

- Sur la gauche, les différentes rubriques (filtres) organisées de la manière suivante:
    - Traffic
        - Accidents
        - Travaux
    - Culture
        - Manifestations
    - Chantiers
        - Rénovations
        - Constructions
    - Doléances
  
     Elles pourront être cochées afin de les visualiser au centre du programme, d'apparaitre sur la carte sous forme de pin ainsi que de déterminer les informations lors de la génération du PDF.

- Sur la droite la carte composée de tuiles fournies par OpenStreetMap (OSM) liée à un calendrier pour le filtrage des événements sur le plan temporel.




## 3.3	Contraintes

L'application produite doit être fonctionnelle sur les machines Windows.
Il faudra avoir accès à une connexion internet afin d'avoir accès aux tuiles OSM de la carte.

## 3.4	Situation actuelle

> Description de la situation actuelle de l’outil utilisé.

## 3.5	Situation future

> Description de la situation future avec l’outil réalisé et mis en service.




## 3.6	Exigences envers le système

La machine devra etre doter d'au moins 4GB de RAM ainsi que d'un processeur de 5 ème génération afin de garantir un fonctionnement fluide de l'application. 

## 3.7	Architecture centrale

>Modèle conceptuel de données, schéma, dessin ou plan de l’architecture technique du système.


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
