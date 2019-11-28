# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

-- init script create procs
-- Inital script to create stored procedures etc for mysql platform
DROP PROCEDURE IF EXISTS usp_ebean_drop_foreign_keys;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_foreign_keys TABLE, COLUMN
-- deletes all constraints and foreign keys referring to TABLE.COLUMN
--
CREATE PROCEDURE usp_ebean_drop_foreign_keys(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE c_fk_name CHAR(255);
  DECLARE curs CURSOR FOR SELECT CONSTRAINT_NAME from information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE() and TABLE_NAME = p_table_name and COLUMN_NAME = p_column_name
      AND REFERENCED_TABLE_NAME IS NOT NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN curs;

  read_loop: LOOP
    FETCH curs INTO c_fk_name;
    IF done THEN
      LEAVE read_loop;
    END IF;
    SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP FOREIGN KEY ', c_fk_name);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
  END LOOP;

  CLOSE curs;
END
$$

DROP PROCEDURE IF EXISTS usp_ebean_drop_column;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_column TABLE, COLUMN
-- deletes the column and ensures that all indices and constraints are dropped first
--
CREATE PROCEDURE usp_ebean_drop_column(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  CALL usp_ebean_drop_foreign_keys(p_table_name, p_column_name);
  SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP COLUMN ', p_column_name);
  PREPARE stmt FROM @sql;
  EXECUTE stmt;
END
$$
create table tblattendance (
  idattendance                  bigint auto_increment not null,
  checkindt                     datetime(6),
  checkoutdt                    datetime(6),
  employeeid                    bigint not null,
  constraint pk_tblattendance primary key (idattendance)
);

create table tblemployee (
  id                            bigint auto_increment not null,
  email                         varchar(255),
  password                      varchar(255),
  firstname                     varchar(255),
  lastname                      varchar(255),
  role                          bigint,
  constraint uq_tblemployee_role unique (role),
  constraint pk_tblemployee primary key (id)
);

create table role (
  roleid                        bigint auto_increment not null,
  role                          varchar(255),
  roledesc                      varchar(255),
  constraint pk_role primary key (roleid)
);

create index ix_tblattendance_employeeid on tblattendance (employeeid);
alter table tblattendance add constraint fk_tblattendance_employeeid foreign key (employeeid) references tblemployee (id) on delete restrict on update restrict;

alter table tblemployee add constraint fk_tblemployee_role foreign key (role) references role (roleid) on delete restrict on update restrict;


# --- !Downs

alter table tblattendance drop foreign key fk_tblattendance_employeeid;
drop index ix_tblattendance_employeeid on tblattendance;

alter table tblemployee drop foreign key fk_tblemployee_role;

drop table if exists tblattendance;

drop table if exists tblemployee;

drop table if exists role;

