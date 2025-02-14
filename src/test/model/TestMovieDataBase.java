package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMovieDataBase {
    MovieDataBase testDataBase;

    private Movie testMovie1;
    private Movie testMovie2;
    private Movie testMovie3;

    private Rate testRate1;
    private Rate testRate2;
    private Rate testRate3;
    private Rate testRate4;
    private Rate testRate5;

    @BeforeEach
    public void runBefore() {
        testDataBase = new MovieDataBase();

        testMovie1 = new Movie("testMovie1");
        testMovie2 = new Movie("testMovie2");
        testMovie3 = new Movie("testMovie3");

        testRate1 = new Rate(00001, 1);
        testRate2 = new Rate(00002, 2);
        testRate3 = new Rate(00003, 3);
        testRate4 = new Rate(00004, 4);
        testRate5 = new Rate(00005, 5);

        testMovie1.addGenre(Genre.Romance);
        testMovie1.addGenre(Genre.Action);

        testMovie2.addGenre(Genre.Romance);
        testMovie2.addGenre(Genre.Sci_Fi);

        testMovie3.addGenre(Genre.Romance);
        testMovie3.addGenre(Genre.Drama);

        testMovie1.rateMovie(testRate1);
        testMovie1.rateMovie(testRate2);

        testMovie2.rateMovie(testRate1);
        testMovie2.rateMovie(testRate2);
        testMovie2.rateMovie(testRate3);

        testMovie3.rateMovie(testRate1);
        testMovie3.rateMovie(testRate2);
        testMovie3.rateMovie(testRate3);
        testMovie3.rateMovie(testRate4);
        testMovie3.rateMovie(testRate5);
    }

    @Test
    public void testConstructor() {
        assertTrue(testDataBase.getDataBase().isEmpty());
    }

    @Test
    public void testFilterMoviebyGenre() {

        testDataBase.addMovie(testMovie1);
        testDataBase.addMovie(testMovie2);
        testDataBase.addMovie(testMovie3);

        assertTrue(testDataBase.filterMoviebyGenre(Genre.Horror).isEmpty());
        assertEquals(3,testDataBase.filterMoviebyGenre(Genre.Romance).size());
        assertEquals(testMovie1,testDataBase.filterMoviebyGenre(Genre.Romance).get(0));
        assertEquals(testMovie2,testDataBase.filterMoviebyGenre(Genre.Romance).get(1));
        assertEquals(testMovie3,testDataBase.filterMoviebyGenre(Genre.Romance).get(2));
        assertEquals(1,testDataBase.filterMoviebyGenre(Genre.Action).size());
        assertEquals(testMovie1,testDataBase.filterMoviebyGenre(Genre.Action).get(0));
    }

    @Test
    public void testFindMovie() {
        testDataBase.addMovie(testMovie1);
        testDataBase.addMovie(testMovie2);
        testDataBase.addMovie(testMovie3);

        assertEquals(0, testDataBase.findMovie("testMovie1"));
        assertEquals(1, testDataBase.findMovie("testMovie2"));
        assertEquals(2, testDataBase.findMovie("testMovie3"));
        assertEquals(-1, testDataBase.findMovie("testMovie4"));
    }

}

