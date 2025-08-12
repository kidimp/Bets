CREATE TABLE IF NOT EXISTS round
(
    id    INT PRIMARY KEY AUTO_INCREMENT,
    name  VARCHAR(256) NOT NULL,
    round INT          NOT NULL CHECK (round >= 1)
);