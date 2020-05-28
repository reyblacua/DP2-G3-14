-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner2','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner2','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner3','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner3','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner4','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner4','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner5','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner5','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner6','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner6','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner7','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner7','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner8','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner8','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner9','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner9','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner10','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner10','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner11','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner11','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner12','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner12','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner13','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner13','owner');

-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');

INSERT INTO vets (ID,FIRST_NAME,LAST_NAME) VALUES (1, 'James', 'Carter');
INSERT INTO vets (ID,FIRST_NAME,LAST_NAME) VALUES (2, 'Helen', 'Leary');
INSERT INTO vets (ID,FIRST_NAME,LAST_NAME) VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets (ID,FIRST_NAME,LAST_NAME) VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets (ID,FIRST_NAME,LAST_NAME) VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets (ID,FIRST_NAME,LAST_NAME) VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties (ID,NAME) VALUES (1, 'radiology');
INSERT INTO specialties (ID,NAME) VALUES (2, 'surgery');
INSERT INTO specialties (ID,NAME) VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison',false,true,false,true, '6085551023', 'owner1');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie',true,false,false,true, '6085551749', 'owner2');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland',true,true,true,true, '6085558763', 'owner3');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor',false,true,true,false,'6085558763','owner4');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison',true,true,true,true, '6085552765', 'owner5');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona',false,true,true,true, '6085552654', 'owner6');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona',true,true,true,true, '6085555387', 'owner7');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison',false,true,true,true, '6085557683', 'owner8');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison',true,true,true,true, '6085559435', 'owner9');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee',true,true,false,true, '6085555487', 'owner10');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (11, 'Carla', 'Blanco', '115 Prime St.', 'Puerto Rico',true,false,false,true, '1285555487', 'owner11');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (12, 'Julia', 'Martin', '116 Prime St.', 'Puerto Rico',true,true,true,true, '3454565424', 'owner12');
INSERT INTO owners (ID,FIRST_NAME,LAST_NAME,ADDRESS,CITY,DANGEROUS_ANIMAL,LIVES_IN_CITY,NUMEROUS_ANIMAL,POSITIVE_HISTORY,TELEPHONE,USERNAME) VALUES (13, 'Ruben', 'Doblas', '42 St. Florencia', 'Rubencio',false,false,false,true, '6087555434', 'owner13');

INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07',false,true, 1, 1);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06',false,false, 1, 2);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17',true,true, 2, 3);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07',false,false, 2, 3);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30',false,true, 1, 4);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (6, 'George', '2010-01-20',false,true, 1, 5);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04',false,false, 1, 6);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (8, 'Max', '2012-09-04',false,true, 1, 6);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06',false,true, 2, 7);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24',false,true, 2, 8);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09',false,true, 1, 9);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24',true,true, 2, 10);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08',false,true, 1, 10);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (14, 'Poppy', '2013-02-01',true,true, 1, 11);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (15, 'Fuffy', '2013-02-01',false,true, 1, 10);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (16, 'Siri', '2013-02-01',false,true, 2, 11);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (17, 'Lulu', '2013-02-01',false,true, 3, 11);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (18, 'Caty', '2013-02-01',false,true, 4, 11);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (19, 'Lili', '2013-02-01',false,true, 5, 11);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (20, 'Subaru', '2020-01-08',false,true, 1, 13);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (21, 'Masato', '2020-01-08',false,true, 1, 13);
INSERT INTO pets(id,name,birth_date,dangerous,is_vaccinated,type_id,owner_id) VALUES (22, 'Satoshi', '2020-01-08',false,true, 1, 13);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

INSERT INTO hairdressers(id,first_name,last_name,specialties,active) VALUES (1, 'George', 'Primero', 1, true);
INSERT INTO hairdressers(id,first_name,last_name,specialties,active) VALUES (2, 'George', 'Segundo', 2, true);
INSERT INTO hairdressers(id,first_name,last_name,specialties,active) VALUES (3, 'George', 'Tercero', 3, true);
INSERT INTO hairdressers(id,first_name,last_name,specialties,active) VALUES (4, 'George', 'Cuarto', 4, false);
INSERT INTO hairdressers(id,first_name,last_name,specialties,active) VALUES (5, 'George', 'Quinto', 5, true);
INSERT INTO hairdressers(id,first_name,last_name,specialties,active) VALUES (6, 'George', 'Sexto', 6, false);

INSERT INTO payments(id,name,amount,pay_date) VALUES (1, 'Pago1', 200.00, '2020-07-24' );
INSERT INTO payments(id,name,amount,pay_date) VALUES (2, 'Pago2', 25.00 , '2020-07-25' );
INSERT INTO payments(id,name,amount,pay_date) VALUES (3, 'Pago3', 25.00 , '2020-07-26' );
INSERT INTO payments(id,name,amount,pay_date) VALUES (4, 'Pago4', 1050.4 , '2020-07-27' );
INSERT INTO payments(id,name,amount,pay_date) VALUES (5, 'Pago5', 1150 , '2020-07-28' );
INSERT INTO payments(id,name,amount,pay_date) VALUES (6, 'Pago6', 1250.6 , '2020-07-29' );
INSERT INTO payments(id,name,amount,pay_date) VALUES (7, 'Pago7', 200 , '2020-07-29' );
INSERT INTO payments(id,name,amount,pay_date) VALUES (8, 'Pago8', 200 , '2020-07-29' );

INSERT INTO appointments(id,name,description,datetime,is_paid,hairdresser_id,pet_id,payment_id,owner_id) VALUES (1,'Cita1','Cita para Leo','2020-07-20 20:50', true, 1, 1, 1, 1);
INSERT INTO appointments(id,name,description,datetime,is_paid,hairdresser_id,pet_id,payment_id,owner_id) VALUES (2,'Cita2','Cita para Basil','2020-07-03 18:35', true, 2, 2, 2, 2);
INSERT INTO appointments(id,name,description,datetime,is_paid,hairdresser_id,pet_id,payment_id,owner_id) VALUES (3,'Cita3','Cita para Rosy','2020-07-04 19:36', true, 3, 3, 3, 3);
INSERT INTO appointments(id,name,description,datetime,is_paid,hairdresser_id,pet_id,owner_id) VALUES (4,'Cita4','Cita para Jewel','2020-07-05 20:37', false, 4, 4, 3);
INSERT INTO appointments(id,name,description,datetime,is_paid,hairdresser_id,pet_id,owner_id) VALUES (5,'Cita5','Cita para Iggy','2020-07-06 21:38', false, 5, 5, 4);
INSERT INTO appointments(id,name,description,datetime,is_paid,hairdresser_id,pet_id,payment_id,owner_id) VALUES (6,'Cita6','Cita para George','2020-07-20 10:39', true, 6, 6, 6, 5);

INSERT INTO announcement(ID,NAME,CAN_BE_ADOPTED,DESCRIPTION,PET_NAME,OWNER_ID,TYPE_ID) VALUES (1,'Anuncio1',true,'Hola','Fox',1,1);
INSERT INTO announcement(ID,NAME,CAN_BE_ADOPTED,DESCRIPTION,PET_NAME,OWNER_ID,TYPE_ID) VALUES (2,'Anuncio2',true,'Hola','Fufu',2,3);
INSERT INTO announcement(ID,NAME,CAN_BE_ADOPTED,DESCRIPTION,PET_NAME,OWNER_ID,TYPE_ID) VALUES (3,'Anuncio3',false,'Hola','Lulu',3,2);

INSERT INTO answer(ID,NAME,DATE,DESCRIPTION,ANNOUNCEMENT_ID,OWNER_ID) VALUES (1,'Respuesta1','2010-03-09','Hola',1,2);
INSERT INTO answer(ID,NAME,DATE,DESCRIPTION,ANNOUNCEMENT_ID,OWNER_ID) VALUES (2,'Respuesta2','2010-03-09','Hola',1,3);
INSERT INTO answer(ID,NAME,DATE,DESCRIPTION,ANNOUNCEMENT_ID,OWNER_ID) VALUES (3,'Respuesta3','2010-03-09','Hola',2,1);

INSERT INTO trainer(ID,NAME) VALUES (1,'Adolfo Fernandez Ruiz');
INSERT INTO trainer(ID,NAME) VALUES (2,'Laura Rio Caballero');
INSERT INTO trainer(ID,NAME) VALUES (3,'Pedro Suarez García');

INSERT INTO course(ID,NAME,DANGEROUS_ALLOWED,FINISH_DATE,START_DATE,CAPACITY,PET_TYPE_ID,TRAINER_ID,COST) VALUES (1,'Curso para gatos',false,'2020-07-03','2020-06-03',20,1,1,200);
INSERT INTO course(ID,NAME,DANGEROUS_ALLOWED,FINISH_DATE,START_DATE,CAPACITY,PET_TYPE_ID,TRAINER_ID,COST) VALUES (2,'Curso para perros',false,'2020-07-03','2020-06-03',20,2,2,150);
INSERT INTO course(ID,NAME,DANGEROUS_ALLOWED,FINISH_DATE,START_DATE,CAPACITY,PET_TYPE_ID,TRAINER_ID,COST) VALUES (3,'Curso para perros peligrosos',true,'2020-07-03','2020-06-03',10,2,3,250);
INSERT INTO course(ID,NAME,DANGEROUS_ALLOWED,FINISH_DATE,START_DATE,CAPACITY,PET_TYPE_ID,TRAINER_ID,COST) VALUES (4,'Curso para gatos peligrosos',true,'2020-07-03','2020-06-03',10,1,3,300);
INSERT INTO course(ID,NAME,DANGEROUS_ALLOWED,FINISH_DATE,START_DATE,CAPACITY,PET_TYPE_ID,TRAINER_ID,COST) VALUES (5,'Curso super guay para gatos',false,'2020-07-03','2020-06-03',2,1,1,200);

INSERT INTO inscription(ID,NAME,DATE,IS_PAID,COURSE_ID,OWNER_ID,PAYMENT_ID,PET_ID) VALUES(1,'Inscription1','2020-05-03',true, 1,9,1,11);
INSERT INTO inscription(ID,NAME,DATE,IS_PAID,COURSE_ID,OWNER_ID,PAYMENT_ID,PET_ID) VALUES(2,'Inscription2','2020-05-03',true, 2,3,2,3);
INSERT INTO inscription(ID,NAME,DATE,IS_PAID,COURSE_ID,OWNER_ID,PAYMENT_ID,PET_ID) VALUES(3,'Inscription3','2020-05-03',false, 3,10,null,12);
INSERT INTO inscription(ID,NAME,DATE,IS_PAID,COURSE_ID,OWNER_ID,PAYMENT_ID,PET_ID) VALUES(4,'Inscription4','2020-05-03',true, 5,10,7,13);
INSERT INTO inscription(ID,NAME,DATE,IS_PAID,COURSE_ID,OWNER_ID,PAYMENT_ID,PET_ID) VALUES(5,'Inscription5','2020-05-03',true, 5,6,8,8);
INSERT INTO inscription(ID,NAME,DATE,IS_PAID,COURSE_ID,OWNER_ID,PAYMENT_ID,PET_ID) VALUES(6,'Inscription6','2020-05-03',true, 1,1,3,1);


