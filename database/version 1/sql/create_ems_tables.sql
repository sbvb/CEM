-- create_ems_tables.sql
-- Author: Vinícius Heineck dos Santos
-- Date: 2011/06/09
-- -------------------------------------------
-- PROMPTT 'EMS Tables creation'

CREATE TABLE tb_ambiance
(
	ambiance_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	max_students  INTEGER UNSIGNED NOT NULL,
	name  VARCHAR(100) NOT NULL,
	school_id  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (ambiance_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_ccdt
(
	id_ccdt  INTEGER UNSIGNED auto_increment NOT NULL,
	event_comments  VARCHAR(200) NULL,
	dt_id  INTEGER UNSIGNED NOT NULL,
	class_course_id  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (id_ccdt)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_ccdta
(
	id_ccdta INTEGER UNSIGNED auto_increment NOT NULL,
	ambiance_id  INTEGER UNSIGNED NOT NULL,
	id_ccdt  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (id_ccdta)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_class
(
	class_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	school_id  INTEGER UNSIGNED NOT NULL,
	program_id  INTEGER UNSIGNED NOT NULL,
	name  VARCHAR(100) NOT NULL,
	PRIMARY KEY (class_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_class_course
(
	class_course_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	finished  boolean NOT NULL,
	person_id  INTEGER UNSIGNED NULL,
	class_id  INTEGER UNSIGNED NOT NULL,
	schedule_id  INTEGER UNSIGNED NOT NULL,
	course_id  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (class_course_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_contract
(
	id_contract  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	name  VARCHAR(50) NOT NULL,
	school_id  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (id_contract)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_course
(
	course_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	name  VARCHAR(100) NOT NULL,
	school_id  INTEGER UNSIGNED NOT NULL,	
	code_name  VARCHAR(20) NOT NULL,
	PRIMARY KEY (course_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_datetime
(
	dt_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	date  DATE NOT NULL,
	time_begin  TIME NOT NULL,
	time_end  TIME NOT NULL,
	schedule_id  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (dt_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_enrollment
(
	enrollment_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	payment_ok  BOOLEAN NOT NULL,
	school_id  INTEGER UNSIGNED NOT NULL,
	person_id  INTEGER UNSIGNED NOT NULL,
	class_id  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (enrollment_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_file
(
	id_file  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	description  VARCHAR(200) NULL,
	permission  VARCHAR(20) NOT NULL,
	empty_for_user  TINYINT NOT NULL,
	id_formats  INTEGER UNSIGNED NOT NULL,
	person_id  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (id_file)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_formats
(
	id_formats  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	name  VARCHAR(20) NOT NULL,
	extension  VARCHAR(20) NOT NULL,
	 PRIMARY KEY (id_formats)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_globals
(
	globals_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	grade_min  DECIMAL(3,1) NOT NULL,
	PRIMARY KEY (globals_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_grade
(
	grade_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	grade  DECIMAL(3,1) NOT NULL,
	person_id  INTEGER UNSIGNED NOT NULL,
	class_course_id  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (grade_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_log
(
	log_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	username  VARCHAR(50) NOT NULL,
	ip  VARCHAR(20) NOT NULL,
	event  VARCHAR(100) NOT NULL,
	curtime  DATETIME NOT NULL,
	PRIMARY KEY (log_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_person
(
	person_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	name  VARCHAR(100) NOT NULL,
	email  VARCHAR(100) NOT NULL,
	telephone  VARCHAR(30) NOT NULL,
	nickname  VARCHAR(100) NOT NULL,
	school_id  INTEGER UNSIGNED NULL,
	person_type_id  INTEGER UNSIGNED NULL,
	obs  VARCHAR(200) NULL,
	user_id  VARCHAR(50) NULL,
	reg_type  VARCHAR(20) NULL,
	date  DATETIME NULL,
	program_id  INTEGER UNSIGNED NULL,
	address  VARCHAR(100) NULL,
	district  VARCHAR(50) NULL,
	city  VARCHAR(50) NULL,
	state  VARCHAR(30) NULL,
	zip_code  VARCHAR(20) NULL,
	career  VARCHAR(50) NULL,
	civil_status  VARCHAR(20) NULL,
	identity  VARCHAR(20) NULL,
	issued_by  VARCHAR(20) NULL,
	cpf  VARCHAR(20) NULL,
	desc_quota VARCHAR(30) NULL,
	desc_value VARCHAR(30) NULL,
	PRIMARY KEY (person_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_person_present
(
	id_person_present  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	person_id  INTEGER UNSIGNED NOT NULL,
	id_ccdt  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (id_person_present)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_person_type
(
	person_type_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	name  VARCHAR(20) NOT NULL,
	PRIMARY KEY (person_type_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_program
(
	program_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	name  VARCHAR(100) NOT NULL,
	value  INTEGER UNSIGNED NOT NULL,
	description  VARCHAR(200) NOT NULL,
	school_id  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (program_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_program_course
(
	program_course_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	program_id  INTEGER UNSIGNED NOT NULL,
	course_id  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (program_course_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_schedule
(
	schedule_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	name  VARCHAR(100) NOT NULL,
	school_id  INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (schedule_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE tb_school
(
	school_id  INTEGER UNSIGNED AUTO_INCREMENT NOT NULL,
	name  VARCHAR(100) NOT NULL,
	PRIMARY KEY (school_id)
)
ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;

ALTER TABLE tb_ambiance
	ADD FOREIGN KEY R_20 (school_id) REFERENCES tb_school(school_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tb_ccdt
	ADD FOREIGN KEY R_60 (dt_id) REFERENCES tb_datetime(dt_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_61 (class_course_id) REFERENCES tb_class_course(class_course_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_ccdta
	ADD FOREIGN KEY R_58 (ambiance_id) REFERENCES tb_ambiance(ambiance_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_62 (id_ccdt) REFERENCES tb_ccdt(id_ccdt) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_class
	ADD FOREIGN KEY R_19 (school_id) REFERENCES tb_school(school_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_01 (program_id) REFERENCES tb_program(program_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_class_course
	ADD FOREIGN KEY R_02 (class_id) REFERENCES tb_class(class_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_16 (schedule_id) REFERENCES tb_schedule(schedule_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_06 (course_id) REFERENCES tb_course(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_08 (person_id) REFERENCES tb_person(person_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_contract
	ADD FOREIGN KEY R_66 (school_id) REFERENCES tb_school(school_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_course
	ADD FOREIGN KEY R_18 (school_id) REFERENCES tb_school(school_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_datetime
	ADD FOREIGN KEY R_15 (schedule_id) REFERENCES tb_schedule(schedule_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_enrollment
	ADD FOREIGN KEY R_21 (school_id) REFERENCES tb_school(school_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_11 (person_id) REFERENCES tb_person(person_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_03 (class_id) REFERENCES tb_class(class_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_file
	ADD FOREIGN KEY R_64 (id_formats) REFERENCES tb_formats(id_formats) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_65 (person_id) REFERENCES tb_person(person_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_grade
	ADD FOREIGN KEY R_09 (person_id) REFERENCES tb_person(person_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_04 (class_course_id) REFERENCES tb_class_course(class_course_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_person
	ADD FOREIGN KEY R_22 (school_id) REFERENCES tb_school(school_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_12 (person_type_id) REFERENCES tb_person_type(person_type_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_67 (program_id) REFERENCES tb_program(program_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_person_present
	ADD FOREIGN KEY R_10 (person_id) REFERENCES tb_person(person_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_63 (id_ccdt) REFERENCES tb_ccdt(id_ccdt) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_program
	ADD FOREIGN KEY R_17 (school_id) REFERENCES tb_school(school_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_program_course
	ADD FOREIGN KEY R_13 (program_id) REFERENCES tb_program(program_id) ON DELETE CASCADE ON UPDATE CASCADE,
	ADD FOREIGN KEY R_05 (course_id) REFERENCES tb_course(course_id) ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE tb_schedule
	ADD FOREIGN KEY R_23 (school_id) REFERENCES tb_school(school_id) ON DELETE CASCADE ON UPDATE CASCADE
;

-- PROMPTT 'EMS Tables created succesfull'
-- PROMPTT mysql>