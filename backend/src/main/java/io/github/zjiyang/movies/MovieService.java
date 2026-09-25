package io.github.zjiyang.movies;

import java.util.List;
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

    public MovieService(MovieRepository movies, GenreRepository genres) {
        this.movies = movies;
        this.genres = genres;
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
}
