# __Cahier des charges__ du Gestionnaire de mots de passe  

## 1 But du cahier des charges  
Ce cahier des charges décrit les objectifs à atteindre au terme de la réalisation de notre projet.

## 2 Situation de départ  
Dans le cadre de l'unité « Projet de semestre », nous avons comme tâche de réaliser un projet par équipes. Nous avons décidé de mettre au point un gestionnaire de mots de passes.

## 3	Objectifs du cahier des charges  

## 3.1	Modèle conceptuel de données  
(Schéma relationnel ??????)

> Modèle conceptuel de données (MCD) – et ceci même si le projet ne comporte pas de base de données !  
Description des données traitées. Modèle conceptuel (MCD) de la base de données, description des « Entités » et des liens entre elles.

## 3.2	Maquette fonctionnelles  

> Maquettes d’interface utilisateur. A ce stade il ne doit pas y avoir de détails ; il s’agit de montrer le nombre d’écrans/fenêtres différents, les informations qu’ils contiendront et leur organisation générale  
Maquettes fonctionnelles (dessins et explications permettant de comprendre comment le logiciel fonctionnera).

## 4 Fonctionnalités principales  
Le but de notre logiciel est de permettre à l'utilisateur de stocker des mots de passe et leur contexte d'utilisation (le service où le mot de passe est utilisé). Ainsi, chaque _mot de passe_ correspondant à un ensemble d'informations.

1.	Stocker et organiser les mots de passes de l'utilisateur  
    L'utilisateur peut ajouter, supprimer, éditer ses mots de passe. Chaque mot de passe comprend :
    * un __identificateur__, le nom du mot de passe  
    * un __nom d'utilisateur__, le login  
    * une __phrase de passe__, la chaine de caractères qui doit être protégée  
    * une __url__, le site ou le nom de l'application où le mot de passe est utilisé  
    * des __commentaires__, si l'utilisateur veut ajouter des précisions  
    * une __date de création__
    * une __date d'expiration__, grâce à laquelle le gestionnaire peut informer l'utilisateur de l'expiration du mot de passe  

2.	Générer des phrases de passe, selon plusieurs modes :  
    __mode 1__: L'utilisateur choisit des composants :
        * nombre de caractères  
        * nombre de chiffres  
        * nombres de lettres minuscules  
        * nombres de lettres majuscules  
        * emploi de caractère spéciaux  

    __mode 2__:	L'utilisateur ne choisit pas de paramètres et un mot de passe _fort_ est généré par notre algorithme  
        * l'algorithme tire un nombre suffisant de caractères au hasard  
         
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
2.  possibilité de récupération des mots de passe (par mail, par clé usb, par exemple)  


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
