package model.boardmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.boardmodel.pieces.IPiece;
import model.boardmodel.pieces.PieceColour;
import model.boardmodel.pieces.Rook;

public class SquareTests {

	@Test
	public void testCreateSquare() {
		Square square = new Square();
		IPiece piece = new Rook(null, PieceColour.BLACK, new SquareCoordinate(0, 0));
		square.setPiece(piece);
		assertNotNull(square);
		assertEquals(piece, square.getPiece());
		assertTrue(square.hasPiece());

		square = new Square();
		assertNull(square.getPiece());
		assertFalse(square.hasPiece());
	}
}
