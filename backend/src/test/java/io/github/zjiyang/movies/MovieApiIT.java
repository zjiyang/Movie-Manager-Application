package io.github.zjiyang.movies;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

// Real HTTP server and PostgreSQL: run only against a disposable development DB.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieApiIT {
    @Autowired TestRestTemplate http;
    @Autowired JdbcTemplate jdbc;
    private Long fixtureId;

    @BeforeEach
    void insertFixture() {
        fixtureId = jdbc.queryForObject(
                "INSERT INTO movies (title, release_year) VALUES (?, ?) RETURNING id",
                Long.class, "接口测试电影", 2026);
    }

    @AfterEach
    void removeFixture() {
        if (fixtureId != null) {
            jdbc.update("DELETE FROM movies WHERE id = ?", fixtureId);
        }
    }

    @Test
    void detailReadsActualDatabaseAndReflectsUpdates() {
        var response = http.getForEntity("/api/movies/" + fixtureId, JsonNode.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("title").asText()).isEqualTo("接口测试电影");
        assertThat(response.getBody().get("releaseYear").asInt()).isEqualTo(2026);
        assertThat(response.getBody().get("createdAt").asText()).isNotBlank();
        jdbc.update("UPDATE movies SET release_year = NULL WHERE id = ?", fixtureId);
        var updated = http.getForObject("/api/movies/" + fixtureId, JsonNode.class);
        assertThat(updated.get("releaseYear").isNull()).isTrue();
    }

    @Test
    void listIsPaginatedAndOrderedById() {
        var first = http.getForEntity("/api/movies?page=0&size=1", JsonNode.class);
        var second = http.getForObject("/api/movies?page=1&size=1", JsonNode.class);
        assertThat(first.getStatusCode()).isEqualTo(HttpStatus.OK);
        var body = first.getBody();
        assertThat(body.get("items").size()).isEqualTo(1);
        assertThat(body.get("page").asInt()).isZero();
        assertThat(body.get("size").asInt()).isEqualTo(1);
        assertThat(body.get("totalElements").asLong())
                .isEqualTo(jdbc.queryForObject("SELECT count(*) FROM movies", Long.class));
        assertThat(body.get("items").get(0).get("id").asLong())
                .isLessThan(second.get("items").get(0).get("id").asLong());
    }

    @Test
    void outOfRangePageReturnsEmptyItems() {
        var response = http.getForEntity("/api/movies?page=99999&size=100", JsonNode.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get("items").isEmpty()).isTrue();
    }

    @Test
    void missingMovieReturnsNotFound() {
        var response = http.getForEntity("/api/movies/9223372036854775807", JsonNode.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().get("detail").asText()).isEqualTo("Movie not found");
    }

    @ParameterizedTest
    @ValueSource(strings = {"?page=-1", "?size=0", "?size=101", "?page=abc", "/0", "/abc"})
    void invalidRequestsReturnBadRequest(String suffix) {
        assertThat(http.getForEntity("/api/movies" + suffix, String.class).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
