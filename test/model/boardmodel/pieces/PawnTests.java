package model.boardmodel.pieces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import controller.IController;
import model.GameModel;
import model.GameType;
import model.boardmodel.Board;
import model.boardmodel.SquareCoordinate;

public class PawnTests {
	private GameModel gameModel;

	@Before
	public void setUp() {
		gameModel = new GameModel(new ArrayList<IController>());
	}

	// Test pawn constructor and core attributes
	@Test
	public void testCreatePawn() {
		IPiece pawn = new Pawn(gameModel.createNewGameBoard(false, GameType.LOCAL), PieceColour.WHITE, new SquareCoordinate(1, 1));
		assertNotNull(pawn);
		assertEquals(PieceType.PAWN, pawn.getType());
		assertEquals(PieceColour.WHITE, pawn.getColour());
		assertEquals(new SquareCoordinate(1, 1), pawn.getPosition());
		pawn.setPosition(new SquareCoordinate(7, 7));
		assertEquals(new SquareCoordinate(7, 7), pawn.getPosition());
	}

	// Test get pawn moves from initial game setup (black and white pawn)
	@Test
	public void testGetMovesFromInitialSetup() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Pawn whitePawn = (Pawn) gameBoard.getGrid()[4][1].getPiece();
		whitePawn.evaluateMoves();
		Set<SquareCoordinate> whiteExpectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(4, 2), new SquareCoordinate(4, 3)));
		Set<SquareCoordinate> whiteAvailableMoves = whitePawn.getMoves();
		assertNotNull(whiteAvailableMoves);
		assertEquals(whiteExpectedMoves, whiteAvailableMoves);

		Pawn blackPawn = (Pawn) gameBoard.getGrid()[5][6].getPiece();
		blackPawn.evaluateMoves();
		Set<SquareCoordinate> blackExpectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(5, 5), new SquareCoordinate(5, 4)));
		Set<SquareCoordinate> blackAvailableMoves = blackPawn.getMoves();
		assertNotNull(blackAvailableMoves);
		assertEquals(blackExpectedMoves, blackAvailableMoves);
	}

	// Test get pawn moves after pawn has moved (no enemy pieces in path)
	@Test
	public void testGetMovesAfterPawnHasMoved() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Pawn pawn = (Pawn) gameBoard.getGrid()[3][1].getPiece();
		gameBoard.movePiece(pawn.getPosition(), new SquareCoordinate(3, 2));
		pawn.evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(3, 3)));
		Set<SquareCoordinate> availableMoves = pawn.getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get pawn moves with pieces blocking and enemy pieces in path to capture
	@Test
	public void testGetMovesPiecesBlocking() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Pawn pawn = (Pawn) gameBoard.getGrid()[4][1].getPiece();
		gameBoard.movePiece(pawn.getPosition(), new SquareCoordinate(4, 3));
		gameBoard.movePiece(new SquareCoordinate(3, 6), new SquareCoordinate(3, 4));
		gameBoard.movePiece(new SquareCoordinate(5, 1), new SquareCoordinate(5, 4));
		pawn.evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(4, 4), new SquareCoordinate(3, 4)));
		Set<SquareCoordinate> availableMoves = pawn.getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get pawn moves with pawn blocking check against friendly king
	@Test
	public void testGetMovesWithFriendlyCheckExposure() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		gameBoard.movePiece(new SquareCoordinate(4, 1), new SquareCoordinate(4, 3));
		gameBoard.movePiece(new SquareCoordinate(4, 6), new SquareCoordinate(4, 4));
		gameBoard.movePiece(new SquareCoordinate(3, 0), new SquareCoordinate(7, 4));
		Pawn pawn = (Pawn) gameBoard.getGrid()[5][6].getPiece();
		pawn.evaluateMoves();
		Set<SquareCoordinate> availableMoves = pawn.getMoves();
		assertNotNull(availableMoves);
		assertEquals(0, availableMoves.size());
	}

	// Test en passant
	@Test
	public void testGetMovesEnPassant() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Pawn pawn = (Pawn) gameBoard.getGrid()[4][1].getPiece();
		gameBoard.movePiece(pawn.getPosition(), new SquareCoordinate(4, 3));
		gameBoard.movePiece(new SquareCoordinate(1, 7), new SquareCoordinate(2, 5));
		gameBoard.movePiece(pawn.getPosition(), new SquareCoordinate(4, 4));
		gameBoard.movePiece(new SquareCoordinate(3, 6), new SquareCoordinate(3, 4));
		pawn.evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(4, 5), new SquareCoordinate(3, 5)));
		Set<SquareCoordinate> availableMoves = pawn.getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);

		// Check pawn can't move en passant after first availability
		gameBoard.movePiece(new SquareCoordinate(3, 1), new SquareCoordinate(3, 2));
		gameBoard.movePiece(new SquareCoordinate(6, 7), new SquareCoordinate(5, 5));
		pawn.evaluateMoves();
		expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(4, 5), new SquareCoordinate(5, 5)));
		availableMoves = pawn.getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	@Test
	public void testGetMovesEnPassantBothSides() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Pawn pawn = (Pawn) gameBoard.getGrid()[4][1].getPiece();
		gameBoard.movePiece(pawn.getPosition(), new SquareCoordinate(4, 3));
		gameBoard.movePiece(new SquareCoordinate(1, 7), new SquareCoordinate(2, 5));
		gameBoard.movePiece(pawn.getPosition(), new SquareCoordinate(4, 4));
		gameBoard.movePiece(new SquareCoordinate(3, 6), new SquareCoordinate(3, 4));
		pawn.evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(4, 5), new SquareCoordinate(3, 5)));
		Set<SquareCoordinate> availableMoves = pawn.getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);

		// Check pawn moves en passant on opposite side
		gameBoard.movePiece(new SquareCoordinate(3, 1), new SquareCoordinate(3, 2));
		gameBoard.movePiece(new SquareCoordinate(5, 6), new SquareCoordinate(5, 4));
		pawn.evaluateMoves();
		expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(4, 5), new SquareCoordinate(5, 5)));
		availableMoves = pawn.getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}
}
