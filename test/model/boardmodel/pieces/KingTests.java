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
import model.boardmodel.Square;
import model.boardmodel.SquareCoordinate;

public class KingTests {
	private GameModel gameModel;

	@Before
	public void setUp() {
		gameModel = new GameModel(new ArrayList<IController>());
	}

	// Test king constructor and core attributes
	@Test
	public void testCreateKing() {
		IPiece king = new King(gameModel.createNewGameBoard(false, GameType.LOCAL), PieceColour.WHITE, new SquareCoordinate(1, 1));
		assertNotNull(king);
		assertEquals(PieceType.KING, king.getType());
		assertEquals(PieceColour.WHITE, king.getColour());
		assertEquals(new SquareCoordinate(1, 1), king.getPosition());
		king.setPosition(new SquareCoordinate(7, 7));
		assertEquals(new SquareCoordinate(7, 7), king.getPosition());
	}

	// Test get king moves from initial game setup
	@Test
	public void testGetMovesFromInitialSetup() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		King king = (King) gameBoard.getGrid()[4][0].getPiece();
		king.evaluateMoves();
		Set<SquareCoordinate> availableMoves = king.getMoves();
		assertNotNull(availableMoves);
		assertEquals(0, availableMoves.size());
	}

	// Test get king moves on empty board
	@Test
	public void testGetMovesEmptyBoard() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(2, 2)));
		grid[2][2].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(1, 1), new SquareCoordinate(1, 2), new SquareCoordinate(1, 3),
						new SquareCoordinate(2, 3), new SquareCoordinate(3, 3), new SquareCoordinate(3, 2),
						new SquareCoordinate(3, 1), new SquareCoordinate(2, 1)));
		Set<SquareCoordinate> availableMoves = grid[2][2].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get king moves with friendly pieces blocking and enemy pieces that can be captured
	@Test
	public void testGetMovesPiecesBlocking() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 2)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 1)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 1)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(3, 6)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.BLACK, new SquareCoordinate(3, 3)));
		grid[4][2].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(4, 1), new SquareCoordinate(3, 3), new SquareCoordinate(5, 2)));
		Set<SquareCoordinate> availableMoves = grid[4][2].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get king moves stale mate
	@Test
	public void testGetMovesStaleMate() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(6, 0)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.BLACK, new SquareCoordinate(5, 2)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 4)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 4)));
		grid[6][0].getPiece().evaluateMoves();
		Set<SquareCoordinate> availableMoves = grid[6][0].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(0, availableMoves.size());
	}

	// Test get king moves can castle both sides (white)
	@Test
	public void testGetMovesWhiteKingCastlingBothSides() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.removePiece(new SquareCoordinate(1, 0));
		gameBoard.removePiece(new SquareCoordinate(2, 0));
		gameBoard.removePiece(new SquareCoordinate(3, 0));
		gameBoard.removePiece(new SquareCoordinate(5, 0));
		gameBoard.removePiece(new SquareCoordinate(6, 0));
		grid[4][0].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(2, 0), new SquareCoordinate(3, 0), new SquareCoordinate(5, 0),
						new SquareCoordinate(6, 0)));
		Set<SquareCoordinate> availableMoves = grid[4][0].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get king moves can castle both sides (black)
	@Test
	public void testGetMovesBlackKingCastlingBothSides() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.removePiece(new SquareCoordinate(1, 7));
		gameBoard.removePiece(new SquareCoordinate(2, 7));
		gameBoard.removePiece(new SquareCoordinate(3, 7));
		gameBoard.removePiece(new SquareCoordinate(5, 7));
		gameBoard.removePiece(new SquareCoordinate(6, 7));
		grid[4][7].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(2, 7), new SquareCoordinate(3, 7), new SquareCoordinate(5, 7),
						new SquareCoordinate(6, 7)));
		Set<SquareCoordinate> availableMoves = grid[4][7].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Check king can't castle after he's moved
	@Test
	public void testGetMovesCastlingAfterKingHasMoved() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.removePiece(new SquareCoordinate(1, 0));
		gameBoard.removePiece(new SquareCoordinate(2, 0));
		gameBoard.removePiece(new SquareCoordinate(3, 0));
		gameBoard.removePiece(new SquareCoordinate(5, 0));
		gameBoard.removePiece(new SquareCoordinate(6, 0));
		gameBoard.movePiece(new SquareCoordinate(4, 0), new SquareCoordinate(5, 0));
		gameBoard.movePiece(new SquareCoordinate(5, 0), new SquareCoordinate(4, 0));
		grid[4][0].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(3, 0), new SquareCoordinate(5, 0)));
		Set<SquareCoordinate> availableMoves = grid[4][0].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Check king can't castle after rook has moved
	@Test
	public void testGetMovesCastlingAfterRookHasMoved() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.removePiece(new SquareCoordinate(1, 0));
		gameBoard.removePiece(new SquareCoordinate(2, 0));
		gameBoard.removePiece(new SquareCoordinate(3, 0));
		gameBoard.removePiece(new SquareCoordinate(5, 0));
		gameBoard.removePiece(new SquareCoordinate(6, 0));
		gameBoard.movePiece(new SquareCoordinate(7, 0), new SquareCoordinate(6, 0));
		gameBoard.movePiece(new SquareCoordinate(6, 0), new SquareCoordinate(7, 0));
		grid[4][0].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(2, 0), new SquareCoordinate(3, 0), new SquareCoordinate(5, 0)));
		Set<SquareCoordinate> availableMoves = grid[4][0].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Check king can't castle when in check
	@Test
	public void testGetKingMovesCastlingWhenInCheck() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(0, 0)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 0)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 7)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 3)));
		grid[4][0].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(3, 0), new SquareCoordinate(3, 1), new SquareCoordinate(5, 0),
						new SquareCoordinate(5, 1)));
		Set<SquareCoordinate> availableMoves = grid[4][0].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Check king can't castle when in check
	@Test
	public void testGetKingMovesCastlingWithPathBlocked() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(0, 0)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 0)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 7)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.BLACK, new SquareCoordinate(1, 2)));
		grid[4][0].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(3, 1), new SquareCoordinate(4, 1), new SquareCoordinate(5, 0),
						new SquareCoordinate(5, 1), new SquareCoordinate(6, 0)));
		Set<SquareCoordinate> availableMoves = grid[4][0].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}
}
