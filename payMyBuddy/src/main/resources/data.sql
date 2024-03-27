DROP DATABASE IF EXISTS pmb;
CREATE DATABASE pmb;
USE pmb;
 CREATE TABLE user(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(30) NOT NULL,
    lastname VARCHAR(30) NOT NULL,
    role VARCHAR(5) NOT NULL,
    email VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
    );

INSERT INTO user (firstname, lastname, role, email, password)
VALUES
    ('Phil', 'Admin','ADMIN','phil.admin@pmb.fr','$2a$10$zFvC9UAxeYJp3dnyv8cHHO0JupOZ6GtlGtAuWbCFzXn4znAShiGym'),
    ('Phil', 'Pmb','USER','phil.pmb@test.fr','$2a$10$XeE2pyiMLbkJbXz46tzRf.SFSqlyBQAKis0dzL1jRlHMYHvhxME32'),
    ('Bob', 'Cousy','USER','bob.cousy@test.fr','$2a$10$RdwowuvQO2Y25yyfS/KPxevbKbXNGaLROOk7NMrx7GEkOZRqfRufq'),
    ('Shawna', 'Money','USER','shawna.money@test.fr','$2a$10$YMiVbLOHUtFsmdBasMMjtO7qrmKVHR4a.utE74AuJlOhlnT/W015O');


CREATE TABLE account(
    id INT NOT NULL PRIMARY KEY,
    balance DOUBLE NOT NULL,
    FOREIGN KEY (id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO account (id, balance)
VALUES
    (1, 0.0),
    (2, 500.0),
    (3, 250.0),
    (4, 100.0);

CREATE TABLE connection(
   id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
   sender_email VARCHAR(30) NOT NULL,
   recipient_email VARCHAR(30) NOT NULL,
   FOREIGN KEY (sender_email) REFERENCES user(email) ON DELETE CASCADE ON UPDATE CASCADE,
   FOREIGN KEY (recipient_email) REFERENCES user(email) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO connection (sender_email, recipient_email)
VALUES
    ('phil.pmb@test.fr', 'shawna.money@test.fr');

CREATE TABLE transaction(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    creation_date DATE NOT NULL,
    connection_id INT NOT NULL,
    description VARCHAR(30) NOT NULL,
    amount DOUBLE NOT NULL,
    FOREIGN KEY (connection_id) REFERENCES connection(id) ON DELETE CASCADE ON UPDATE CASCADE
);
INSERT INTO transaction (creation_date, connection_id, description, amount)
VALUES
    (CURRENT_DATE ,1, 'Movie tickets', 20.0);










