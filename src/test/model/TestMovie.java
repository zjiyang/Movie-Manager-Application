package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestMovie {
    private Movie testMovie;
    private Genre testGenre1;
    private Genre testGenre2;
    private Rate testRate1;
    private Rate testRate2;
    private Rate testRate3;
    private StreamService testService1;
    private StreamService testService2;


    
    @BeforeEach
    public void runBefore() {
        testMovie = new Movie("testMovie");
        testGenre1 = new Genre("testGenre1");
        testGenre2 = new Genre("testGenre2");
        testRate1 = new Rate(22233, 1);
        testRate2 = new Rate(21290, 4);
        testRate3 = new Rate(21290, 3);
        testService1 = new StreamService("testService1");
        testService2 = new StreamService("testService2");

    }

    @Test
    public void testConstructor() {
        assertEquals("testMovie", testMovie.getName());
        assertEquals(0, testMovie.getYearReleased());
        assertTrue(testMovie.getGenres().isEmpty());
        assertTrue(testMovie.getRates().isEmpty());
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

    @Test
    public void testRatedBefore() {
        assertFalse(testMovie.ratedBefore(testRate1));
        testMovie.rateMovie(testRate1);
        assertTrue(testMovie.ratedBefore(testRate1));
    }

    @Test
    public void testRateMovie() {
        testMovie.rateMovie(testRate1);
        assertEquals(1, testMovie.getRates().size());
        assertEquals(testRate1, testMovie.getRates().get(0));
        assertEquals(22233, testMovie.getRates().get(0).getUserID());
        assertEquals(1, testMovie.getRates().get(0).getScore());

        testMovie.rateMovie(testRate2);
        assertEquals(2, testMovie.getRates().size());
        assertEquals(testRate1, testMovie.getRates().get(0));
        assertEquals(testRate2, testMovie.getRates().get(1));
        assertEquals(22233, testMovie.getRates().get(0).getUserID());
        assertEquals(1, testMovie.getRates().get(0).getScore());
        assertEquals(21290, testMovie.getRates().get(1).getUserID());
        assertEquals(4, testMovie.getRates().get(1).getScore());

        testMovie.rateMovie(testRate3);
        assertEquals(2, testMovie.getRates().size());
        assertEquals(testRate1, testMovie.getRates().get(0));
        assertEquals(testRate3, testMovie.getRates().get(1));
        assertEquals(22233, testMovie.getRates().get(0).getUserID());
        assertEquals(1, testMovie.getRates().get(0).getScore());
        assertEquals(21290, testMovie.getRates().get(1).getUserID());
        assertEquals(3, testMovie.getRates().get(1).getScore());
    }
    
    @Test
    public void testAddStreamService() {
        testMovie.addStreamService(testService1);
        assertEquals(1, testMovie.getStreamServices().size());
        assertEquals(testService1, testMovie.getStreamServices().get(0));

        testMovie.addStreamService(testService1);
        assertEquals(1, testMovie.getStreamServices().size());
        assertEquals(testService1, testMovie.getStreamServices().get(0));

        testMovie.addStreamService(testService2);
        assertEquals(2, testMovie.getStreamServices().size());
        assertEquals(testService1, testMovie.getStreamServices().get(0));
        assertEquals(testService2, testMovie.getStreamServices().get(1));
    }

    @Test
    public void testAverageScore() {
        testMovie.rateMovie(testRate1);
        double avescore = 1;
        assertEquals(avescore, testMovie.averageScore(), 0.01);

        testMovie.rateMovie(testRate2);
        avescore = (1+4)/2;
        assertEquals(avescore, testMovie.averageScore(), 0.01);
    }

    @Test
    public void testFindScoreWithUserID() {
        testMovie.rateMovie(testRate1);
        testMovie.rateMovie(testRate2);
        assertEquals(1, testMovie.findScoreWithUserID(22233));
        assertEquals(4, testMovie.findScoreWithUserID(21290));

        testMovie.rateMovie(testRate3);
        assertEquals(3, testMovie.findScoreWithUserID(21290));


    }

}
