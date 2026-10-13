CREATE TABLE topics(
    id SERIAL PRIMARY KEY NOT NULL,
    topic TEXT NOT NULL ,
    date_created TIMESTAMP NOT NULL,
    admin_id BIGINT NOT NULL
)