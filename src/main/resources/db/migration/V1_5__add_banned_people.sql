CREATE TABLE banned_people(
    id SERIAL PRIMARY KEY,
    id_discord BIGINT NOT NULL,
    reason TEXT
)

