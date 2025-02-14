package model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestMovie {
    private Movie testMovie;
    private Genre testGenre1;
    private Genre testGenre2;
    
    @BeforeEach
    public void runBefore() {
        testMovie = new Movie("testMovie");
    }

    @Test
    public void testConstructor() {
        assertEquals("testMovie", testMovie.getName());
        assertEquals(0, testMovie.getYearReleased());
        assertTrue(testMovie.getGenres().isEmpty());
        assertTrue(testMovie.getScores().isEmpty());
        assertTrue(testMovie.getStreamServices().isEmpty());
    }

    @Test 
    public void testSetYearReleased() {
        testMovie.setYearReleased(1999);
        assertEquals(1999, testMovie.getYearReleased());
    }

    @Test 
    public void testAddGenre() {
        testMovie.addGenre(testGenre1);
        assertEquals(1, testMovie.getGenres().size());
        assertEquals(testGenre1, testMovie.getGenres().get(0));

        testMovie.addGenre(testGenre1);
        assertEquals(1, testMovie.getGenres().size());
        assertEquals(testGenre1, testMovie.getGenres().get(0));

        testMovie.addGenre(testGenre2);
        assertEquals(2, testMovie.getGenres().size());
        assertEquals(testGenre1, testMovie.getGenres().get(0));
        assertEquals(testGenre2, testMovie.getGenres().get(1));
    }
    


}
