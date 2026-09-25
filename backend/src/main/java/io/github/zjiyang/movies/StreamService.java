package io.github.zjiyang.movies;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** A place a movie can be watched, as recorded by the desktop application. */
@Entity
@Table(name = "stream_services")
public class StreamService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60, unique = true)
    private String name;

    protected StreamService() { }

    public Long getId() { return id; }
    public String getName() { return name; }
}
