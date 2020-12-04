DROP TABLE IF EXISTS PROTOCOL;
DROP TABLE IF EXISTS LAB_USER;
DROP TABLE IF EXISTS ROLE;
DROP TABLE IF EXISTS PROJECT;

CREATE TABLE PROJECT (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(250) NOT NULL,
                         status VARCHAR(250) NOT NULL,
                         BONITA_PROCESS_ID VARCHAR(250) NULL,
                         BONITA_CASE_ID VARCHAR(250) NULL
);

CREATE TABLE ROLE (
  id INT PRIMARY KEY,
  name VARCHAR(250) NOT NULL
);

CREATE TABLE LAB_USER (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  role_id int not null

);

CREATE TABLE PROTOCOL (
  id INT AUTO_INCREMENT PRIMARY KEY,
  start_time datetime,
  name VARCHAR(250) NULL,
  local BOOLEAN NULL,
  end_time datetime,
  status VARCHAR(250),
  score INT,
  approved BOOLEAN NULL,
  USER_ASSIGNED_ID int null,
  PROJECT_ID int not null,
  BONITA_ASSIGNED_ID VARCHAR(250) NULL,
  FOREIGN KEY (PROJECT_ID) REFERENCES PROJECT(id),
  FOREIGN KEY (USER_ASSIGNED_ID) REFERENCES LAB_USER(id)

);


INSERT INTO ROLE (id, name) VALUES (1, 'ADMIN');
INSERT INTO ROLE (id, name) VALUES (2, 'USER');
INSERT INTO LAB_USER (name, password, role_id) VALUES ('juan perez', 'juan.perez', '2');
INSERT INTO LAB_USER (name, password, role_id) VALUES ('maria gonzales', 'maria.gonzalez', '1');




