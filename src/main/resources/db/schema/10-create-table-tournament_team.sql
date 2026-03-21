CREATE TABLE IF NOT EXISTS tournament_team
(
    tournament_id INT NOT NULL,
    team_id       INT NOT NULL,
    PRIMARY KEY (tournament_id, team_id),

    CONSTRAINT fk_tt_tournament FOREIGN KEY (tournament_id) REFERENCES tournament (id),
    CONSTRAINT fk_tt_team FOREIGN KEY (team_id) REFERENCES team (id)
);