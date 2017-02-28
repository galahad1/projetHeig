# Cahier des charges du Gestionnaire de mots de passe  
 > Le cahier des charges détaillé:
•	Modèle conceptuel de données (MCD) – et ceci même si le projet ne comporte pas de base de données !
•	Maquettes d’interface utilisateur. A ce stade il ne doit pas y avoir de détails ; il s’agit de montrer le nombre d’écrans/fenêtres différents, les informations qu’ils contiendront et leur organisation générale
•	Cas d’utilisation et scénarios
•	Une première liste d’objectifs SMART. Il est clair qu’à ce stade du projet il n’est pas possible d’avoir la liste complète des objectifs

## 1	But du cahier des charges  

> Point visé, cible du cahier des charges.
Exemple : Ce cahier des charges décrit les objectifs à atteindre à l’aide de la solution visée ainsi que les exigences et les souhaits envers le déploiement de systèmes d’exploitation.

Ce cahier des charges décrit les objectifs à atteindre au terme de la réalisation de notre projet.

## 2	Situation de départ

>Description du contexte de départ, de l’entreprise adjudicatrice, du sujet dans sa globalité. Description de l’entreprise qui lance ce projet.
Exemple : Le Centre Professionnel du Nord Vaudois (CPNV) est l’un des 14 établissements d’enseignement professionnel que compte le canton de Vaud. Sa taille et sa structure lui permettent de couvrir une partie importante des besoins en formation professionnelle du Nord vaudois qui représente environ un dixième de la population cantonale.
Le CPNV regroupe les écoles professionnelles et de métiers des sites d’Yverdon-les-Bains, de Sainte-Croix et de Payerne.  
A ce jour, le CPNV ne possède aucune solution permettant le déploiement rapide de postes de travail au sein de leur parc informatique.  
Il serait intéressé par l’acquisition d’un produit permettant d’automatiser la distribution du système d’exploitation utilisé. Il bénéficierait d’un gain de temps et de simplicité de gestion de son parc informatique.

Dans le cadre de l'unité « Projet de semestre », nous avons comme tâche de réaliser un projet par équipes. Nous avons décidé de mettre au point un gestionnaire de mots de passes.

## 3	Objectifs du cahier des charges

> Objectifs SMART du cahier des charges  

## 3.1	Modèle conceptuel de données (Schéma relationnel ??????)

> Modèle conceptuel de données (MCD) – et ceci même si le projet ne comporte pas de base de données !  
Description des données traitées. Modèle conceptuel (MCD) de la base de données, description des « Entités » et des liens entre elles.

## 3.2	Maquette fonctionnelles

> Maquettes d’interface utilisateur. A ce stade il ne doit pas y avoir de détails ; il s’agit de montrer le nombre d’écrans/fenêtres différents, les informations qu’ils contiendront et leur organisation générale  
Maquettes fonctionnelles (dessins et explications permettant de comprendre comment le logiciel fonctionnera).

## 4	Fonctionnalités principales

1.	Stocker, trier et organiser les mots de passes des utilisateurs
2.	Génération de mots de passes suivant plusieurs modes :

1.	L'utilisateur choisit les paramètres de la génération du mot de passe (nombre de chiffres, nombres de lettres, taille, etc.)
2.	L'utilisateur ne choisit pas de paramètres et un mot de passe fort est généré par notre algorithme
3.	L'utilisateur choisit une phrase de base. La phrase est ensuite découpée et convertie en mot de passe selon un algorithme prédéfinit.
3.	Chiffrer du texte
4.	Envoie d’email lors de plusieurs saisies erronées du mot de passe maître
5.	Notifications de la date d'expiration des mots de passes
6.	Une interface graphique sera réalisée afin de permettre une utilisation simple et convivial de notre programme. Nous utiliserons la libraire swing pour réaliser cette tâche.
7.	Timer qui verrouille l’application si l’utilisateur ne touches pas l’application après un certain temps.

## 1.2	Fonctionnalités supplémentaires (suivant possibilité)

1.	Chiffrement de pièces jointes
2.	Analyse de résistance de mots de passes
3.	Double authentification (nonce ou fichier annexe)

## 1.3	Fonctionnalité future

1.	Plugin pour navigateurs web.






## 1.4	Description de la fonctionnalité de gestion des mots de passes

La gestion des mots de passes se fera avec l'utilisation d'une base de données.
Le fichier de la base de données sera chiffré avec le mot de passe maître et donc illisible pour le reste du monde.*





## 1.5	Description de la fonctionnalité de génération de mots de passes

Pour cette fonctionnalité nous implémentons plusieurs modes de génération des mots de passes

1.	L'utilisateur donnera le format du mot passe à générer. Il y a plusieurs paramètres à fournir :
1.	Taille du mot de passe
2.	Le mot de passe doit-il contenir des lettres ?
3.	Le mot de passe doit-il contenir des chiffre ?
4.	Le mot de passe doit-il contenir des caractères spéciaux ?
5.	Caractères obligatoires que doit contenir le mot de passe . Il sera également possible à l'utilisateur d'ajouter des caractères obligatoires sur le mot de passe généré.  
2.	L’utilisateur ne définit pas de paramètres et un mot de passe aléatoire fort est généré.*
3.	L'utilisateur saisit une phrase de base. La phrase est ensuite découpée et convertie en mot de passe suivant un algorithme prédéfinit.*



## 1.6	Description de la fonctionnalité de chiffrement de texte

Cette fonctionnalité a pour but de permettre à l'utilisateur de saisir des notes qui seront par la suite chiffrées suivant un algorithme prédéfinit.




## 1.7	Description de l'intrface graphique



## 1.8	Description de la fonctionnalité de chiffrement de pièces jointes




## 1.9	Description de la fonctionnalité d'analyse de résistance de mot de passe





## 1.10	 Description de la fonctionnalité de double authentification









* Voir spécifications des algorithmes dans la documentation annexe.

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
