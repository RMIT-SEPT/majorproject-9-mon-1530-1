CREATE TABLE users
(
  username   varchar      NOT NULL,
  "password" varchar      NOT NULL,
  userType   varchar      NULL,
  "name"     varchar      NULL,
  phone      varchar      NULL,
  address    varchar      NULL,
  "token"    varchar(255) null
);

INSERT INTO public.users
  (username, "password", usertype, "name", phone, address, "token")
VALUES ('ValidUser', 'test password', 'User', 'Test name', 'Test phone', 'Test address', 'ValidUserToken');

INSERT INTO public.users
  (username, "password", usertype, "name", phone, address, "token")
VALUES ('ValidAdmin', 'test password', 'Admin', 'Test name', 'Test phone', 'Test address', 'ValidAdminToken');

