drop table IF EXISTS users;

CREATE TABLE users(
	username varchar NOT NULL,
	"password" varchar NOT NULL,
	userType varchar NULL,
    "name" varchar NULL,
    phone varchar NULL,
	address varchar NULL	
);


insert into users (username, "password", userType, "name",phone,address ) 
VALUES ('s1@gmail.com', 's1', 'S','Jerry','0412345678','1 Super st');

insert into users (username, "password", userType, "name",phone,address ) 
VALUES ('w1@gmail.com', 'w1', 'W','John','0412345678','1 Worker St');

insert into users (username, "password", userType, "name",phone,address ) 
VALUES ('c1@gmail.com', 'c1', 'C','Merry','0412345678','1 Customer St');

insert into users (username, "password", userType, "name",phone,address ) 
VALUES ('kath123', 'tempPass1', 'Worker','Kathreen McDonald','0412345678','11 North Street, Suburb, Melbourne 3000');

insert into users (username, "password", userType, "name",phone,address ) 
VALUES ('markTheMark', 'tempPass1', 'Worker','Mark Falley','0456123115','11 South Street, Suburb, Melbourne 3000');

insert into users (username, "password", userType, "name",phone,address ) 
VALUES ('realSarahX', 'tempPass1', 'Worker','Sarah Mickey','0475832784','11 West Street, Suburb, Melbourne 3000');

insert into users (username, "password", userType, "name",phone,address ) 
VALUES ('JohnLeLemon', 'tempPass1', 'Worker','John Lim Le','0472731414','11 East Street, Suburb, Melbourne 3000');

insert into users (username, "password", userType, "name",phone,address ) 
VALUES ('lizatawaf', 'tempPass5', 'Admin','Liza Tawaf','0455213414','21 Admin Street, Suburb, Melbourne 3000');
