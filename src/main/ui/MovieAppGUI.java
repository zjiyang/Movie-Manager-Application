package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

//Represents a graphical user interface allow user to act multiple function and interact with database
public class MovieAppGUI extends JFrame {
    private static final String JSON_STORE = "./data/MovieDataBase.json";
    private MovieDataBase movies;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private DefaultListModel<String> movieListModel;
    private JList<String> movieList;
    
    //EFFECTS: constructor create a GUI
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

    //EFFECTS: Adds a menu bar called "File" with load and save options.
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(new LoadMoviesAction()));
        fileMenu.add(new JMenuItem(new SaveMoviesAction()));
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    //EFFECTS: Initializes the movie list panel and adds it to the main window.
    private void addMovieListPanel() {
        movieListModel = new DefaultListModel<>();
        movieList = new JList<>(movieListModel);
        add(new JScrollPane(movieList), BorderLayout.CENTER);
    }

    //EFFECTS: Initializes and adds buttons for adding and rating movies.
    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2));
        buttonPanel.add(new JButton(new AddMovieAction()));
        buttonPanel.add(new JButton(new RateMovieAction()));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //EFFECTS: Prompts user for movie details and adds the movie to the database.
    private class AddMovieAction extends AbstractAction {
        AddMovieAction() {
            super("Add Movie");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            String name = JOptionPane.showInputDialog("Enter movie title:", "MovieName");
            if (name == null || name.trim().isEmpty()) {
                return;
            }
            
            Movie movie = new Movie(name);
            String yearStr = JOptionPane.showInputDialog("Enter year released:","1234");
            movie.setYearReleased(Integer.parseInt(yearStr));
            setGenre(movie);
            rateMovie(movie);
            setStreamService(movie);
            movies.addMovie(movie);
            updateMovieList();
        }
    }

    //MODIFIES: movie
    //EFFECTS: Allows user to select multiple genres for the given movie.
    private void setGenre(Movie movie) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JCheckBox action = new JCheckBox("Action");
        JCheckBox drama = new JCheckBox("Drama");
        JCheckBox comedy = new JCheckBox("Comedy");
        JCheckBox horror = new JCheckBox("Horror");
        JCheckBox romance = new JCheckBox("Romance");

        panel.add(new JLabel("Select Genres:"));
        panel.add(action);
        panel.add(drama);
        panel.add(comedy);
        panel.add(horror);
        panel.add(romance);

        int option = JOptionPane.showConfirmDialog(null, panel, "Select Genres", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            if (action.isSelected()) movie.addGenre(new Genre("Action"));
            if (drama.isSelected()) movie.addGenre(new Genre("Drama"));
            if (comedy.isSelected()) movie.addGenre(new Genre("Comedy"));
            if (horror.isSelected()) movie.addGenre(new Genre("Horror"));
            if (romance.isSelected()) movie.addGenre(new Genre("Romance"));
        }
    }

    //REQUIRES: User ID must be a valid integer.
    //MODIFIES: movie
    //EFFECTS: Prompts user to rate a movie using a slider.
    private void rateMovie(Movie movie) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JTextField userIDField = new JTextField();
        
        // Create a JSlider for selecting the score (0 - 10)    
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5); // Default value: 5
        slider.setPreferredSize(new Dimension(400, 50));
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        
        // Add labels to the slider
        java.util.Hashtable<Integer, JLabel> labelTable = new java.util.Hashtable<>();
        labelTable.put(0, new JLabel("0: Bad"));
        labelTable.put(5, new JLabel("5: Average"));
        labelTable.put(10, new JLabel("10: Excellent"));
        slider.setLabelTable(labelTable);
        
        panel.add(new JLabel("Enter your User ID Number:"));
        panel.add(userIDField);
        panel.add(new JLabel("Select Rating (0 - 10):"));
        panel.add(slider);
        
        int option = JOptionPane.showConfirmDialog(null, panel, "Rate Movie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int userID = Integer.parseInt(userIDField.getText().trim());
                int rateScore = slider.getValue();  // Get the selected value from the slider
                movie.rateMovie(new Rate(userID, rateScore));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid User ID! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                rateMovie(movie);
            }
        }
    }
    //MODIFIES: movie
    //EFFECTS: Allows user to select multiple StreamService for the given movie.
    private void setStreamService(Movie movie) {
        String[] services = {"Netflix", "DisneyPlus", "AppleTV", "PrimeVideo", "Only In Theater"};
        String choice = (String) JOptionPane.showInputDialog(null, "Select stream service:", "Stream Service",
                JOptionPane.QUESTION_MESSAGE, null, services, services[0]);
        if (choice != null) { 
            movie.addStreamService(new StreamService(choice));
        }
    }

    //EFFECTS: Prompts user to rate an existing movie if found in the database.
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

    //EFFECTS: Saves the movie database to a file.
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

    //MODIFIES: movies
    //EFFECTS: Loads the movie database from a file.
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

    //MODIFIES: movieListModel
    //EFFECTS: Updates the movie list display with current movie data.
    private void updateMovieList() {
        movieListModel.clear();
        int index = 1;
        movieListModel.clear();
        for (Movie movie : movies.getDataBase()) {
            double avgScore = movie.averageScore();
            movieListModel.addElement(index + ". " 
                        + movie.getName() + " - " + movie.getYearReleased() + " - Average Score: " + avgScore);
            index++;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieAppGUI::new);
    }
}
