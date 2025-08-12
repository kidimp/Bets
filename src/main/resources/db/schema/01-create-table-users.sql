CREATE TABLE IF NOT EXISTS users
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    username             VARCHAR(255) NOT NULL UNIQUE,
    email                VARCHAR(255) NOT NULL UNIQUE,
    password             VARCHAR(255) NOT NULL,
    role                 VARCHAR(255) NOT NULL,
    is_active            BOOLEAN      NOT NULL,
    activation_code      VARCHAR(255),
    reset_password_token VARCHAR(255)
);