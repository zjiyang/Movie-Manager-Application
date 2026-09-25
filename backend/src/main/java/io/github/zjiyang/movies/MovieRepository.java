package io.github.zjiyang.movies;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Filters on genre with a subquery rather than a join, so a movie carrying
     * two genres still counts once and the page totals stay correct.
     */
    @Query(value = "select m from Movie m "
            + "where lower(:genre) in (select lower(g.name) from m.genres g)",
           countQuery = "select count(m) from Movie m "
            + "where lower(:genre) in (select lower(g.name) from m.genres g)")
    Page<Movie> findByGenreName(@Param("genre") String genre, Pageable pageable);
}
