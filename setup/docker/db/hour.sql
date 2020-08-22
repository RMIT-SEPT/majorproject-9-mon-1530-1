CREATE TABLE public.hours
(
  hours_id        serial    NOT NULL,
  customer_username varchar   NOT NULL,
  worker_username   varchar   NOT NULL,
  start_time        timestamp NOT NULL,
  end_time          timestamp NOT NULL
);
