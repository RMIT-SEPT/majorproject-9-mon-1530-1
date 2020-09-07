drop table IF EXISTS bookings;

CREATE TABLE bookings (
                        booking_id serial NOT NULL,
                        customer_username varchar NOT NULL,
                        worker_username varchar NOT NULL,
                        start_date_time timestamp NOT NULL,
                        end_date_time timestamp NOT NULL,
                        CONSTRAINT bookings_pkey PRIMARY KEY (booking_id)
);

insert into bookings (customer_username, worker_username, start_date_time, end_date_time )
VALUES ('john@gmail.com', 'jerry@gmail.com','2020-01-01 00:00:00','2020-01-01 23:59:59');


insert into bookings (customer_username, worker_username, start_date_time, end_date_time )
VALUES ('john@gmail.com', 'jerry@gmail.com','2020-11-02 00:00:00','2020-11-03 23:59:59');