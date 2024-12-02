CREATE TABLE library (
                                 id BIGSERIAL PRIMARY KEY,
                                 book_id BIGINT NOT NULL,
                                 user_id BIGINT NOT NULL,
                                 borrow_date TIMESTAMP NOT NULL,
                                 return_date TIMESTAMP,
                                 FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
                                 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);