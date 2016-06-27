package model.boardmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SquareCoordinateTests {

	@Test
	public void testCreateSquareCoordinate() {
		SquareCoordinate squareCoordinate = new SquareCoordinate(3, 4);
		assertEquals(3, squareCoordinate.getX());
		assertEquals(4, squareCoordinate.getY());
	}

	@Test
	public void testSquareCoordinateHashCode() {
		SquareCoordinate squareCoordinate = new SquareCoordinate(6, 7);
		assertEquals(1154, squareCoordinate.hashCode());

		SquareCoordinate squareCoordinate2 = new SquareCoordinate(5, 2);
		assertEquals(1118, squareCoordinate2.hashCode());
	}

	// Check equals conditions and that coordinates are considered equal if they have the same X&Y
	@Test
	public void testSquareCoordinateEquals() {
		SquareCoordinate squareCoordinate = new SquareCoordinate(3, 4);
		SquareCoordinate squareCoordinate2 = squareCoordinate;
		assertTrue(squareCoordinate.equals(squareCoordinate2));
		assertFalse(squareCoordinate.equals(null));
		assertFalse(squareCoordinate.equals(new Object()));
		squareCoordinate2 = new SquareCoordinate(5, 6);
		assertFalse(squareCoordinate.equals(squareCoordinate2));
		squareCoordinate2 = new SquareCoordinate(3, 6);
		assertFalse(squareCoordinate.equals(squareCoordinate2));
		squareCoordinate2 = new SquareCoordinate(3, 4);
		assertTrue(squareCoordinate.equals(squareCoordinate2));
	}
}
