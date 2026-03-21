CREATE TABLE IF NOT EXISTS stage
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    name              VARCHAR(30) NOT NULL,
    is_knockout_stage BOOLEAN     NOT NULL
);