CREATE TABLE blocked_users (
        user_id BIGINT NOT NULL,
        blocked_user_id BIGINT NOT NULL,
        PRIMARY KEY (user_id, blocked_user_id),
        FOREIGN KEY (user_id) REFERENCES users(id),
        FOREIGN KEY (blocked_user_id) REFERENCES users(id)
);