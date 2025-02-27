package persistence;

import model.Genre;
import model.Movie;
import model.MovieDataBase;
import model.Rate;
import model.StreamService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads MovieDatabase from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads MovieDatabase from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MovieDataBase read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMovieDataBase(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses MovieDatabase from JSON object and returns it
    private MovieDataBase parseMovieDataBase(JSONObject jsonObject) {
        MovieDataBase database = new MovieDataBase();
        addMovies(database, jsonObject);
        return database;
    }

    // MODIFIES: database
    // EFFECTS: parses Movies from JSON object and adds them to MovieDatabase
    private void addMovies(MovieDataBase database, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("movies");
        for (Object json : jsonArray) {
            JSONObject nextMovie = (JSONObject) json;
            addMovie(database, nextMovie);
        }
    }

    // MODIFIES: database
    // EFFECTS: parses Movie from JSON object and adds it to MovieDatabase
    @SuppressWarnings("methodlength")
    private void addMovie(MovieDataBase database, JSONObject jsonObject) {
        // Extract basic fields
        String name = jsonObject.getString("name");
        int yearReleased = jsonObject.getInt("yearReleased");

        // Create and initialize Movie
        Movie movie = new Movie(name);
        movie.setYearReleased(yearReleased);

        // Parse genres (assumed to be an array of strings)
        Map<String, Genre> genreMap = new HashMap<String, Genre>();
        genreMap.put("Action", Genre.Action);
        genreMap.put("Drama", Genre.Drama);
        genreMap.put("Comedy", Genre.Comedy);
        genreMap.put("Horror", Genre.Horror);
        genreMap.put("Romance", Genre.Romance);
        genreMap.put("Sci-Fi", Genre.Sci_Fi);
        genreMap.put("Animation", Genre.Animation);
        genreMap.put("Documentary", Genre.Documentary);

        JSONArray genresArray = jsonObject.getJSONArray("genres");
        for (int i = 0; i < genresArray.length(); i++) {
            String genreName = genresArray.getString(i);
            Genre genre = genreMap.getOrDefault(genreName, new Genre(genreName));
            movie.addGenre(genre);
        }

        // Parse rates (assumed to be an array of JSON objects)
        JSONArray ratesArray = jsonObject.getJSONArray("rates");
        for (int i = 0; i < ratesArray.length(); i++) {
            JSONObject rateJson = ratesArray.getJSONObject(i);
            int userID = rateJson.getInt("userID");
            int score = rateJson.getInt("score");
            movie.rateMovie(new Rate(userID, score));
        }

        // Parse stream services (assumed to be an array of strings)
        Map<String, StreamService> ssMap = new HashMap<String, StreamService>();
        ssMap.put("Netflix", StreamService.Netflix);
        ssMap.put("DisneyPlus", StreamService.DisneyPlus);
        ssMap.put("AppleTV", StreamService.AppleTV);
        ssMap.put("PrimeVideo", StreamService.PrimeVideo);

        JSONArray ssArray = jsonObject.getJSONArray("streamServices");
        for (int i = 0; i < ssArray.length(); i++) {
            String ssName = ssArray.getString(i);
            StreamService streamService = ssMap.getOrDefault(ssName, new StreamService(ssName));
            movie.addStreamService(streamService);
        }

        // Add the fully populated Movie object to the database
        database.addMovie(movie);
    }
}
