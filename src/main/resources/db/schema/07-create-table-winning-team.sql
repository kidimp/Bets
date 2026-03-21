CREATE TABLE IF NOT EXISTS winning_team
(
    id                   INT PRIMARY KEY,
    date_and_time        DATETIME NOT NULL,
    winning_team_id      INT      NOT NULL,
    second_place_team_id INT      NOT NULL
);