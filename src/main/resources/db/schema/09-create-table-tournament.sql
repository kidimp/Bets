CREATE TABLE IF NOT EXISTS tournament
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    code             VARCHAR(20) NOT NULL,
    name             VARCHAR(30) NOT NULL,
    start            DATETIME NOT NULL,
    finish           DATETIME NOT NULL,
    winning_team_id  INT,
    runner_up_team_id INT,

    CONSTRAINT chk_code_length CHECK (CHAR_LENGTH(code) BETWEEN 3 AND 20),
    CONSTRAINT chk_name_length CHECK (CHAR_LENGTH(name) BETWEEN 3 AND 30)
);