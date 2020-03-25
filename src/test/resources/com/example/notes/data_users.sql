
DELETE FROM notes;
DELETE FROM users;

INSERT INTO users (id, username, encrypted_password, role, active)
    values (1, 'admin', '$2a$04$FK/bOGOv6wlm6VH6mSYJDuSzf9EAasHljhObh0c3sqLlnLcRpor4i', 'ADMIN', 1);
INSERT INTO users (id, username, encrypted_password, role, active)
    values (2, 'user1', '$2a$04$FK/bOGOv6wlm6VH6mSYJDuSzf9EAasHljhObh0c3sqLlnLcRpor4i', 'USER', 1);
INSERT INTO users (id, username, encrypted_password, role, active)
    values (3, 'user2', '$2a$04$FK/bOGOv6wlm6VH6mSYJDuSzf9EAasHljhObh0c3sqLlnLcRpor4i', 'USER', 0);

