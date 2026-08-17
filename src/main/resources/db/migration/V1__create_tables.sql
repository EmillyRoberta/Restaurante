CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       login VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,

                       street VARCHAR(255),
                       number VARCHAR(50),
                       city VARCHAR(100),
                       state VARCHAR(100),
                       zip_code VARCHAR(20),

                       last_update TIMESTAMP,
                       user_type VARCHAR(50) NOT NULL
);

CREATE TABLE restaurants (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             description VARCHAR(500),

                             owner_id BIGINT NOT NULL,

                             CONSTRAINT fk_restaurant_owner
                                 FOREIGN KEY (owner_id)
                                     REFERENCES users(id)
);