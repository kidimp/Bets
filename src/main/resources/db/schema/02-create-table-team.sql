CREATE TABLE IF NOT EXISTS team
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(30) NOT NULL,
    iso_name CHAR(2)     NOT NULL UNIQUE,
    pot      INT         NOT NULL CHECK (pot BETWEEN 1 AND 4)
);