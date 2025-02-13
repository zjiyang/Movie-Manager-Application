package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestGenre {
    private Genre testGenre;

    @BeforeEach
    public void runBefore() {
        testGenre = new Genre("testGenre");
    }

    @Test
    public void testConstructor() {
        assertEquals("testGenre", testGenre.getGenreName());
    }
    

}
