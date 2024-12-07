CREATE TABLE books
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    author      VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    ISBN        VARCHAR(255) NOT NULL
);
CREATE TABLE book_genres (
                             book_id BIGINT REFERENCES books(id) ON DELETE CASCADE,
                             genre VARCHAR(255) NOT NULL,
                             PRIMARY KEY (book_id, genre)
);
ALTER TABLE books
    ADD COLUMN admin_id BIGINT REFERENCES users(id) ON DELETE SET NULL;
ALTER TABLE books
    ADD CONSTRAINT unique_ISBN UNIQUE (ISBN);
