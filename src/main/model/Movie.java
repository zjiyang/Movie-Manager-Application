package model;

import java.util.ArrayList;

// Represents an movie having an title name, released year, genres, scores from users, available platforms to stream.
public class Movie {
    String name;
    int yearReleased;
    ArrayList<Genre> genres;
    ArrayList<Rate> rates;
    ArrayList<StreamService> streamServices;

    // REQUIRES: name has a non-zero length
    // EFFECTS: Construct a Movie object with given name, set the released year 0,
    // genres as empty list, rates as empty list, stream platforms as empty list.
    public Movie(String name) {
        this.name = name;
        this.yearReleased = 0;
        this.genres = new ArrayList<>();
        this.rates = new ArrayList<>();
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
        if (!(this.genres.contains(genre))) {
            this.genres.add(genre);
        }
    }

    // EFFECTS: true if this rate's user rated this movie before
    public Boolean ratedBefore(Rate rate) {
        for (Rate currentrate : this.rates) {
            if (currentrate.getUserID() == rate.userID) {
                return true;
            }
        }
        return false;
    }

    // REQUIRES: userID > 0; 0 <= score <= 10,
    // EFFECTS: if userID rated before - change the rate the user rated before,
    // if not - add the score to the list
    public void rateMovie(Rate rate) {
        if (!(this.ratedBefore(rate))) {
            this.rates.add(rate);
        } else {
            for (int index = 0; index <= (this.rates.size() - 1); index++) {
                if (this.rates.get(index).getUserID() == rate.userID) {
                    this.rates.set(index, rate);
                }
            }
        }
    }

    // REQUIRES: rates contains a rate provided by given userID
    // EFFECTS: return the score rated by the given userID
    public int findScoreWithUserID(int userID) {
        int toReturn = -1;
        for (int index = 0; index <= (this.rates.size() - 1); index++) {
            if (this.rates.get(index).getUserID() == userID) {
                toReturn = this.rates.get(index).getScore();
            }
        }
        return toReturn;

    }

    // EFFECTS: return the avergae score of the movie
    public double averageScore() {
        int totalScore = 0;
        double averageScore = 0.11;

        for (Rate currentrate : this.rates) {
            totalScore += currentrate.getScore();
        }
        averageScore = (totalScore / this.rates.size());
        return averageScore;
    }

    // MODIFIES: this
    // EFFECTS: add a genre if it's not been added yet.
    public void addStreamService(StreamService streamService) {
        if (!(this.streamServices.contains(streamService))) {
            this.streamServices.add(streamService);
        }

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

    public ArrayList<Rate> getRates() {
        return this.rates;
    }

    public ArrayList<StreamService> getStreamServices() {
        return this.streamServices;
    }
}
