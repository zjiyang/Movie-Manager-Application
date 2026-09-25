-- Model the relationships the desktop application kept in memory:
-- a movie has many genres and is available on many streaming services,
-- and each user may leave at most one rating per movie.

CREATE TABLE genres (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(60) NOT NULL UNIQUE CHECK (length(trim(name)) > 0)
);

CREATE TABLE stream_services (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(60) NOT NULL UNIQUE CHECK (length(trim(name)) > 0)
);

-- Join tables carry no surface of their own: the pair is the primary key, so the
-- same genre cannot be attached to the same movie twice.
CREATE TABLE movie_genres (
    movie_id BIGINT NOT NULL REFERENCES movies (id) ON DELETE CASCADE,
    genre_id BIGINT NOT NULL REFERENCES genres (id) ON DELETE RESTRICT,
    PRIMARY KEY (movie_id, genre_id)
);
CREATE INDEX idx_movie_genres_genre ON movie_genres (genre_id);

CREATE TABLE movie_stream_services (
    movie_id BIGINT NOT NULL REFERENCES movies (id) ON DELETE CASCADE,
    stream_service_id BIGINT NOT NULL REFERENCES stream_services (id) ON DELETE RESTRICT,
    PRIMARY KEY (movie_id, stream_service_id)
);
CREATE INDEX idx_movie_stream_services_service ON movie_stream_services (stream_service_id);

-- One rating per user per movie is enforced by the database, not by application
-- code, so a race between two requests cannot create a second row.
CREATE TABLE ratings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    movie_id BIGINT NOT NULL REFERENCES movies (id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL CHECK (user_id > 0),
    score SMALLINT NOT NULL CHECK (score BETWEEN 0 AND 10),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_ratings_movie_user UNIQUE (movie_id, user_id)
);
CREATE INDEX idx_ratings_movie ON ratings (movie_id);
