CREATE TABLE users (
                       id            UUID         NOT NULL,
                       google_id     VARCHAR(255) NOT NULL,
                       email         VARCHAR(255) NOT NULL,
                       name          VARCHAR(255) NOT NULL,
                       avatar_url    VARCHAR(500),
                       created_at    TIMESTAMP    NOT NULL,
                       last_login_at TIMESTAMP    NOT NULL,

                       CONSTRAINT pk_users PRIMARY KEY (id),
                       CONSTRAINT uq_users_google_id UNIQUE (google_id),
                       CONSTRAINT uq_users_email    UNIQUE (email)
);
