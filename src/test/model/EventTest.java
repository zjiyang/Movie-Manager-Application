package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Event class
 */
public class EventTest {
	private Event e;
	private long beforeCreation;
	private long afterCreation;

	@BeforeEach
	public void runBefore() {
		beforeCreation = System.currentTimeMillis();
		e = new Event("Sensor open at door");
		afterCreation = System.currentTimeMillis();
	}

	@Test
	public void testEvent() {
		assertEquals("Sensor open at door", e.getDescription());
		assertTrue(e.getDate().getTime() >= beforeCreation);
		assertTrue(e.getDate().getTime() <= afterCreation);
	}

	@Test
	public void testToString() {
		assertEquals(e.getDate().toString() + "\n" + "Sensor open at door", e.toString());
	}
}
