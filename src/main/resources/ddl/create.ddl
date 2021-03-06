DROP TABLE    IF EXISTS txt;
DROP SEQUENCE IF EXISTS seq_txt;

CREATE SEQUENCE seq_txt START 1;

CREATE TABLE txt (
  id   INTEGER PRIMARY KEY DEFAULT nextval('seq_txt'),
  name VARCHAR NOT NULL,
  body VARCHAR NOT NULL
);