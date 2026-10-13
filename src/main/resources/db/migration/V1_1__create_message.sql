CREATE TABLE conversations(
    id SERIAL PRIMARY KEY ,
    date_created TIMESTAMP
);

CREATE TABLE messages(
    id SERIAL PRIMARY KEY ,
    idUser BIGINT,
    message TEXT,
    date_created TIMESTAMP,
    id_message BIGINT,
    conservation_id BIGINT,
    FOREIGN KEY(conservation_id) REFERENCES conversations(id) ON DELETE CASCADE
)
