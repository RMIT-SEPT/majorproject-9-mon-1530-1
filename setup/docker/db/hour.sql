drop table IF EXISTS public.hours;

CREATE TABLE public.hours
(
  hours_id        serial    NOT NULL,
  customer_username varchar   NOT NULL,
  worker_username   varchar   NOT NULL,
  start_time        timestamp NOT NULL,
  end_time          timestamp NOT NULL
);


insert into public.hours (customer_username, worker_username, start_time, end_time ) 
VALUES ('john@gmail.com', 'jerry@gmail.com','2020-01-01 00:00:00','2020-01-03 23:59:59');