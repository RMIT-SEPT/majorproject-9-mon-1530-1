FROM postgres:13
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgres
ENV POSTGRES_DB sept
ADD user.sql /docker-entrypoint-initdb.d/
ADD booking.sql /docker-entrypoint-initdb.d/