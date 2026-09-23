package persistence;

import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    @TempDir
    Path tempDirectory;

    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            MovieDataBase database = new MovieDataBase();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyMovieDataBase() {
        try {
            MovieDataBase database = new MovieDataBase();
            String destination = tempDirectory.resolve("empty.json").toString();
            JsonWriter writer = new JsonWriter(destination);
            writer.open();
            writer.write(database);
            writer.close();

            JsonReader reader = new JsonReader(destination);
            database = reader.read();
            assertTrue(database.getDataBase().isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralMovieDataBase() {
        try {
            MovieDataBase database = new MovieDataBase();

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

            database.addMovie(m1);
            database.addMovie(m2);

            String destination = tempDirectory.resolve("movies.json").toString();
            JsonWriter writer = new JsonWriter(destination);
            writer.open();
            writer.write(database);
            writer.close();

            JsonReader reader = new JsonReader(destination);
            database = reader.read();
            ArrayList<Movie> movies;
            movies = database.getDataBase();

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
            testSS2.add(StreamService.AppleTV);

            assertEquals(2, movies.size());
            checkMovie(movies.get(0), "m1", 2024, testGenres1, testRates1, testSS1);
            checkMovie(movies.get(1), "m2", 2025, testGenres2, testRates2, testSS2);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
