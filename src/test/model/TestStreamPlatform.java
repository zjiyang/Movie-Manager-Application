package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestStreamPlatform {
    public StreamService testStreamService;

    @BeforeEach
    public void runBefore() {
        testStreamService = new StreamService("testStreamService");
    }

    @Test
    public void testConstructor() {
        assertEquals("testStreamService", testStreamService.getStreamPlatformName());
    }

}
