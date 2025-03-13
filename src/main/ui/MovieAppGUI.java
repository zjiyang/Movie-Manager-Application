package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MovieAppGUI extends JFrame {
    private static final String JSON_STORE = "./data/MovieDataBase.json";
    private MovieDataBase movies;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private DefaultListModel<String> movieListModel;
    private JList<String> movieList;
    
    public MovieAppGUI() {
        super("Movie Database");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        movies = new MovieDataBase();

        addMenu();
        addMovieListPanel();
        addButtonPanel();

        setVisible(true);
    }

    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(new LoadMoviesAction()));
        fileMenu.add(new JMenuItem(new SaveMoviesAction()));
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void addMovieListPanel() {
        movieListModel = new DefaultListModel<>();
        movieList = new JList<>(movieListModel);
        add(new JScrollPane(movieList), BorderLayout.CENTER);
    }

    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
        buttonPanel.add(new JButton(new AddMovieAction()));
        buttonPanel.add(new JButton(new RateMovieAction()));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class AddMovieAction extends AbstractAction {
        AddMovieAction() {
            super("Add Movie");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String name = JOptionPane.showInputDialog("Enter movie title:");
            if (name == null || name.trim().isEmpty()) return;
            
            Movie movie = new Movie(name);
            String yearStr = JOptionPane.showInputDialog("Enter year released:");
            //try { movie.setYearReleased(Integer.parseInt(yearStr)); } catch (NumberFormatException e) {}

            try { 
                int year = Integer.parseInt(yearStr);
                movie.setYearReleased(year);
                System.out.println("DEBUG: Year stored in object -> " + movie.getYearReleased()); // Add this for debugging
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid year! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            setGenre(movie);
            rateMovie(movie);
            setStreamService(movie);
            movies.addMovie(movie);
            updateMovieList();
        }
    }

    private void setGenre(Movie movie) {
        String[] genres = {"Action", "Drama", "Comedy", "Horror", "Romance"};
        String choice = (String) JOptionPane.showInputDialog(null, "Select genre:", "Genre",
                JOptionPane.QUESTION_MESSAGE, null, genres, genres[0]);
        if (choice != null) movie.addGenre(new Genre(choice));
    }

    private void rateMovie(Movie movie) {
        String userID = JOptionPane.showInputDialog("Rate the movie by entering your User ID:");
        try { movie.setYearReleased(Integer.parseInt(userID)); } catch (NumberFormatException e) {}

        String rateScore = JOptionPane.showInputDialog("Your score from 0 to 10: ");
        try { movie.setYearReleased(Integer.parseInt(rateScore)); } catch (NumberFormatException e) {}
        
        movie.rateMovie(new Rate(Integer.parseInt(userID), Integer.parseInt(rateScore)));
    }

    private void setStreamService(Movie movie) {
        String[] services = {"Netflix", "DisneyPlus", "AppleTV", "PrimeVideo"};
        String choice = (String) JOptionPane.showInputDialog(null, "Select stream service:", "Stream Service",
                JOptionPane.QUESTION_MESSAGE, null, services, services[0]);
        if (choice != null) movie.addStreamService(new StreamService(choice));
    }

    private class RateMovieAction extends AbstractAction {
        RateMovieAction() {
            super("Rate Movie");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String name = JOptionPane.showInputDialog("Enter movie title to rate:");
            int index = movies.findMovie(name);
            if (index == -1) {
                JOptionPane.showMessageDialog(null, "Movie not found.");
                return;
            }
            Movie movie = movies.getDataBase().get(index);
            int userID = Integer.parseInt(JOptionPane.showInputDialog("Enter your user ID:"));
            int score = Integer.parseInt(JOptionPane.showInputDialog("Enter rating (0-10):"));
            movie.rateMovie(new Rate(userID, score));
            updateMovieList();
        }
    }

    private class SaveMoviesAction extends AbstractAction {
        SaveMoviesAction() {
            super("Save Movies");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                jsonWriter.open();
                jsonWriter.write(movies);
                jsonWriter.close();
                JOptionPane.showMessageDialog(null, "Movies saved successfully!");
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error saving movies.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class LoadMoviesAction extends AbstractAction {
        LoadMoviesAction() {
            super("Load Movies");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                movies = jsonReader.read();
                updateMovieList();
                JOptionPane.showMessageDialog(null, "Movies loaded successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error loading movies.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateMovieList() {
        movieListModel.clear();
        int index = 1;
        movieListModel.clear();
        for (Movie movie : movies.getDataBase()) {
            double avgScore = movie.averageScore();
            movieListModel.addElement(index + ". " + movie.getName() + " - " + movie.getYearReleased() + " - Average Score: " + avgScore);
            index++;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieAppGUI::new);
    }
}
