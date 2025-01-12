DO
$$
    BEGIN
        IF
            NOT EXISTS(SELECT 1 FROM pg_namespace WHERE nspname = 'library2_schema') THEN
            CREATE SCHEMA library2_schema;
        END IF;
    END
$$;
CREATE TABLE library2_schema.users (
                                       id SERIAL PRIMARY KEY,
                                       username VARCHAR(100) NOT NULL UNIQUE,
                                       password VARCHAR(100) NOT NULL,
                                       name VARCHAR(100) NOT NULL
);

CREATE TABLE library2_schema.user_roles (
                                            user_id BIGINT NOT NULL,
                                            role VARCHAR(50) NOT NULL,
                                            FOREIGN KEY (user_id) REFERENCES library2_schema.users(id) ON DELETE CASCADE,
                                            PRIMARY KEY (user_id, role)
);

