package model;

import java.util.ArrayList;

// Represents an movie having an title name, released year, genres, scores from users, available platforms to stream.
public class Movie {
    String name;
    int yearReleased;
    ArrayList<Genre> genres;
    ArrayList<Integer> scores;
    ArrayList<StreamService> streamServices;

    // REQUIRES: name has a non-zero length
    // EFFECTS: Construct a Movie object with given name, set the released year 0,
    // genres as empty list, rates as empty list, stream platforms as empty list.
    public Movie(String name) {
        this.name = name;
        this.yearReleased = 0;
        this.genres = new ArrayList<>();
        this.scores = new ArrayList<>();
        this.streamServices = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: set the released year 
    public void setYearReleased(int yearReleased) {
        this.yearReleased = yearReleased;
    }

    // MODIFIES: this
    // EFFECTS: add a genre if it's not been added yet.
    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

    // MODIFIES: this
    // EFFECTS: add a genre if it's not been added yet.
    public void addStreamServices(StreamService streamService) {
        this.streamServices.add(streamService);
    }

    // below are getters
    public String getName() {
        return this.name;
    }

    public int getYearReleased() {
        return this.yearReleased;
    }

    public ArrayList<Genre> getGenres() {
        return this.genres;
    }

    public ArrayList<Integer> getScores() {
        return this.scores;
    }

    public ArrayList<StreamService> getStreamServices() {
        return this.streamServices;
    }
}
