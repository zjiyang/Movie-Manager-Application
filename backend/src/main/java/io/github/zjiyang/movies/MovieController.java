package io.github.zjiyang.movies;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class MovieController {
    private static final int MAX_PAGE_SIZE = 100;
    private static final int MAX_GENRE_LENGTH = 60;

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
        if (id < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id must be positive");
        }
        return service.detail(id);
    }

    @GetMapping("/api/genres")
    public List<String> genres() {
        return service.genreNames();
    }
}
