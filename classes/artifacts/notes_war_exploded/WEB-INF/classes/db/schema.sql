USE notesdb;

# users

DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id INT(8) NOT NULL AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL,
                       encrypted_password VARCHAR(255) NOT NULL,
                       role VARCHAR(10),
                       active BIT(1) NOT NULL DEFAULT b'0',
                       PRIMARY KEY (id),
                       UNIQUE (username)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = 'utf8'
    COLLATE = 'utf8_general_ci';

# notes

DROP TABLE IF EXISTS notes;

CREATE TABLE notes (
    id INT(8) NOT NULL AUTO_INCREMENT,
    message VARCHAR(100) NOT NULL,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    done BIT(1) NOT NULL DEFAULT b'0',
    PRIMARY KEY (id)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = 'utf8'
COLLATE = 'utf8_general_ci';

ALTER TABLE notes ADD COLUMN user_id INT(8) NOT NULL;
ALTER TABLE notes ADD FOREIGN KEY (user_id) REFERENCES users(id);

# for "Remember me"

CREATE TABLE IF NOT EXISTS persistent_logins(
    username  VARCHAR(64) NOT NULL,
    series    VARCHAR(64) NOT NULL,
    token     VARCHAR(64) NOT NULL,
    last_used TIMESTAMP   NOT NULL,
    PRIMARY KEY (series)
);