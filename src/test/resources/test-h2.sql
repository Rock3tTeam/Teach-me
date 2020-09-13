DROP TABLE IF EXISTS classes;

CREATE TABLE IF NOT EXISTS classes (
   id serial PRIMARY KEY,
   amount_of_students INTEGER(50) NOT NULL,
   capacity INTEGER(50) NOT NULL,
   date_of_end DATE,
   date_of_init DATE,
   description VARCHAR(255) UNIQUE NOT NULL,
   name VARCHAR(255) NOT NULL
);

INSERT INTO classes VALUES
    (1,0,20,'2017-03-14','2017-03-14','description test','hi');