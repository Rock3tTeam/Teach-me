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
   date_of_end DATE NOT NULL,
   date_of_init DATE NOT NULL,
   description VARCHAR(255) UNIQUE NOT NULL,
   name VARCHAR(255) NOT NULL,
   professor VARCHAR(255) NOT NULL
);

ALTER TABLE classes ADD CONSTRAINT FK_PROFESSOR
  foreign key (professor)
  references users (email);

INSERT INTO users VALUES
    ('prueba@gmail.com','pepito','perez','prueba','Soy una prueba');

INSERT INTO classes VALUES
    (1,0,20,'2017-03-14','2017-03-14','description test','clase de prueba','prueba@gmail.com');