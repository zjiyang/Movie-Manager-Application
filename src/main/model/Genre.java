package model;

//Represents genre of a movie 
public class Genre {
    private String name;

    // EFFECTS: Construct a genre with given name.
    public Genre(String name) {
        this.name = name;
    }

    public String getGenreName() {
        return this.name;
    }
    
    
    // Define commonly used genres as public static final objects
    public static final Genre Action = new Genre("Action");
    public static final Genre Romance = new Genre("Romance");
    public static final Genre Drama = new Genre("Drama");
    public static final Genre Comedy = new Genre("Comedy");
    public static final Genre Horror = new Genre("Horror");
    public static final Genre Sci_Fi = new Genre("Sci-Fi");
    public static final Genre Animation = new Genre("Animation");
    public static final Genre Documentary = new Genre("Documentary");
}

