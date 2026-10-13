CREATE TABLE bug_reports(
    id SERIAL PRIMARY KEY,
    bug_description text NOT NULL,
    date TIMESTAMP NOT NULL,
    user_id BIGINT,
    FOREIGN KEY(user_id) REFERENCES users(id)
)
