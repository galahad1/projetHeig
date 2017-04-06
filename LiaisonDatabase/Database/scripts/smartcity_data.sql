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
INSERT INTO `smartcity`.`RubriqueParent` (`nomRubriqueParent`) VALUES ('TRAFIC'),('MANIFESTATION'),('CHANTIERS'),('DOLEANCES');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`RubriqueEnfant`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`RubriqueEnfant` (`idRubriqueParent`,`nomRubriqueEnfant`) VALUES (1,'Accidents'),(1,'Travaux'),(2,'Manifestations'),(3,'Rénovations'),(3,'Constructions');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Priorite`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Priorite` (`nomPriorite`,`niveau`) VALUES ('Mineur', 0),('Gênant', 1),('Préocupant', 2),('Important', 3),('Urgent', 4);
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Statut`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Statut` (`nomStatut`) VALUES ('En attente'),('Traité'),('Refusé');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Rue`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Rue` (`nomRue`) VALUES ('Rue Adrien-Pichard'),('Rue Auguste-Piclou'),('Avenue des Acacias'),('Rue de l’Académie'),('Avenue Agassiz'),('Rue de l’Ale'),('Avenue des Alpes'),('Avenue André Schnetzler'),('Avenue André Schnetzler');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`NPA`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Npa` (`numeroNpa`) VALUES ('1000'),('1001'),('1002'),('1003'),('1004'),('1005'),('1006'),('1007'),('1010'),('1011'),('1012'),('1015'),('1018');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Adresse`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Adresse` (`idRue`,`numeroDeRue`,`idNpa`) VALUES (1,'1',1),(1,'2',1),(1,'3',1),(1,'4',1),(1,'5',1),(1,'6',1),(2,'7',2),(2,'8',2),(2,'9',2),(2,'10',2),(2,'11',2),(2,'12',2),(3,'13',5),(4,'14',5),(5,'15',2),(6,'16',6),(7,'17',7),(8,'18',7);
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`TitreCivil`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`TitreCivil` (`titre`,`abreviation`) VALUES ('Monsieur','M'),('Madame','Mme'),('Mademoiselle','Mlle'),('Docteur','Dr'),('Maître','Me'),('Professeur','Pr');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Nationalite`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Nationalite` (`nomNationalite`) VALUES ('Afghan'),('Albanais'),('Belge'),('Britanique'),('Canadien'),('Espagnol'),('Français'),('Italien'),('Russe'),('Suisse'),('Turc'),('Vietnamien');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Sexe`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Sexe` (`nomSexe`) VALUES ('Homme'),('Femme');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Utilisateur`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Utilisateur` (`personnePhysique`,`avs`,`idTitreCivil`,`nomUtilisateur`,`prenom`,`dateDeNaissance`,`idSexe`,`idNationalite`,`idAdresse`,`email`,`pseudo`,`motDePasse`,`sel`) VALUES (1,'TEST',1,'TEST','TEST',NOW(),1,1,1,'TEST','TEST','TEST','TEST');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`Evenement`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`Evenement` (`idRubriqueEnfant`,`idUtilisateur`,`nomEvenement`,`idAdresse`,`latitude`,`longitude`,`debut`,`details`,`idPriorite`,`idStatut`) VALUES (1,1,'TEST',1,3.14,3.14,NOW(),'TEST',1,1);
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`EvenementUtilisateur`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`EvenementUtilisateur` (`idEvenement`,`idUtilisateur`,`commentaire`) VALUES (1,1,'Début approximatif'); -- ,(2,2,'C\'est pas forcément une bonne idée. Mais c\'est mon avis.'),(3,3,'J\'aime beaucoup ce groupe !'),(3,4,'Je ne trouve pas les mots');
COMMIT;

-- -----------------------------------------------------
-- Table `smartcity`.`UtilisateurConfianceRubriqueEnfant`
-- Dumping data
-- -----------------------------------------------------

SET AUTOCOMMIT=0;
INSERT INTO `smartcity`.`UtilisateurConfianceRubriqueEnfant` (`idUtilisateur`,`idRubriqueEnfant`) VALUES (1,1),(1,2);
COMMIT;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
