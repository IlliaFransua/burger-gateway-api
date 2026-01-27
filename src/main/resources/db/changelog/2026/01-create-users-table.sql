--liquibase formatted sql

-- changeset IlliaFransua:01-create-users-table
CREATE TABLE users (
    id          VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    name        VARCHAR(255),
    picture     VARCHAR(255),
    last_login  TIMESTAMP(6) WITH TIME ZONE,
    
    PRIMARY KEY (id),
    CONSTRAINT user_email_unique UNIQUE (email)
);
