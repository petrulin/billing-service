CREATE ROLE billing_service WITH
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE
    INHERIT
    LOGIN
    NOREPLICATION
    NOBYPASSRLS
    CONNECTION LIMIT -1
    PASSWORD '3wAoXJGHSv';


CREATE SCHEMA billing_service AUTHORIZATION billing_service;


-- Permissions

GRANT ALL ON SCHEMA billing_service TO billing_service;

CREATE TABLE billing_service.message (
	uuid varchar(100) NOT NULL,
	CONSTRAINT message_pkey PRIMARY KEY (uuid)
);

ALTER TABLE billing_service.message OWNER TO billing_service;
GRANT ALL ON TABLE billing_service.message TO billing_service;


CREATE TABLE billing_service.client (
   id bigserial NOT NULL,
   created timestamp(6) NOT NULL,
   balance numeric(16,2) NOT NULL DEFAULT 0,
   currency int4 NOT NULL,
   user_name varchar(50) NOT NULL,
   CONSTRAINT billing_pkey PRIMARY KEY (id)
);

ALTER TABLE billing_service.client OWNER TO billing_service;
GRANT ALL ON TABLE billing_service.client TO billing_service;
