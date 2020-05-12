-- Domotica database V7

-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema domotica
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `domotica` ;

-- -----------------------------------------------------
-- Schema domotica
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `domotica` DEFAULT CHARACTER SET latin1 ;
USE `domotica` ;

-- -----------------------------------------------------
-- Table `domotica`.`profile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `domotica`.`profile` (
  `ProfileId` INT(11) NOT NULL AUTO_INCREMENT,
  `Gebruikersnaam` VARCHAR(10) NOT NULL,
  `TempVerwarmen` DOUBLE NULL DEFAULT 20.0,
  `LichtWaarde` INT(4) NULL DEFAULT 5,
  `LaatsteLogin` DATETIME NULL DEFAULT NOW(),
  PRIMARY KEY (`ProfileId`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `domotica`.`afspeellijst`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `domotica`.`afspeellijst` (
  `AfspeellijstId` INT(11) NOT NULL AUTO_INCREMENT,
  `ProfileId` INT(11) NOT NULL,
  `Naam` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`AfspeellijstId`),
  INDEX `ProfileId` (`ProfileId` ASC),
  CONSTRAINT `afspeellijst_ibfk_1`
    FOREIGN KEY (`ProfileId`)
    REFERENCES `domotica`.`profile` (`ProfileId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `domotica`.`nummer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `domotica`.`nummer` (
  `NummerId` INT(11) NOT NULL AUTO_INCREMENT,
  `Naam` VARCHAR(45) NOT NULL,
  `Artiest` VARCHAR(45) NULL DEFAULT NULL,
  `Tijdsduur` INT(11) NOT NULL,
  `BestandsNaam` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`NummerId`),
  UNIQUE INDEX `Locatie_UNIQUE` (`BestandsNaam` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `domotica`.`afspeellijst_nummer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `domotica`.`afspeellijst_nummer` (
  `AfspeellijstId` INT(11) NOT NULL,
  `NummerId` INT(11) NOT NULL,
  PRIMARY KEY (`AfspeellijstId`, `NummerId`),
  INDEX `NummerId` (`NummerId` ASC),
  CONSTRAINT `afspeellijst_nummer_ibfk_1`
    FOREIGN KEY (`AfspeellijstId`)
    REFERENCES `domotica`.`afspeellijst` (`AfspeellijstId`),
  CONSTRAINT `afspeellijst_nummer_ibfk_2`
    FOREIGN KEY (`NummerId`)
    REFERENCES `domotica`.`nummer` (`NummerId`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `domotica`.`log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `domotica`.`log` (
  `Date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `Temperatuur` INT(11) NULL DEFAULT NULL,
  `Luchtvochtigheid` INT(11) NULL DEFAULT NULL,
  `Luchtdruk` INT(11) NULL DEFAULT NULL,
  `Lichtsterkte` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`Date`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
