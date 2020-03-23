

INSERT INTO users (username, encrypted_password, role, active)
    values ('admin', '$2a$04$FK/bOGOv6wlm6VH6mSYJDuSzf9EAasHljhObh0c3sqLlnLcRpor4i', 'ADMIN', 1);
INSERT INTO users (username, encrypted_password, role, active)
    values ('user1', '$2a$04$FK/bOGOv6wlm6VH6mSYJDuSzf9EAasHljhObh0c3sqLlnLcRpor4i', 'USER', 1);
INSERT INTO users (username, encrypted_password, role, active)
    values ('user2', '$2a$04$FK/bOGOv6wlm6VH6mSYJDuSzf9EAasHljhObh0c3sqLlnLcRpor4i', 'USER', 0);


INSERT INTO notes (message, date, done, user_id) VALUES ('Note 1',  '2019-12-10 10:30:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 2',  '2019-12-10 12:30:00', 1, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 3',  '2020-01-01 12:30:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 4',  '2020-01-02 19:10:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 5',  '2020-01-03 12:30:00', 1, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 6',  '2020-01-04 12:30:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 7',  '2020-01-05 12:30:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 8',  '2020-01-06 12:30:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 9',  '2020-01-07 12:30:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 10', '2020-01-08 10:00:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 11', '2020-01-09 12:30:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 12', '2020-01-10 12:30:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 13', '2020-01-11 12:30:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 14', '2020-01-12 12:30:00', 0, 1);
INSERT INTO notes (message, date, done, user_id) VALUES ('Note 15', '2020-01-13 12:30:00', 0, 1);
