package model;

import java.util.ArrayList;

// Represents all the movie data base of this platform
public class MovieDataBase {
    private ArrayList<Movie> MovieDataBase;

    // EFFECTS: Construct a empty movie database for the platform.
    public MovieDataBase(int userID) {
        MovieDataBase = new ArrayList<>();
    }

}
