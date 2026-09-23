-- Bootstrap for NEW local volumes only, not a migration runner.
-- Introduce Flyway with the Spring Boot backend before evolving this schema.
CREATE TABLE movies (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL CHECK (length(trim(title)) > 0),
    release_year INTEGER CHECK (release_year BETWEEN 1888 AND 2100),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Fictional development data, unrelated to the legacy JSON files.
INSERT INTO movies (title, release_year)
VALUES ('Demo Movie A', 2024), ('Demo Movie B', 2025);
