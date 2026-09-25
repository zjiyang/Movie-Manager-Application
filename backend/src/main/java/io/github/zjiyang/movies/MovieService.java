package io.github.zjiyang.movies;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class MovieService {
    private final MovieRepository movies;
    private final GenreRepository genres;
    private final RatingRepository ratings;

    public MovieService(MovieRepository movies, GenreRepository genres, RatingRepository ratings) {
        this.movies = movies;
        this.genres = genres;
        this.ratings = ratings;
    }

    /**
     * One page of movies, optionally narrowed to a single genre. An unknown genre
     * is not an error: it is a filter that matches nothing, so the page is empty.
     */
    public MoviePage list(int page, int size, String genre) {
        var pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Movie> result = (genre == null || genre.isBlank())
                ? movies.findAll(pageable)
                : movies.findByGenreName(genre.trim(), pageable);
        return new MoviePage(result.map(MovieResponse::from).getContent(), page, size,
                result.getTotalElements(), result.getTotalPages());
    }

    public MovieResponse detail(long id) {
        return movies.findById(id).map(MovieResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    }

    /** The genre vocabulary, so a client can offer the filter without guessing. */
    public List<String> genreNames() {
        return genres.findAllByOrderByNameAsc().stream().map(Genre::getName).toList();
    }

    /**
     * Records one user's score for one movie, replacing that user's previous
     * score if there was one. Sending the same request twice leaves the database
     * in the same state as sending it once.
     *
     * <p>The response is the movie rather than the rating: the only thing a
     * rating changes for a reader is the movie's average, so returning the movie
     * saves the client a second request.
     */
    @Transactional
    public MovieResponse rate(long movieId, long userId, int score) {
        // This lookup only buys a clear 404 instead of a constraint error. It is
        // not what keeps the data correct: the unique index is, which is why the
        // write below does not also read the existing rating first.
        if (!movies.existsById(movieId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
        try {
            ratings.upsert(movieId, userId, score);
        } catch (DataIntegrityViolationException failedConstraint) {
            // The movie was deleted between the check above and this write.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found", failedConstraint);
        }
        return detail(movieId);
    }

    /**
     * Withdraws a user's rating. Removing a rating that is not there is not an
     * error, so a repeated request stays successful; only an unknown movie is
     * reported as missing.
     */
    @Transactional
    public void withdrawRating(long movieId, long userId) {
        if (!movies.existsById(movieId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
        }
        ratings.deleteByMovieAndUser(movieId, userId);
    }
}
