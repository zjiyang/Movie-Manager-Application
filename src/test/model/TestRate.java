package model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestRate {
    Rate testRate1;

    @BeforeEach
    public void runBefore() {
        testRate1 = new Rate(22233, 1);
    }

    @Test
    public void testGetUserID() {
        assertEquals(22233, testRate1.getUserID());
    }

    @Test
    public void testGetScore() {
        assertEquals(1, testRate1.getScore());
    }


}


