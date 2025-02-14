package ui;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Action;

import model.*;

// Build the application
public class MovieApp {
    private MovieDataBase movies;
    private Scanner input;

    // EFFECTS: runs the application
    public MovieApp() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("1")) {
            addMovie();
        } else if (command.equals("2")) {
            rateFiledMovie();
        } else if (command.equals("3")) {
            viewAllMovie();
        } else if (command.equals("4")) {
            viewRecommendation();
        } else if (command.equals("5")) {
            findStreamService();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes MovieDataBase
    private void init() {
        movies = new MovieDataBase();
        Movie movie1 = new Movie("The Godfather");
        movie1.setYearReleased(1972);
        movie1.addGenre(Genre.Drama);
        movie1.addStreamService(StreamService.Netflix);
        movie1.addStreamService(StreamService.DisneyPlus);
        movie1.rateMovie(Rate.rate8);

        Movie movie2 = new Movie("Black Swan");
        movie2.setYearReleased(2010);
        movie2.addGenre(Genre.Horror);
        movie2.addStreamService(StreamService.Netflix);
        movie2.rateMovie(Rate.rate9);

        movies.addMovie(movie1);
        movies.addMovie(movie2);

        input = new Scanner(System.in);
        input.useDelimiter("\r?\n|\r");
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\t1 -> add a movie");
        System.out.println("\t2 -> rate a movie");
        System.out.println("\t3 -> view all movies");
        System.out.println("\t4 -> view recommendations");
        System.out.println("\t5 -> find stream service for a movie");
    }

    // MODIFIES: this
    // EFFECTS: add a Movie to the database
    private void addMovie() {
        System.out.print("Enter the title: ");
        String name = input.next();
        Movie movie = new Movie(name);
        System.out.print("Enter the year released: ");
        int yearReleased = input.nextInt();
        movie.setYearReleased(yearReleased);
        System.out.print("Let's add a genre to it. ");
        this.setGenre(movie);
        System.out.print("How would you rate the movie? ");
        this.rateMovie(movie);
        System.out.print("Any known Stream service provider? ");
        this.setStreamService(movie);
        movies.addMovie(movie);
        System.out.print("the movie is added to the database!");
    }

    public void setGenre(Movie movie) {
        System.out.print("Add a genre by selecting from: ");
        System.out.println("\ta -> Action");
        System.out.println("\tb -> Drama");
        System.out.println("\tc -> Comedy");
        System.out.println("\td -> Horror");

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
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: rate a movie and display the new updated average score.
    public void rateMovie(Movie movie) {
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

    public void rateFiledMovie() {
        System.out.print("Type the movie title to rate: ");
        String name = input.next();

        int index = movies.findMovie(name);

        if (index == -1) {
            System.out.println("We don't have this movie yet! Please help add it to the database: ");
            addMovie();
        } else {
            rateMovie(movies.getDataBase().get(index));
        }
    }

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

    public void viewRecommendation() {
        System.out.println(this.movies);
    }

    public void setStreamService(Movie movie) {
        System.out.print("Add a stream service by selecting from: ");
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

    public void findStreamService() {
        String name;
        System.out.print("Type the movie title to search: ");
        name = input.next();

        int index = movies.findMovie(name);

        if (index == -1) {
            System.out.println("We don't have this movie yet! Please help add it to the database: ");
            addMovie();
        } else {
            //System.out.println("results: ");
            ArrayList<StreamService> services = movies.getDataBase().get(index).getStreamServices();

            System.out.println("There are " + services.size() + " stream services available: ");
            for (int servicesindex = 0; (servicesindex <= (services.size() - 1)); servicesindex++) {
                System.out.println((servicesindex + 1) + ". " + services.get(servicesindex).getStreamPlatformName());
            }   
        }
    }

}
