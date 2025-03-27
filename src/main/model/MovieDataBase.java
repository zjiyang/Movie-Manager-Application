package model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

// Represents the data base, including all movies on this platform
public class MovieDataBase implements Writable {
    private ArrayList<Movie> dataBase;

    // EFFECTS: Construct a empty movie database for the platform.
    public MovieDataBase() {
        dataBase = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: add given movie to the database.
    public void addMovie(Movie movie) {
        this.dataBase.add(movie);
        EventLog.getInstance().logEvent(new Event("Added movie: " + movie.getName() + " to the Movie Database."));
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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("movies", moviesToJson());
        return json;
    }

    // EFFECTS: returns things in this database as a JSON array
    private JSONArray moviesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Movie movie : this.dataBase) {
            jsonArray.put(movie.toJson());
        }

        return jsonArray;
    }
}

