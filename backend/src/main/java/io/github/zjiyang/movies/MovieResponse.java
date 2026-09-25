package io.github.zjiyang.movies;

import java.time.Instant;
import java.util.List;

/**
 * What the API returns. Kept separate from the entity so the database layout can
 * change without silently changing the shape clients depend on.
 */
public record MovieResponse(Long id,
                            String title,
                            Integer releaseYear,
                            List<String> genres,
                            List<String> streamServices,
                            Double averageScore,
                            int ratingCount,
                            Instant createdAt) {

    static MovieResponse from(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseYear(),
                movie.getGenres().stream().map(Genre::getName).toList(),
                movie.getStreamServices().stream().map(StreamService::getName).toList(),
                roundToOneDecimal(movie.averageScore()),
                movie.getRatings().size(),
                movie.getCreatedAt());
    }

    private static Double roundToOneDecimal(java.util.OptionalDouble average) {
        if (average.isEmpty()) {
            return null;
        }
        return Math.round(average.getAsDouble() * 10.0) / 10.0;
    }
}
