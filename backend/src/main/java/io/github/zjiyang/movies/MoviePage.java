package io.github.zjiyang.movies;

import java.util.List;

public record MoviePage(List<MovieResponse> items, int page, int size, long totalElements, int totalPages) { }
