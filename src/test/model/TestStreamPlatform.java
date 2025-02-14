package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestStreamPlatform {
    private StreamService testStreamPlatform;

    @BeforeEach
    public void runBefore() {
        testStreamPlatform = new StreamService("testStreamPlatform");
    }

    @Test
    public void testConstructor() {
        assertEquals("testStreamPlatform", testStreamPlatform.getStreamPlatformName());
    }

}
