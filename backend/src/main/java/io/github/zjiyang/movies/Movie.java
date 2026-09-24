package io.github.zjiyang.movies;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    protected Movie() { }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Integer getReleaseYear() { return releaseYear; }
    public Instant getCreatedAt() { return createdAt; }
}
