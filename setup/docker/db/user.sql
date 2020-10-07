drop table IF EXISTS users;

CREATE TABLE public.users
(
  username   varchar      NOT NULL,
  "password" varchar      NOT NULL,
  usertype   varchar      NULL,
  "name"     varchar      NULL,
  phone      varchar      NULL,
  address    varchar      NULL,
  "token"    varchar(255) NULL
);


insert into users (username, "password", userType, "name", phone, address, "token")
VALUES ('s1@gmail.com', 's1', 'S', 'Jerry', '0412345678', '1 Super st', null);

insert into users (username, "password", userType, "name", phone, address, "token")
VALUES ('w1@gmail.com', 'w1', 'W', 'John', '0412345678', '1 Worker St', null);

insert into users (username, "password", userType, "name", phone, address, "token")
VALUES ('c1@gmail.com', 'c1', 'C', 'Merry', '0412345678', '1 Customer St', null);

insert into users (username, "password", userType, "name", phone, address, "token")
VALUES ('kath123', 'tempPass1', 'Worker', 'Kathreen McDonald', '0412345678', '11 North Street, Suburb, Melbourne 3000', null);

insert into users (username, "password", userType, "name", phone, address, "token")
VALUES ('markTheMark', 'tempPass1', 'Worker', 'Mark Falley', '0456123115', '11 South Street, Suburb, Melbourne 3000', null);

insert into users (username, "password", userType, "name", phone, address, "token")
VALUES ('realSarahX', 'tempPass1', 'Worker', 'Sarah Mickey', '0475832784', '11 West Street, Suburb, Melbourne 3000', null);

insert into users (username, "password", userType, "name", phone, address, "token")
VALUES ('JohnLeLemon', 'tempPass1', 'Worker', 'John Lim Le', '0472731414', '11 East Street, Suburb, Melbourne 3000', null);

insert into users (username, "password", userType, "name", phone, address, "token")
VALUES ('lizatawaf', 'tempPass5', 'Admin', 'Liza Tawaf', '0455213414', '21 Admin Street, Suburb, Melbourne 3000', null);
