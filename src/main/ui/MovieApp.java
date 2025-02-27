package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

// Movie database application
public class MovieApp {
    private static final String JSON_STORE = "./data/MovieDataBase.json";
    private MovieDataBase movies;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the application
    public MovieApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        movies = new MovieDataBase();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runApp();
    }

    public void init() {
        input = new Scanner(System.in);
        System.out.println("load movies from database? (y/n)");
        String loadChoice = input.next().toLowerCase();
        if (loadChoice.equals("y")) {
            loadMovieDataBase();
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {
        input = new Scanner(System.in);
        init();

        boolean keepGoing = true;
        while (keepGoing) {

            displayMenu();
            System.out.print("\nEnter your command: ");
            String command = input.next().toLowerCase();

            if (command.equals("q")) {
                System.out.println("\nDo you want to save your movies added to the file? (y/n)");
                String saveChoice = input.next().toLowerCase();
                if (saveChoice.equals("y")) {
                    saveMovieDataBase();
                    System.out.println("Thanks for your movie contribution!");
                }
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("\nGoodbye!");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\t1 -> add a movie");
        System.out.println("\t2 -> rate a movie");
        System.out.println("\t3 -> view all movies");
        System.out.println("\t4 -> view recommendations");
        System.out.println("\t5 -> find stream service for a movie");
        System.out.println("\t6 -> save current movie database to file");
        System.out.println("\t7 -> load last movie database from file");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("1")) {
            addMovie();
        } else if (command.equals("2")) {
            rateMovie();
        } else if (command.equals("3")) {
            viewAllMovie();
        } else if (command.equals("4")) {
            viewRecommendation();
        } else if (command.equals("5")) {
            findStreamService();
        } else if (command.equals("6")) {
            saveMovieDataBase();
        } else if (command.equals("7")) {
            loadMovieDataBase();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: add a Movie to the database
    private void addMovie() {
        input.nextLine();
        System.out.print("Enter the title: ");
        String name = input.nextLine();
        Movie movie = new Movie(name);

        System.out.print("Enter the year released: ");
        int yearReleased = input.nextInt();
        movie.setYearReleased(yearReleased);

        System.out.print("\nLet's add a genre to it. ");
        this.setGenre(movie);

        System.out.print("How would you rate the movie? ");
        this.rateExistMovie(movie);

        System.out.print("Any known Stream service provider? ");
        this.setStreamService(movie);

        movies.addMovie(movie);
        System.out.print("the movie is added to the database!");
    }

    // MODIFIES: this
    // EFFECTS: prompts user to add a valid Genre type to the given movie
    public void setGenre(Movie movie) {
        System.out.print("\nAdd a genre by selecting from:");
        System.out.println("\ta -> Action");
        System.out.println("\tb -> Drama");
        System.out.println("\tc -> Comedy");
        System.out.println("\td -> Horror");
        System.out.println("\te -> Romance");

        String command;
        command = input.next();
        command = command.toLowerCase();

        if (command.equals("a")) {
            movie.addGenre(Genre.Action);
        } else if (command.equals("b")) {
            movie.addGenre(Genre.Drama);
        } else if (command.equals("c")) {
            movie.addGenre(Genre.Comedy);
        } else if (command.equals("d")) {
            movie.addGenre(Genre.Horror);
        } else if (command.equals("e")) {
            movie.addGenre(Genre.Romance);
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: rate a movie:
    // if user input a new movie, ask user to create the movie first;
    // if user input a existing movie, then add rate to it.
    public void rateMovie() {
        System.out.print("Type the movie title to rate: ");
        String name = input.next();

        int index = movies.findMovie(name);

        if (index == -1) {
            System.out.println("We don't have this movie yet! Please help add it to the database: ");
            addMovie();
        } else {
            rateExistMovie(movies.getDataBase().get(index));
        }
    }

    // MODIFIES: this
    // EFFECTS: rate a existing movie and display the new updated average score.
    // if user hasn't add a rate to the given movie, add the rate.
    // if user rated before, replace the old rate with new rate.
    public void rateExistMovie(Movie movie) {
        int userID;
        System.out.print("Please enter your userID: ");
        userID = input.nextInt();

        int score;
        System.out.print("Your score from 0 to 10: ");
        score = input.nextInt();

        Rate rate = new Rate(userID, score);

        if (movie.ratedBefore(rate)) {
            System.out.println("You rated this movie before with score: " + movie.findScoreWithUserID(userID)
                    + ". " + "Now is updated with your new score!");
            movie.rateMovie(rate);
        } else {
            movie.rateMovie(rate);
        }

        System.out.println("now the new average score is " + movie.averageScore());
    }

    // EFFECTS: display all movies in the database so far with name, year, and
    // average score.
    public void viewAllMovie() {
        ArrayList<Movie> movieList = this.movies.getDataBase();
        System.out.println("Here are the movies on our database:");
        System.out.println("We have " + movieList.size() + " movies: ");
        for (int index = 0; index <= movieList.size() - 1; index++) {
            System.out.println(
                    (index + 1) + ". " + movieList.get(index).getName() + " - " + movieList.get(index).getYearReleased()
                            + " - Average Score: " + movieList.get(index).averageScore());
        }
    }

    // EFFECTS: prompts user to select a Genre type to see the best rated movie
    @SuppressWarnings("methodlength")
    public void viewRecommendation() {
        System.out.print("\nplease filter the list by selecting a genre from: ");
        System.out.println("\ta -> Action");
        System.out.println("\tb -> Drama");
        System.out.println("\tc -> Comedy");
        System.out.println("\td -> Horror");
        System.out.println("\te -> Romance");
        System.out.println("\tf -> All Genres");

        String command;
        command = input.next();
        command = command.toLowerCase();
        String range = "";

        ArrayList<Movie> filteredMovies = new ArrayList<>();

        if (command.equals("a")) {
            filteredMovies = movies.filterMoviebyGenre(Genre.Action);
            range = "in Action";
        } else if (command.equals("b")) {
            filteredMovies = movies.filterMoviebyGenre(Genre.Drama);
            range = "in Drama";
        } else if (command.equals("c")) {
            filteredMovies = movies.filterMoviebyGenre(Genre.Comedy);
            range = "in Comedy";
        } else if (command.equals("d")) {
            filteredMovies = movies.filterMoviebyGenre(Genre.Horror);
            range = "in Horror";
        } else if (command.equals("e")) {
            filteredMovies = movies.filterMoviebyGenre(Genre.Romance);
            range = "in Romance";
        } else if (command.equals("f")) {
            filteredMovies = this.movies.getDataBase();
            range = "over all";
        } else {
            System.out.println("Selection not valid...");
        }
        System.out.print("The top rated movie " + range + " is : " + this.topAveScoreMovie(filteredMovies).getName());
    }

    // EFFECTS: return the top average score movie.
    // the first movie, if same score
    public Movie topAveScoreMovie(ArrayList<Movie> movies) {
        Movie topMovie = null;
        double topScore = 0;
        for (int index = 0; index <= (movies.size() - 1); index++) {
            if (movies.get(index).averageScore() > topScore) {
                topMovie = movies.get(index);
                topScore = movies.get(index).averageScore();
            }
        }
        return topMovie;
    }

    // MODIFIES: this
    // EFFECTS: prompts user to select a valid stream service to the given movie
    public void setStreamService(Movie movie) {
        System.out.print("\nAdd a stream service by selecting from: ");
        System.out.println("\ta -> Netflix");
        System.out.println("\tb -> DisneyPlus");
        System.out.println("\tc -> AppleTV");
        System.out.println("\td -> PrimeVideo");

        String command;
        command = input.next();
        command = command.toLowerCase();

        if (command.equals("a")) {
            movie.addStreamService(StreamService.Netflix);
        } else if (command.equals("b")) {
            movie.addStreamService(StreamService.DisneyPlus);
        } else if (command.equals("c")) {
            movie.addStreamService(StreamService.AppleTV);
        } else if (command.equals("d")) {
            movie.addStreamService(StreamService.PrimeVideo);
        } else {
            System.out.println("Selection not valid...");
        }

    }

    // EFFECTS: return a list of available stream services for the given movie name.
    public void findStreamService() {
        String name;
        System.out.print("Type the movie title to search: ");
        name = input.next();

        int index = movies.findMovie(name);

        if (index == -1) {
            System.out.println("We don't have this movie yet! Please help add it to the database: ");
            addMovie();
        } else {
            // System.out.println("results: ");
            ArrayList<StreamService> services = movies.getDataBase().get(index).getStreamServices();

            System.out.println("There are " + services.size() + " stream services available: ");
            for (int servicesindex = 0; (servicesindex <= (services.size() - 1)); servicesindex++) {
                System.out.println((servicesindex + 1) + ". " + services.get(servicesindex).getStreamPlatformName());
            }
        }
    }

    // EFFECTS: saves the MovieDataBase to file
    private void saveMovieDataBase() {
        try {
            jsonWriter.open();
            jsonWriter.write(movies);
            jsonWriter.close();
            System.out.println("Saved the Movie Database to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads MovieDataBase from file
    private void loadMovieDataBase() {
        try {
            movies = jsonReader.read();
            System.out.println("Loaded Movie Database from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
