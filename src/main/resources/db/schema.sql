USE notesdb;

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