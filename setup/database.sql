drop table users;
CREATE TABLE users(

	username varchar NOT NULL,
	"password" varchar NOT NULL,
	userType varchar NULL,
    "name" varchar NULL,
    phone varchar NULL,
	address varchar NULL	
);


insert into users (username, "password", userType, "name",phone,address ) 
VALUES ('ab@gmail.com', '22222', 'S','mariam','0470158812','12Elizabth');


select *from users

