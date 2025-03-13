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
        labelTable.put(0, new JLabel("Bad"));
        labelTable.put(5, new JLabel("Average"));
        labelTable.put(10, new JLabel("Excellent"));
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
            }
        }
    }

    private void setStreamService(Movie movie) {
        String[] services = {"Netflix", "DisneyPlus", "AppleTV", "PrimeVideo", "Only In Theater"};
        String choice = (String) JOptionPane.showInputDialog(null, "Select stream service:", "Stream Service",
                JOptionPane.QUESTION_MESSAGE, null, services, services[0]);
        if (choice != null) { 
            movie.addStreamService(new StreamService(choice));
        }
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
            movieListModel.addElement(index + ". " 
                        + movie.getName() + " - " + movie.getYearReleased() + " - Average Score: " + avgScore);
            index++;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieAppGUI::new);
    }
}
