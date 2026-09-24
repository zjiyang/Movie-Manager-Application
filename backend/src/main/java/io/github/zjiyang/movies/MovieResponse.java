package io.github.zjiyang.movies;

import java.time.Instant;

public record MovieResponse(Long id, String title, Integer releaseYear, Instant createdAt) {
    static MovieResponse from(Movie movie) {
        return new MovieResponse(movie.getId(), movie.getTitle(), movie.getReleaseYear(), movie.getCreatedAt());
    }
}
