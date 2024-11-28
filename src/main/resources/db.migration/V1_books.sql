CREATE TABLE books
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    author      VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    ISBN        VARCHAR(255) NOT NULL

);
CREATE TABLE genres
(
    id         SERIAL PRIMARY KEY,
    genre_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE book_genres
(
    book_id BIGINT       NOT NULL,
    genre   VARCHAR(255) NOT NULL,
    PRIMARY KEY (book_id, genre),
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE
);
DROP TABLE book_genres;
CREATE TABLE book_genres (
                             book_id BIGINT,
                             genre_id BIGINT,
                             PRIMARY KEY (book_id, genre_id),
                             FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
                             FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);
