-- Verifies the rules the schema is supposed to enforce. Everything runs inside a
-- transaction that is rolled back, so the database is unchanged afterwards.
\set ON_ERROR_STOP on
BEGIN;
DO $$
DECLARE
    sample_id BIGINT;
    drama_id BIGINT;
    remaining INTEGER;
BEGIN
    SELECT id INTO drama_id FROM genres WHERE name = 'Drama';
    IF drama_id IS NULL THEN
        RAISE EXCEPTION 'Seeded genre vocabulary is missing';
    END IF;

    -- The imported library is present.
    IF (SELECT count(*) FROM movies) < 6 THEN
        RAISE EXCEPTION 'Imported movie library is missing rows';
    END IF;
    IF NOT EXISTS (
        SELECT 1 FROM movies m
        JOIN movie_genres mg ON mg.movie_id = m.id
        JOIN genres g ON g.id = mg.genre_id
        WHERE m.title = 'The Godfather' AND g.name = 'Drama'
    ) THEN
        RAISE EXCEPTION 'Imported movie lost its genre relationship';
    END IF;

    -- Create, read and update a movie.
    INSERT INTO movies (title, release_year) VALUES ('Database check', 2026)
        RETURNING id INTO sample_id;
    IF NOT EXISTS (SELECT 1 FROM movies WHERE id = sample_id AND created_at IS NOT NULL) THEN
        RAISE EXCEPTION 'Inserted movie not readable';
    END IF;
    UPDATE movies SET title = 'Updated database check' WHERE id = sample_id;
    IF NOT EXISTS (SELECT 1 FROM movies WHERE id = sample_id AND title = 'Updated database check') THEN
        RAISE EXCEPTION 'Update not persisted';
    END IF;

    -- A blank or missing title, and an impossible release year, are rejected.
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

    -- A genre cannot be attached to the same movie twice.
    INSERT INTO movie_genres (movie_id, genre_id) VALUES (sample_id, drama_id);
    BEGIN
        INSERT INTO movie_genres (movie_id, genre_id) VALUES (sample_id, drama_id);
        RAISE EXCEPTION 'Duplicate genre link was accepted';
    EXCEPTION WHEN unique_violation THEN NULL;
    END;

    -- A genre still referenced by a movie cannot be deleted.
    BEGIN
        DELETE FROM genres WHERE id = drama_id;
        RAISE EXCEPTION 'Referenced genre was deleted';
    EXCEPTION WHEN foreign_key_violation THEN NULL;
    END;

    -- One rating per user per movie; a second rating from the same user fails.
    INSERT INTO ratings (movie_id, user_id, score) VALUES (sample_id, 42, 7);
    BEGIN
        INSERT INTO ratings (movie_id, user_id, score) VALUES (sample_id, 42, 9);
        RAISE EXCEPTION 'Second rating from the same user was accepted';
    EXCEPTION WHEN unique_violation THEN NULL;
    END;

    -- Scores stay inside the range the desktop application used.
    BEGIN
        INSERT INTO ratings (movie_id, user_id, score) VALUES (sample_id, 43, 11);
        RAISE EXCEPTION 'Out-of-range score was accepted';
    EXCEPTION WHEN check_violation THEN NULL;
    END;

    -- A rating cannot point at a movie that does not exist.
    BEGIN
        INSERT INTO ratings (movie_id, user_id, score) VALUES (9223372036854775807, 44, 5);
        RAISE EXCEPTION 'Rating for a missing movie was accepted';
    EXCEPTION WHEN foreign_key_violation THEN NULL;
    END;

    -- Deleting a movie removes its ratings and links, leaving no orphans.
    DELETE FROM movies WHERE id = sample_id;
    IF EXISTS (SELECT 1 FROM movies WHERE id = sample_id) THEN
        RAISE EXCEPTION 'Deleted movie still present';
    END IF;
    SELECT count(*) INTO remaining FROM ratings WHERE movie_id = sample_id;
    IF remaining <> 0 THEN
        RAISE EXCEPTION 'Ratings outlived their movie';
    END IF;
    SELECT count(*) INTO remaining FROM movie_genres WHERE movie_id = sample_id;
    IF remaining <> 0 THEN
        RAISE EXCEPTION 'Genre links outlived their movie';
    END IF;
END $$;
ROLLBACK;
