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

