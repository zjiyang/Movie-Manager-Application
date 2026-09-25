-- Import of data/MovieDataBase.json from the desktop application, so the service
-- serves the real library rather than placeholder rows. Genres and streaming
-- services keep the vocabulary the original model class defined.

INSERT INTO genres (name) VALUES
    ('Action'), ('Romance'), ('Drama'), ('Comedy'),
    ('Horror'), ('Sci-Fi'), ('Animation'), ('Documentary');

-- 'Only In Theater' is not a streaming platform, but the desktop application
-- stored it in the same field, so the import keeps it rather than dropping data.
INSERT INTO stream_services (name) VALUES
    ('Netflix'), ('DisneyPlus'), ('AppleTV'), ('PrimeVideo'), ('Only In Theater');

INSERT INTO movies (title, release_year) VALUES
    ('The Godfather', 1972),
    ('Black Swan', 2010),
    ('The Outrun', 2024),
    ('La La Land', 2016),
    ('Anora', 2024),
    ('There''s Still Tomorrow', 2023);

INSERT INTO movie_genres (movie_id, genre_id)
SELECT m.id, g.id
FROM (VALUES
    ('The Godfather', 'Drama'),
    ('Black Swan', 'Horror'),
    ('The Outrun', 'Drama'),
    ('La La Land', 'Romance'),
    ('Anora', 'Romance'),
    ('There''s Still Tomorrow', 'Drama'),
    ('There''s Still Tomorrow', 'Comedy')
) AS pair (movie_title, genre_name)
JOIN movies m ON m.title = pair.movie_title
JOIN genres g ON g.name = pair.genre_name;

INSERT INTO movie_stream_services (movie_id, stream_service_id)
SELECT m.id, s.id
FROM (VALUES
    ('The Godfather', 'Netflix'),
    ('The Godfather', 'DisneyPlus'),
    ('Black Swan', 'Netflix'),
    ('The Outrun', 'PrimeVideo'),
    ('La La Land', 'PrimeVideo'),
    ('Anora', 'Netflix'),
    ('There''s Still Tomorrow', 'Only In Theater')
) AS pair (movie_title, service_name)
JOIN movies m ON m.title = pair.movie_title
JOIN stream_services s ON s.name = pair.service_name;

INSERT INTO ratings (movie_id, user_id, score)
SELECT m.id, pair.user_id, pair.score
FROM (VALUES
    ('The Godfather', 1, 8),
    ('Black Swan', 1, 9),
    ('The Outrun', 1, 9),
    ('La La Land', 1, 10),
    ('Anora', 1, 7),
    ('There''s Still Tomorrow', 1, 9)
) AS pair (movie_title, user_id, score)
JOIN movies m ON m.title = pair.movie_title;
