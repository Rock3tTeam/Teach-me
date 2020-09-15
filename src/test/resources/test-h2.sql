DROP TABLE IF EXISTS classes;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
   email VARCHAR(255) PRIMARY KEY,
   first_name VARCHAR(255)  NOT NULL,
   last_name VARCHAR(255)  NOT NULL,
   password VARCHAR(255)  NOT NULL,
   description VARCHAR(255)  NOT NULL
);

CREATE TABLE IF NOT EXISTS classes (
   id serial PRIMARY KEY,
   amount_of_students INTEGER(50) NOT NULL,
   capacity INTEGER(50) NOT NULL,
   date_of_end TIMESTAMP NOT NULL,
   date_of_init TIMESTAMP NOT NULL,
   description VARCHAR(255) UNIQUE NOT NULL,
   name VARCHAR(255) NOT NULL,
   professor VARCHAR(255) NOT NULL
);

ALTER TABLE classes ADD CONSTRAINT FK_PROFESSOR
  foreign key (professor)
  references users (email);

