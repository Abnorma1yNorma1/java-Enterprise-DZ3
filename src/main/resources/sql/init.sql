CREATE SCHEMA vote_app AUTHORIZATION postgres;

CREATE TABLE vote_app.singers
(
    name character varying NOT NULL,
    votes integer NOT NULL DEFAULT 0,
    PRIMARY KEY (name)
);

ALTER TABLE IF EXISTS vote_app.singers
    OWNER to postgres;


CREATE TABLE vote_app.genres
(
    name character varying NOT NULL,
    votes integer NOT NULL DEFAULT 0,
    PRIMARY KEY (name)
);

ALTER TABLE IF EXISTS vote_app.genres
    OWNER to postgres;


CREATE TABLE vote_app.comments
(
    dt_create timestamp(6) without time zone NOT NULL,
    content text NOT NULL
);

ALTER TABLE IF EXISTS vote_app.comments
    OWNER to postgres;