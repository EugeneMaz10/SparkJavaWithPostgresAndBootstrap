
createdb spark3;

psql spark3;

CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY NOT NULL,
  first_name TEXT,
  last_name TEXT,
  email TEXT
);

insert into users (first_name, last_name, email) values ('spark', 'mentor3', 'eugenengaira863@gmail.com');
