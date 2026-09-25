package io.github.zjiyang.movies;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

// Real HTTP server and PostgreSQL: run only against a disposable development DB.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RatingApiIT {
    private static final long USER = 9001L;
    private static final long OTHER_USER = 9002L;
    private static final long MISSING_MOVIE = 9223372036854775807L;

    @Autowired TestRestTemplate http;
    @Autowired JdbcTemplate jdbc;
    private Long movieId;

    @BeforeEach
    void insertMovie() {
        movieId = jdbc.queryForObject(
                "INSERT INTO movies (title, release_year) VALUES (?, ?) RETURNING id",
                Long.class, "评分测试电影", 2026);
    }

    @AfterEach
    void removeMovie() {
        if (movieId != null) {
            // Ratings go with it: the foreign key cascades.
            jdbc.update("DELETE FROM movies WHERE id = ?", movieId);
        }
    }

    // --- the rule this step exists for ---------------------------------------

    @Test
    void ratingTwiceReplacesTheScoreInsteadOfAddingARow() {
        assertThat(rate(USER, 4).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rate(USER, 9).getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(ratingRows(USER)).isEqualTo(1);
        assertThat(storedScore(USER)).isEqualTo(9);
    }

    @Test
    void reRatingKeepsTheFirstTimestampAndMovesTheSecond() {
        rate(USER, 4);
        Instant created = timestamp("created_at");
        Instant firstUpdate = timestamp("updated_at");
        assertThat(firstUpdate).isEqualTo(created);

        rate(USER, 5);
        assertThat(timestamp("created_at")).isEqualTo(created);
        assertThat(timestamp("updated_at")).isAfter(firstUpdate);
    }

    @Test
    void eachUserKeepsTheirOwnScore() {
        rate(USER, 3);
        rate(OTHER_USER, 8);

        assertThat(storedScore(USER)).isEqualTo(3);
        assertThat(storedScore(OTHER_USER)).isEqualTo(8);
        assertThat(jdbc.queryForObject(
                "SELECT count(*) FROM ratings WHERE movie_id = ?", Integer.class, movieId)).isEqualTo(2);
    }

    // --- what the caller gets back -------------------------------------------

    @Test
    void responseCarriesTheRecomputedAverage() {
        rate(USER, 6);
        JsonNode movie = rate(OTHER_USER, 9).getBody();

        assertThat(movie.get("id").asLong()).isEqualTo(movieId);
        assertThat(movie.get("ratingCount").asInt()).isEqualTo(2);
        assertThat(movie.get("averageScore").asDouble()).isEqualTo(7.5);
    }

    // --- input the service must refuse ---------------------------------------

    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    void scoresAtTheEdgesOfTheRangeAreAccepted(int score) {
        assertThat(rate(USER, score).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(storedScore(USER)).isEqualTo(score);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 11, 100})
    void scoresOutsideTheRangeAreRejected(int score) {
        assertThat(rate(USER, score).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(ratingRows(USER)).isZero();
    }

    @Test
    void aMissingScoreIsRejected() {
        var body = new HashMap<String, Object>();
        body.put("score", null);
        var response = http.exchange(ratingUrl(movieId, USER), HttpMethod.PUT,
                new HttpEntity<>(body), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void aZeroUserIsRejected() {
        var response = http.exchange(ratingUrl(movieId, 0), HttpMethod.PUT,
                new HttpEntity<>(Map.of("score", 5)), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void ratingAMovieThatDoesNotExistIsNotFound() {
        var response = http.exchange(ratingUrl(MISSING_MOVIE, USER), HttpMethod.PUT,
                new HttpEntity<>(Map.of("score", 5)), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // --- withdrawing ----------------------------------------------------------

    @Test
    void withdrawingRemovesTheRatingAndTheAverage() {
        rate(USER, 7);
        assertThat(withdraw(movieId, USER).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(ratingRows(USER)).isZero();
        JsonNode movie = http.getForObject("/api/movies/" + movieId, JsonNode.class);
        assertThat(movie.get("averageScore").isNull()).isTrue();
        assertThat(movie.get("ratingCount").asInt()).isZero();
    }

    @Test
    void withdrawingNothingIsStillSuccessful() {
        assertThat(withdraw(movieId, USER).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(withdraw(movieId, USER).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void withdrawingFromAMovieThatDoesNotExistIsNotFound() {
        assertThat(withdraw(MISSING_MOVIE, USER).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // --- helpers --------------------------------------------------------------

    private ResponseEntity<JsonNode> rate(long userId, int score) {
        return http.exchange(ratingUrl(movieId, userId), HttpMethod.PUT,
                new HttpEntity<>(Map.of("score", score)), JsonNode.class);
    }

    private ResponseEntity<Void> withdraw(long movie, long userId) {
        return http.exchange(ratingUrl(movie, userId), HttpMethod.DELETE,
                HttpEntity.EMPTY, Void.class);
    }

    private static String ratingUrl(long movie, long userId) {
        return "/api/movies/" + movie + "/ratings/" + userId;
    }

    private int ratingRows(long userId) {
        return jdbc.queryForObject("SELECT count(*) FROM ratings WHERE movie_id = ? AND user_id = ?",
                Integer.class, movieId, userId);
    }

    private int storedScore(long userId) {
        return jdbc.queryForObject("SELECT score FROM ratings WHERE movie_id = ? AND user_id = ?",
                Integer.class, movieId, userId);
    }

    private Instant timestamp(String column) {
        var value = jdbc.queryForObject(
                "SELECT " + column + " FROM ratings WHERE movie_id = ? AND user_id = ?",
                OffsetDateTime.class, movieId, USER);
        return value.toInstant();
    }
}
