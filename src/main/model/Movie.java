package model;

import java.util.ArrayList;

// Represents an movie having an title name, released year, genres, scores from users, available platforms to stream.
public class Movie {
    

    // REQUIRES: name has a non-zero length
    // EFFECTS: Construct a Movie object with given name, set the released year 0,
    // genres as empty list, rates as empty list, stream platforms as empty list.
    public Movie(String name) {
  
    }

    // MODIFIES: this
    // EFFECTS: set the released year
    public void setYearReleased(int yearReleased) {
        
    }

    // MODIFIES: this
    // EFFECTS: add a genre if it's not been added yet.
    public void addGenre(Genre genre) {
    }

    // EFFECTS: true if this rate's user rated this movie before
    public Boolean ratedBefore(Rate rate) {
        return false;

    }

    // REQUIRES: userID > 0; 0 <= score <= 10,
    // EFFECTS: if userID rated before - change the rate the user rated before,
    // if not - add the score to the list
    public void rateMovie(Rate rate) {

    }

    // REQUIRES: rates contains a rate provided by given userID
    // EFFECTS: return the score rated by the given userID
    public int findScoreWithUserID(int userID) {
        return 1;
    }


    // EFFECTS: return the avergae score of the movie
    public double averageScore() {
        return 0.01;
    }

    // MODIFIES: this
    // EFFECTS: add a genre if it's not been added yet.
    public void addStreamService(StreamService streamService) {
  
    }

    // below are getters
    public String getName() {
        return "";
    }

    public int getYearReleased() {
        return -1;
    }

    public ArrayList<Genre> getGenres() {
        return null;

    }

    public ArrayList<Rate> getRates() {
        return null;
    }

    public ArrayList<StreamService> getStreamServices() {
        return null;
    }
}
