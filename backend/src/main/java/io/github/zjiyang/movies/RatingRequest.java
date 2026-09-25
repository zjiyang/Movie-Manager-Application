package io.github.zjiyang.movies;

/**
 * The body of a rating request. Score is boxed so a missing field arrives as
 * null and can be rejected, instead of silently becoming zero.
 */
public record RatingRequest(Integer score) { }
