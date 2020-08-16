CREATE TABLE bookings
(
    booking_id serial primary key,
    customer_id character varying  NOT NULL,
    worker_id character varying  NOT NULL,
    start_time timestamp NOT NULL,
    end_time timestamp NOT NULL
);