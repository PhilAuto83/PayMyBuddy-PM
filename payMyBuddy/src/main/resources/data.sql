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
    ('Phil', 'Admin','ADMIN','phil.admin@pmb.fr','$2a$10$6Ne007sU6yj/TaDtNEBFoO8wvbvvw3oujGaga3QMnawj1yNZ5aV1q'),
    ('Phil', 'Pmb','USER','phil.pmb@user.fr','$2a$10$egppM.OoAfk09Lc.maNdtOoi8igd9INuznDIU2nyw7AlefiJ.HOaG'),
    ('Bob', 'Cousy','USER','bob.cousy@user.fr','$2a$10$egppM.OoAfk09Lc.maNdtOoi8igd9INuznDIU2nyw7AlefiJ.HOaG'),
    ('Shawna', 'Money','USER','shawna.money@user.fr','$2a$10$egppM.OoAfk09Lc.maNdtOoi8igd9INuznDIU2nyw7AlefiJ.HOaG');

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
    sender_account_id INT NOT NULL,
    recipient_account_id INT NOT NULL,
    FOREIGN KEY (sender_account_id) REFERENCES account(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (recipient_account_id) REFERENCES account(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO connection (sender_account_id, recipient_account_id)
    VALUES
        (2, 4);


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

