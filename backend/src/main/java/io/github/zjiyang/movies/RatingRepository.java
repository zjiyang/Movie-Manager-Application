package io.github.zjiyang.movies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    /**
     * Stores a score in one statement. Reading first to decide between insert and
     * update would be check-then-act: two requests could both read "no rating
     * yet" and both insert. Here the database resolves the collision against the
     * unique index at write time, so re-rating replaces the score and never adds
     * a second row.
     *
     * <p>created_at is deliberately untouched on conflict: the row keeps the
     * moment the user first rated the movie.
     *
     * <p>This is PostgreSQL syntax. The portable alternative is to attempt the
     * insert and catch the unique violation, which costs a round trip and an
     * exception on a completely ordinary path.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            INSERT INTO ratings (movie_id, user_id, score)
            VALUES (:movieId, :userId, :score)
            ON CONFLICT (movie_id, user_id)
            DO UPDATE SET score = EXCLUDED.score, updated_at = CURRENT_TIMESTAMP
            """, nativeQuery = true)
    int upsert(@Param("movieId") long movieId,
               @Param("userId") long userId,
               @Param("score") int score);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM ratings WHERE movie_id = :movieId AND user_id = :userId",
           nativeQuery = true)
    int deleteByMovieAndUser(@Param("movieId") long movieId, @Param("userId") long userId);
}
