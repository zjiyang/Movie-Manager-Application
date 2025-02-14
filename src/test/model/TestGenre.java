package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGenre {
    public Genre testGenre1;

    @BeforeEach
    public void runBefore() {
        testGenre1 = new Genre("testGenre1");
    }

    @Test
    public void testConstructor() {
        assertEquals("testGenre1", testGenre1.getGenreName());
    }
    

}
