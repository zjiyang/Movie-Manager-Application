package io.github.zjiyang.movies;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.OptionalDouble;
import java.util.Set;
import org.hibernate.annotations.BatchSize;

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

    // Collections load lazily so a list request does not drag every relationship
    // into memory. BatchSize makes Hibernate fetch them for the whole page in one
    // query per collection instead of one query per movie.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @OrderBy("name")
    @BatchSize(size = 100)
    private Set<Genre> genres = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "movie_stream_services",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "stream_service_id"))
    @OrderBy("name")
    @BatchSize(size = 100)
    private Set<StreamService> streamServices = new LinkedHashSet<>();

    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    private Set<Rating> ratings = new LinkedHashSet<>();

    protected Movie() { }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Integer getReleaseYear() { return releaseYear; }
    public Instant getCreatedAt() { return createdAt; }
    public Set<Genre> getGenres() { return genres; }
    public Set<StreamService> getStreamServices() { return streamServices; }
    public Set<Rating> getRatings() { return ratings; }

    /**
     * Mean of every score, empty when nobody has rated the movie. The desktop
     * version divided two integers and threw on an empty list; this returns a
     * real average and says "no value" instead of failing.
     */
    public OptionalDouble averageScore() {
        return ratings.stream().mapToInt(Rating::getScore).average();
    }
}
