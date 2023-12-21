CREATE USER 'nbsManager'@'localhost' IDENTIFIED BY '1234';
CREATE DATABASE jwt_security;
GRANT ALL PRIVILEGES ON jwt_security.* TO 'nbsManager'@'localhost';
FLUSH PRIVILEGES;
