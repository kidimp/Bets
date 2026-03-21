CREATE TABLE IF NOT EXISTS bet
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    user_id         INT     NOT NULL,
    match_id        INT     NOT NULL,
    score_home_team INT     NOT NULL CHECK (score_home_team >= 0),
    score_away_team INT     NOT NULL CHECK (score_away_team >= 0),
    is_extra_time   BOOLEAN NOT NULL,
    is_penalty      BOOLEAN NOT NULL,
    points DOUBLE

--     CONSTRAINT fk_bet_user FOREIGN KEY (user_id) REFERENCES users (id),
--     CONSTRAINT fk_bet_matches FOREIGN KEY (match_id) REFERENCES matches (id),
--     CONSTRAINT unq_user_matches UNIQUE (user_id, match_id)
);
