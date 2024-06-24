create sequence flights_id_seq;
create table if not exists flights
(
    id          integer primary key,
    origin      varchar(255),
    destination varchar(255),
    departure   timestamp without time zone,
    arrival     timestamp without time zone,
    status      varchar(255)
);