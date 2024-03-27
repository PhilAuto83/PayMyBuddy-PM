DROP DATABASE IF EXISTS pmbtest;
CREATE DATABASE pmbtest;
USE pmbtest;

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
    ('Joe', 'Admin','ADMIN','joe.admin@pmb.fr','$2a$10$zFvC9UAxeYJp3dnyv8cHHO0JupOZ6GtlGtAuWbCFzXn4znAShiGym'),
    ('Test', 'Pmb','USER','test.pmb@test.fr','$2a$10$XeE2pyiMLbkJbXz46tzRf.SFSqlyBQAKis0dzL1jRlHMYHvhxME32'),
    ('Tester', 'Cousy','USER','tester.cousy@test.fr','$2a$10$RdwowuvQO2Y25yyfS/KPxevbKbXNGaLROOk7NMrx7GEkOZRqfRufq'),
    ('Testee', 'Money','USER','testee.money@test.fr','$2a$10$YMiVbLOHUtFsmdBasMMjtO7qrmKVHR4a.utE74AuJlOhlnT/W015O'),
    ('Testo', 'Monkey','USER','testo.monkey@test.fr','$2a$10$YMiVbLOHUtFsmdBasMMjtO7qrmKVHR4a.utE74AuJlOhlnT/W015O'),
    ('Testy', 'Testy','USER','testy@test.fr','$2a$10$YMiVbLOHUtFsmdBasMMjtO7qrmKVHR4a.utE74AuJlOhlnT/W015O');


CREATE TABLE account(
                        id INT NOT NULL PRIMARY KEY,
                        balance DOUBLE NOT NULL,
                        FOREIGN KEY (id) REFERENCES user(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO account (id, balance)
VALUES
    (1, 30.0),
    (2, 800.0),
    (3, 200.0),
    (4, 70.0),
    (5, 450.0),
    (6, 300.0);

CREATE TABLE connection(
                           id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                           sender_email VARCHAR(30) NOT NULL,
                           recipient_email VARCHAR(30) NOT NULL,
                           FOREIGN KEY (sender_email) REFERENCES user(email) ON DELETE CASCADE ON UPDATE CASCADE,
                           FOREIGN KEY (recipient_email) REFERENCES user(email) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO connection (sender_email, recipient_email)
VALUES
    ('test.pmb@test.fr', 'testee.money@test.fr'),
    ('tester.cousy@test.fr', 'testee.money@test.fr');


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
    (CURRENT_DATE ,1, 'Movie tickets', 15.0);


