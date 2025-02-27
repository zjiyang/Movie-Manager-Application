package persistence;

import org.junit.jupiter.api.Test;

import model.Genre;
import model.Movie;
import model.MovieDataBase;
import model.Rate;
import model.StreamService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            MovieDataBase dataBase = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testEmptyMovieDataBase.json");
        try {
            MovieDataBase dataBase = reader.read();
            assertTrue(dataBase.getDataBase().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testGeneralMovieDataBase.json");
        try {
            MovieDataBase dataBase = reader.read();
            ArrayList<Movie> movies = dataBase.getDataBase();

            Rate testRate1 = new Rate(00001, 5);
            Rate testRate2 = new Rate(00001, 6);

            Movie m1 = new Movie("m1");
            m1.setYearReleased(2024);
            m1.addGenre(Genre.Drama);
            m1.rateMovie(testRate1);
            m1.addStreamService(StreamService.Netflix);
            m1.addStreamService(StreamService.DisneyPlus);

            Movie m2 = new Movie("m2");
            m2.setYearReleased(2025);
            m2.addGenre(Genre.Action);
            m2.rateMovie(testRate2);
            m2.addStreamService(StreamService.AppleTV);

            ArrayList<Genre> testGenres1 = new ArrayList<Genre>();
            testGenres1.add(Genre.Drama);
            ArrayList<Genre> testGenres2 = new ArrayList<Genre>();
            testGenres2.add(Genre.Action);

            ArrayList<Rate> testRates1 = new ArrayList<Rate>();
            ArrayList<Rate> testRates2 = new ArrayList<Rate>();
            testRates1.add(testRate1);
            testRates2.add(testRate2);

            ArrayList<StreamService> testSS1 = new ArrayList<StreamService>();
            testSS1.add(StreamService.Netflix);
            testSS1.add(StreamService.DisneyPlus);
            ArrayList<StreamService> testSS2 = new ArrayList<StreamService>();
            testSS1.add(StreamService.AppleTV);

            dataBase.addMovie(m1);
            dataBase.addMovie(m2);

            assertEquals(2, movies.size());
            checkMovie(movies.get(0), "m1", 2024, testGenres1, testRates1, testSS1);
            checkMovie(movies.get(1), "m2", 2025, testGenres2, testRates2, testSS2);

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}