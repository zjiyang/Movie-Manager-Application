\set ON_ERROR_STOP on
BEGIN;
DO $$
DECLARE
    movie_id BIGINT;
BEGIN
    INSERT INTO movies (title, release_year) VALUES ('Database check', 2026)
        RETURNING id INTO movie_id;
    IF NOT EXISTS (SELECT 1 FROM movies WHERE id = movie_id AND created_at IS NOT NULL) THEN
        RAISE EXCEPTION 'Inserted movie not readable';
    END IF;
    UPDATE movies SET title = 'Updated database check' WHERE id = movie_id;
    IF NOT EXISTS (SELECT 1 FROM movies WHERE id = movie_id AND title = 'Updated database check') THEN
        RAISE EXCEPTION 'Update not persisted';
    END IF;
    BEGIN
        INSERT INTO movies (title) VALUES ('   ');
        RAISE EXCEPTION 'Blank title was accepted';
    EXCEPTION WHEN check_violation THEN NULL;
    END;
    BEGIN
        INSERT INTO movies (title) VALUES (NULL);
        RAISE EXCEPTION 'Null title was accepted';
    EXCEPTION WHEN not_null_violation THEN NULL;
    END;
    BEGIN
        INSERT INTO movies (title, release_year) VALUES ('Invalid year', 1800);
        RAISE EXCEPTION 'Invalid year was accepted';
    EXCEPTION WHEN check_violation THEN NULL;
    END;
    DELETE FROM movies WHERE id = movie_id;
    IF EXISTS (SELECT 1 FROM movies WHERE id = movie_id) THEN
        RAISE EXCEPTION 'Deleted movie still present';
    END IF;
END $$;
ROLLBACK;
