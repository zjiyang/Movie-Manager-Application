package io.github.zjiyang.movies;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MovieController {
    private static final int MAX_PAGE_SIZE = 100;
    private static final int MAX_GENRE_LENGTH = 60;
    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 10;

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping("/api/movies")
    public MoviePage list(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "20") int size,
                          @RequestParam(required = false) String genre) {
        if (page < 0 || size < 1 || size > MAX_PAGE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "page must be >= 0 and size must be between 1 and " + MAX_PAGE_SIZE);
        }
        if (genre != null && genre.trim().length() > MAX_GENRE_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "genre must be at most " + MAX_GENRE_LENGTH + " characters");
        }
        return service.list(page, size, genre);
    }

    @GetMapping("/api/movies/{id}")
    public MovieResponse detail(@PathVariable long id) {
        requirePositive(id, "id");
        return service.detail(id);
    }

    @GetMapping("/api/genres")
    public List<String> genres() {
        return service.genreNames();
    }

    /**
     * PUT rather than POST: the request names the exact rating it is setting, so
     * sending it twice is the same as sending it once. A POST would read as
     * "add another rating", which is precisely what must not happen.
     *
     * <p>The user is still part of the path. Authentication will take it from the
     * caller's identity instead, and this endpoint is not safe to expose publicly
     * until it does.
     */
    @PutMapping("/api/movies/{movieId}/ratings/{userId}")
    public MovieResponse rate(@PathVariable long movieId,
                              @PathVariable long userId,
                              @RequestBody(required = false) RatingRequest request) {
        requirePositive(movieId, "movieId");
        requirePositive(userId, "userId");
        if (request == null || request.score() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "score is required");
        }
        int score = request.score();
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "score must be between " + MIN_SCORE + " and " + MAX_SCORE);
        }
        return service.rate(movieId, userId, score);
    }

    @DeleteMapping("/api/movies/{movieId}/ratings/{userId}")
    public ResponseEntity<Void> withdrawRating(@PathVariable long movieId, @PathVariable long userId) {
        requirePositive(movieId, "movieId");
        requirePositive(userId, "userId");
        service.withdrawRating(movieId, userId);
        return ResponseEntity.noContent().build();
    }

    private static void requirePositive(long value, String name) {
        if (value < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, name + " must be positive");
        }
    }
}
