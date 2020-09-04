CREATE TABLE public.hours
(
  hours_id         serial    NOT NULL,
  creator_username varchar   NOT NULL,
  worker_username  varchar   NOT NULL,
  start_date_time  timestamp NOT NULL,
  end_date_time    timestamp NOT NULL
);
