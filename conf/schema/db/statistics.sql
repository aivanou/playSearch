CREATE DATABASE statisticDB
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

USE statisticDB;

CREATE TABLE IF NOT EXISTS queriesTable (
  id    BIGINT       NOT NULL AUTO_INCREMENT,
  query VARCHAR(200) NOT NULL,
  date  BIGINT       NOT NULL,
  PRIMARY KEY (id)
)
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS doubleRequests (
  id    BIGINT       NOT NULL AUTO_INCREMENT,
  query VARCHAR(200) NOT NULL,
  executedTimes INTEGER       NOT NULL,
  firstQueryTime INTEGER       NOT NULL,
  secondQueryTime INTEGER       NOT NULL,
  firstQuerySize INTEGER       NOT NULL,
  secondQuerySize INTEGER       NOT NULL,
  PRIMARY KEY (id)
)
  CHARACTER SET utf8
  COLLATE utf8_general_ci
  ENGINE = InnoDB;


CREATE INDEX date_index ON queriesTable (date) USING BTREE;
