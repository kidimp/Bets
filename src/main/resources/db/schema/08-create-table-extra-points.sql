CREATE TABLE IF NOT EXISTS extra_points
(
    id                              INT PRIMARY KEY AUTO_INCREMENT,
    user_id                         INT NOT NULL,
    winning_team_id                 INT NOT NULL,
    number_of_hits_on_correct_score INT NOT NULL,
    number_of_hits_on_match_result  INT NOT NULL,
    extra_points DOUBLE NOT NULL
);