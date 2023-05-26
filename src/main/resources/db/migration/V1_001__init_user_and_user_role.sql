CREATE TABLE users
(
    id         SERIAL PRIMARY KEY NOT NULL,
    password   VARCHAR(255)       NOT NULL,
    username   VARCHAR(255)       NOT NULL UNIQUE,
    first_name VARCHAR(255)       NOT NULL,
    last_name  VARCHAR(255)       NOT NULL
);

CREATE TABLE user_role
(
    user_id INT          NOT NULL REFERENCES users (id),
    role    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role)
);

ALTER TABLE user_role
    ADD CONSTRAINT user_role_user_fk
        FOREIGN KEY (user_id) REFERENCES users (id);
