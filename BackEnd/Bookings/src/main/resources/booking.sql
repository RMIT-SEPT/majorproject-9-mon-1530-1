CREATE TABLE bookings (
	booking_id serial NOT NULL,
	customer_username varchar NOT NULL,
	worker_username varchar NOT NULL,
	start_date_time timestamp NOT NULL,
	end_date_time timestamp NOT NULL,
	CONSTRAINT bookings_pkey PRIMARY KEY (booking_id)
);
