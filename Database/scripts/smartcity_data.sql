-- Smartcity Database Data
--
-- MySQL Script generated by MySQL Workbench
-- Sun Apr  2 12:27:15 2017
-- Model: Smartcity    Version: 1.0
-- Author: Loan Lassalle
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema smartcity
-- -----------------------------------------------------
USE `smartcity` ;

-- -----------------------------------------------------
-- Table `smartcity`.`RubriqueParent`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`RubriqueParent` (`nomRubriqueParent`) VALUES 
('trafic'),('manifestation'),('chantiers'),('doléances');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`RubriqueEnfant`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`RubriqueEnfant` (`idRubriqueParent`,`nomRubriqueEnfant`) VALUES 
(1,'accidents'),(1,'travaux'),(2,'manifestations'),(3,'rénovations'),
(3,'constructions'),(4,'doléances');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Priorite`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Priorite` (`nomPriorite`,`niveau`) VALUES 
('mineur',0),('gênant',1),('préocupant',2),('important',3),('urgent',4);
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Statut`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Statut` (`nomStatut`) VALUES 
('en attente'),('traité'),('refusé');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Rue`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Rue` (`nomRue`) VALUES 
('rue adrien-pichard'),('rue auguste-piclou'),('avenue des acacias'),('rue de l’académie'),
('avenue agassiz'),('rue de l’ale'),('avenue des alpes'),('avenue andré schnetzler'),
('avenue andré schnetzler');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`NPA`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Npa` (`numeroNpa`) VALUES 
('1000'),('1001'),('1002'),('1003'),
('1004'),('1005'),('1006'),('1007'),
('1010'),('1011'),('1012'),('1015'),
('1018');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Adresse`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Adresse` (`idRue`,`numeroDeRue`,`idNpa`) VALUES 
(1,'1',1),(1,'2',1),(1,'3',1),(1,'4',1),
(1,'5',1),(1,'6',1),(2,'7',2),(2,'8',2),
(2,'9',2),(2,'10',2),(2,'11',2),(2,'12',2),
(3,'13',5),(4,'14',5),(5,'15',2),(6,'16',6),
(7,'17',7),(8,'18',7);
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`TitreCivil`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`TitreCivil` (`titre`,`abreviation`) VALUES 
('monsieur','m'),('madame','mme'),('mademoiselle','mlle'),('docteur','dr'),
('maître','me'),('professeur','pr'),('entreprise','ent');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Nationalite`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Nationalite` (`nomNationalite`) VALUES 
('afghane'),('albanaise'),('allemande'),('belge'),
('britanique'),('canadienne'),('espagnole'),('française'),
('italienne'),('russe'),('suisse'),('turque'),
('vietnamienne');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Sexe`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Sexe` (`nomSexe`) VALUES 
('homme'),('femme');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Utilisateur`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Utilisateur` (`personnePhysique`,`avs`,`idTitreCivil`,`nomUtilisateur`,`prenom`,`dateDeNaissance`,`idSexe`,`idNationalite`,`idAdresse`,`email`,`pseudo`,`motDePasse`,`sel`) VALUES 
(1,'756.1234.5678.97',1,'lassalle','loan',CURDATE(),1,7,1,'loan.lassalle@heig-vd.ch','loan.lassalle','_smartcity_','3EgLiQyssGvABUUaqFGUZgrDKGPOlA'),
(0,NULL,7,'touring club suisse',NULL,NULL,NULL,NULL,1,'contact-tcs@tcs.ch','contact-tcs','contact-tcs','CG5bWHG0YxOvABUUaF8ra0HQvvJXr7');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Evenement`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Evenement` 
(`idRubriqueEnfant`,`idUtilisateur`,`nomEvenement`,`idAdresse`,`latitude`,`longitude`,`details`,`idPriorite`,`idStatut`) VALUES 
(1,1,'accidents entrée autoroute bloqué',1,46.52304, 6.58939,'Une voiture s\'est encastrée dans un bus',4,2),
(1,1,'accidents tunnel Malley fermé',1,46.52651, 6.60319,'Deux voitures ont fait un frontal, le tunnel est fermé',4,2),
(2,1,'travaux rond-point',1,46.51665, 6.61917,'construction d\'un rond-point, trafic ralenti',4,2),
(3,1,'manifestation WWF',1,46.52073, 6.63069,'Un camps avec des tentes ont été installé par les manifestants',4,2),
(4,1,'rénovation de la façade de la gare',1,46.51717, 6.62923,'Endroit bruyant',4,1),
(5,1,'construction d\'un nouveau batiment',1,46.50987, 6.6373,'Construction d\'un batiment pour le minage de bitcoin',4,1),
(6,1,'doléance banc cassé',1,46.51716, 6.60333,'Le banc en bois a été scié en deux',4,1);
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Commentaire`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Commentaire` (`idEvenement`,`idUtilisateur`,`commentaire`) VALUES 
(1,1,'début de l\'accident approximatif');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Confiance`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Confiance` (`idUtilisateur`,`idRubriqueEnfant`) VALUES 
(1,1),(1,2);
COMMIT;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
