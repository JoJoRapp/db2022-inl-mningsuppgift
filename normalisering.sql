use iths;

DROP TABLE IF EXISTS UNF;

CREATE TABLE `UNF` (
    `Id` DECIMAL(38, 0) NOT NULL,
    `Name` VARCHAR(26) NOT NULL,
    `Grade` VARCHAR(11) NOT NULL,
    `Hobbies` VARCHAR(25),
    `City` VARCHAR(10) NOT NULL,
    `School` VARCHAR(30) NOT NULL,
    `HomePhone` VARCHAR(15),
    `JobPhone` VARCHAR(15),
    `MobilePhone1` VARCHAR(15),
    `MobilePhone2` VARCHAR(15)
)  ENGINE=INNODB;

LOAD DATA INFILE '/var/lib/mysql-files/denormalized-data.csv'
INTO TABLE UNF
CHARACTER SET latin1
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;


/* Normalisera Student */

drop table if exists Student;

create table Student (
	StudentId int not null auto_increment,
	Name varchar(255) not null,
	constraint primary key (StudentId)
) engine=innodb;

insert into Student (StudentId, Name)
select distinct Id, Name from UNF;


/* Normalisera School */

drop table if exists School, StudentSchool;

create table School (
	SchoolId int not null auto_increment,
	Name varchar(255) not null,
	City varchar(255) not null,
	constraint primary key (SchoolId)
);

insert into School (Name, City)
select distinct School, City from UNF;

create table StudentSchool as
select Id as StudentId, SchoolId from UNF join School on UNF.School = School.Name;


/* Normalisera Phones */

drop table if exists Phone;
create table Phone (
	PhoneId int not null auto_increment,
	StudentId int not null,
	IsHome tinyint not null default 0,
	IsJob tinyint not null default 0,
	IsMobile tinyint not null default 0,
	Number varchar(255) not null,
	constraint primary key(PhoneId)
);

insert into Phone (StudentId, IsHome, IsJob, IsMobile, Number)
select Id, true, false, false, HomePhone from UNF
where HomePhone is not null and HomePhone != ''
union select Id, false, true, false, JobPhone from UNF
where JobPhone is not null and JobPhone != ''
union select Id, false, false, true, MobilePhone1 from UNF
where MobilePhone1 is not null and MobilePhone1 != ''
union select Id, false, false, true, MobilePhone2 from UNF
where MobilePhone2 is not null and MobilePhone2 != '';


/* Normalisera Hobbies */

drop table if exists Hobby;
create table Hobby (
	HobbyId int not null auto_increment,
	Name varchar(255) not null,
	constraint primary key (HobbyId)
);

drop view if exists HobbiesTemp;
create view HobbiesTemp as
select Id as StudentId, trim(substring_index(Hobbies, ",", 1)) as Hobby from UNF
where Hobbies != ""
union select Id as StudentId, trim(substring_index(substring_index(Hobbies, ",", -2), ",", 1)) from UNF
where Hobbies != ""
union select Id as StudentId, trim(substring_index(Hobbies, ",", -1)) from UNF
where Hobbies != "";

insert into Hobby(Name)
select distinct Hobby from HobbiesTemp;

drop table if exists StudentHobby;
create table StudentHobby as
select distinct StudentId, HobbyId from HobbiesTemp join Hobby on HobbiesTemp.Hobby = Hobby.Name;


/* Normalisera Grade */

drop table if exists Grade;
create table Grade (
	GradeId int not null auto_increment,
	Name varchar(255) not null,
	constraint primary key (GradeId)
);

insert into Grade (Name)
select distinct Grade from UNF;

alter table Student add column GradeId int not null;

update Student join UNF on (StudentId = Id) join Grade on Grade.Name = UNF.Grade
set Student.GradeId = Grade.GradeId;
