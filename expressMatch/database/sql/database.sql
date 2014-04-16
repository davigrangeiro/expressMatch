SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `expressMatch` DEFAULT CHARACTER SET latin1 ;
USE `expressMatch` ;

-- -----------------------------------------------------
-- Table `expressMatch`.`user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `expressMatch`.`user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `nick` VARCHAR(50) NOT NULL ,
  `pass` VARCHAR(500) NOT NULL ,
  `enabled` TINYINT(1) NOT NULL ,
  `insert_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `nick` (`nick` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `expressMatch`.`authorities`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `expressMatch`.`authorities` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `user_id` INT(11) NOT NULL ,
  `authority` VARCHAR(50) NOT NULL ,
  `insert_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) ,
  INDEX `user_id` (`user_id` ASC) ,
  CONSTRAINT `authorities_ibfk_1`
    FOREIGN KEY (`user_id` )
    REFERENCES `expressMatch`.`user` (`id` ))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `expressMatch`.`expression_type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `expressMatch`.`expression_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NULL ,
  `description` VARCHAR(512) NULL ,
  `label` VARCHAR(1024) NULL ,
  `model_expression_id` INT(11) NULL ,
  `insert_date` TIMESTAMP NOT NULL DEFAULT 'CURRENT_TIMESTAMP' ,
  PRIMARY KEY (`id`) ,
  INDEX `model_expression_fk_idx` (`model_expression_id` ASC) ,
  CONSTRAINT `model_expression_fk`
    FOREIGN KEY (`model_expression_id` )
    REFERENCES `expressMatch`.`expression` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expressMatch`.`institution`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `expressMatch`.`institution` (
  `id` INT(8) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(256) NOT NULL ,
  `acronym` VARCHAR(45) NULL ,
  `nationality` VARCHAR(45) NOT NULL ,
  `insert_date` TIMESTAMP NOT NULL DEFAULT 'CURRENT_TIMESTAMP' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expressMatch`.`user_info`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `expressMatch`.`user_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(256) NOT NULL ,
  `instituition_id` INT(8) NOT NULL ,
  `nationaity` VARCHAR(45) NULL ,
  `insert_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) ,
  INDEX `instituition_fk_idx` (`instituition_id` ASC) ,
  CONSTRAINT `user_fk`
    FOREIGN KEY (`id` )
    REFERENCES `expressMatch`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `instituition_fk`
    FOREIGN KEY (`instituition_id` )
    REFERENCES `expressMatch`.`institution` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expressMatch`.`expression`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `expressMatch`.`expression` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `label` VARCHAR(1024) NULL ,
  `expression_type_id` INT(11) NULL ,
  `user_info_id` INT(11) NULL ,
  `insert_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) ,
  INDEX `expression_type_fk_idx` (`expression_type_id` ASC) ,
  INDEX `user_info_fk_idx` (`user_info_id` ASC) ,
  CONSTRAINT `expression_type_fk`
    FOREIGN KEY (`expression_type_id` )
    REFERENCES `expressMatch`.`expression_type` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `user_info_fk`
    FOREIGN KEY (`user_info_id` )
    REFERENCES `expressMatch`.`user_info` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expressMatch`.`symbol`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `expressMatch`.`symbol` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `label` VARCHAR(40) NULL ,
  `expression_id` INT(11) NOT NULL ,
  `insert_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) ,
  INDEX `expression_symbol_fk_idx` (`expression_id` ASC) ,
  CONSTRAINT `expression_symbol_fk`
    FOREIGN KEY (`expression_id` )
    REFERENCES `expressMatch`.`expression` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expressMatch`.`stroke`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `expressMatch`.`stroke` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `lt_point` FLOAT NULL ,
  `rb_point` FLOAT NULL ,
  `symbol_id` INT(11) NOT NULL ,
  `insert_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) ,
  INDEX `symbol_stroke_fk_idx` (`symbol_id` ASC) ,
  CONSTRAINT `symbol_stroke_fk`
    FOREIGN KEY (`symbol_id` )
    REFERENCES `expressMatch`.`symbol` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `expressMatch`.`point`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `expressMatch`.`point` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `x` FLOAT NOT NULL ,
  `y` FLOAT NOT NULL ,
  `time` TIMESTAMP NULL ,
  `id_stroke` INT(11) NOT NULL ,
  `insert_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) ,
  INDEX `stroke_point_fk_idx` (`id_stroke` ASC) ,
  CONSTRAINT `stroke_point_fk`
    FOREIGN KEY (`id_stroke` )
    REFERENCES `expressMatch`.`stroke` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `expressMatch` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
