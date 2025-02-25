package persistence;

import model.MovieDataBase;

import java.io.IOException;

import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MovieDataBase read() throws IOException {
        return null; //stub
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        return ""; //stub
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private MovieDataBase parseMovieDataBase(JSONObject jsonObject) {
        return null; //stub
    }

    // MODIFIES: wr
    // EFFECTS: parses Movies from JSON object and adds them to database
    private void addMovies(MovieDataBase database, JSONObject jsonObject) {
    }

    // MODIFIES: movies
    // EFFECTS: parses Movie from JSON object and adds it to database
    private void addMovie(MovieDataBase database, JSONObject jsonObject) {
    }
}
