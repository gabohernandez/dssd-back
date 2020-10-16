DROP TABLE IF EXISTS auth_user;
 
CREATE TABLE auth_user (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL
);
 
INSERT INTO auth_user (name, password) VALUES
  ('grupo06', 'grupo06');


DROP TABLE IF EXISTS protocol;
 
CREATE TABLE protocol (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  starttime datetime,
  endtime datetime,
  status VARCHAR(250),
  score INT
);
