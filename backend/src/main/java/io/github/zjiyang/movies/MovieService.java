package io.github.zjiyang.movies;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class MovieService {
    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public MoviePage list(int page, int size) {
        var result = repository.findAll(PageRequest.of(page, size, Sort.by("id").ascending()));
        return new MoviePage(result.map(MovieResponse::from).getContent(), page, size,
                result.getTotalElements(), result.getTotalPages());
    }

    public MovieResponse detail(long id) {
        return repository.findById(id).map(MovieResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    }
}
