package model;

import java.util.ArrayList;

// Represents the data base, including all movies on this platform
public class MovieDataBase {
    private ArrayList<Movie> dataBase;

    // EFFECTS: Construct a empty movie database for the platform.
    public MovieDataBase() {
        dataBase = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: add given movie to the database.
    public void addMovie(Movie movie) {
        this.dataBase.add(movie);
    }

    // EFFECTS: produce a movie list by flitering the given genre
    // is has no movie, return empty list.
    public ArrayList<Movie> filterMoviebyGenre(Genre genre) {
        ArrayList<Movie> filteredMovies = new ArrayList<>();

        for (Movie movie : this.dataBase) {
            if (movie.getGenres().contains(genre)) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }

    // public ArrayList<Movie> rankMovies() {
    // ArrayList<Movie> rankedMovies = new ArrayList<>(dataBase);

    // for (Movie movie : dataBase) {
    // movie.averageScore();
    // }

    // }

    // EFFECTS: return the index if database has the movie with given name
    // or return -1.
    public int findMovie(String name) {
        for (int index = 0; (index <= (this.dataBase.size() - 1)); index++) {
            if (this.dataBase.get(index).getName().equals(name)) {
                return index;
            }
        }
        return -1;
    }

    // below are getters
    public ArrayList<Movie> getDataBase() {
        return this.dataBase;
    }

}
