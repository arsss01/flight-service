create sequence flights_id_seq;
create table if not exists flights
(
    id          integer primary key,
    origin      varchar(255),
    destination varchar(255),
    departure   timestamp,
    arrival     timestamp,
    status      varchar(255)
);