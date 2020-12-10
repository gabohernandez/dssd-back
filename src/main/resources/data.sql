DROP TABLE IF EXISTS PROTOCOL;
DROP TABLE IF EXISTS AUTH_USER;
DROP TABLE IF EXISTS PROJECT;

CREATE TABLE PROJECT (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(250) NOT NULL
);

CREATE TABLE AUTH_USER (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL
);

CREATE TABLE PROTOCOL (
  id INT AUTO_INCREMENT PRIMARY KEY,
  start_time datetime,
  end_time datetime,
  status VARCHAR(250),
  score INT,
  PROJECT_ID int not null,
  PROTOCOL_UUID VARCHAR(250) null,
  FOREIGN KEY (PROJECT_ID) REFERENCES PROJECT(id)

);

INSERT INTO AUTH_USER (name, password) VALUES ('grupo06', 'grupo06');
INSERT INTO AUTH_USER (name, password) VALUES ('juan perez', 'juan.perez');
INSERT INTO AUTH_USER (name, password) VALUES ('maria gonzales', 'maria.gonzalez');
INSERT INTO AUTH_USER (name, password) VALUES ('pachorra sabella', 'pachorra.sabella');
INSERT INTO AUTH_USER (name, password) VALUES ('diego maradona dios', 'diego.maradona.dios');




