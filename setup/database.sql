SELECT *
FROM pg_catalog.pg_tables
WHERE schemaname != 'pg_catalog' AND 
    schemaname != 'information_schema';
	


drop table IF EXISTS users;
drop table IF EXISTS bookings;
drop table IF EXISTS public.hours;

CREATE TABLE users(
	username varchar NOT NULL,
	"password" varchar NOT NULL,
	userType varchar NULL,
    "name" varchar NULL,
    phone varchar NULL,
	address varchar NULL
);


CREATE TABLE bookings (
	booking_id serial NOT NULL,
	customer_username varchar NOT NULL,
	worker_username varchar NOT NULL,
	start_date_time timestamp NOT NULL,
	end_date_time timestamp NOT NULL,
	CONSTRAINT bookings_pkey PRIMARY KEY (booking_id)
);


CREATE TABLE public.hours
(
  hours_id        serial    NOT NULL,
  customer_username varchar   NOT NULL,
  worker_username   varchar   NOT NULL,
  start_time        timestamp NOT NULL,
  end_time          timestamp NOT NULL
);

insert into users (username, "password", userType, "name",phone,address ) 
VALUES ('ab@gmail.com', '22222', 'S','mariam','0470157714','12 Elizabth');

insert into bookings (customer_username, worker_username, start_date_time, end_date_time ) 
VALUES ('john@gmail.com', 'jerry@gmail.com','2020-01-01 00:00:00','2020-01-01 23:59:59');

insert into public.hours (customer_username, worker_username, start_time, end_time ) 
VALUES ('john@gmail.com', 'jerry@gmail.com','2020-01-01 00:00:00','2020-01-03 23:59:59');

select * from bookings;
