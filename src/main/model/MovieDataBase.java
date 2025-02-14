package model;

import java.util.ArrayList;

// Represents the data base, including all movies on this platform
public class MovieDataBase {

    // EFFECTS: Construct a empty movie database for the platform.
    public MovieDataBase() {
    }

    // MODIFIES: this
    // EFFECTS: add given movie to the database.
    public void addMovie(Movie movie) {
    }

    // EFFECTS: produce a movie list by flitering the given genre
    // is has no movie, return empty list.
    public ArrayList<Movie> filterMoviebyGenre(Genre genre) {
        return null;
    }

    // EFFECTS: retunr a list with decresing order of average score
    public ArrayList<Movie> rankMovies() {
        return null;
    }


    // EFFECTS: return the index if database has the movie with given name
    // or return -1.
    public int findMovie(String name) {
        return -1;
    }

    // below are getters
    public ArrayList<Movie> getDataBase() {
        return null;
    }

}
