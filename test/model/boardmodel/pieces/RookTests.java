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

public class RookTests {
	private GameModel gameModel;

	@Before
	public void setUp() {
		gameModel = new GameModel(new ArrayList<IController>());
	}

	// Test rook constructor and core attributes
	@Test
	public void testCreateRook() {
		IPiece rook = new Rook(gameModel.createNewGameBoard(false, GameType.LOCAL), PieceColour.WHITE, new SquareCoordinate(1, 1));
		assertNotNull(rook);
		assertEquals(PieceType.ROOK, rook.getType());
		assertEquals(PieceColour.WHITE, rook.getColour());
		assertEquals(new SquareCoordinate(1, 1), rook.getPosition());
		rook.setPosition(new SquareCoordinate(7, 7));
		assertEquals(new SquareCoordinate(7, 7), rook.getPosition());
	}

	// Test get rook moves from initial game setup
	@Test
	public void testGetMovesFromInitialSetup() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Rook rook = (Rook) gameBoard.getGrid()[0][0].getPiece();
		rook.evaluateMoves();
		Set<SquareCoordinate> availableMoves = rook.getMoves();
		assertNotNull(availableMoves);
		assertEquals(0, availableMoves.size());
	}

	// Test get rook moves on empty board
	@Test
	public void testGetMovesEmptyBoard() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 5)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		grid[3][5].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(3, 6), new SquareCoordinate(3, 7), new SquareCoordinate(3, 4),
						new SquareCoordinate(3, 3), new SquareCoordinate(3, 2), new SquareCoordinate(3, 1),
						new SquareCoordinate(3, 0), new SquareCoordinate(4, 5), new SquareCoordinate(5, 5),
						new SquareCoordinate(6, 5), new SquareCoordinate(7, 5), new SquareCoordinate(2, 5),
						new SquareCoordinate(1, 5), new SquareCoordinate(0, 5)));
		Set<SquareCoordinate> availableMoves = grid[3][5].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get rook moves with friendly and enemy pieces in paths
	@Test
	public void testGetMovesPiecesBlocking() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 3)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 1)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(1, 3)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 0)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(3, 6)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 3)));
		grid[3][3].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(3, 4), new SquareCoordinate(3, 5), new SquareCoordinate(3, 6),
						new SquareCoordinate(3, 2), new SquareCoordinate(2, 3), new SquareCoordinate(4, 3)));
		Set<SquareCoordinate> availableMoves = grid[3][3].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get rook moves with rook blocking check against friendly king
	@Test
	public void testGetMovesWithFriendlyCheckExposure() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 5)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(6, 5)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.BLACK, new SquareCoordinate(0, 5)));
		grid[5][5].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(4, 5), new SquareCoordinate(3, 5), new SquareCoordinate(2, 5),
						new SquareCoordinate(1, 5), new SquareCoordinate(0, 5)));
		Set<SquareCoordinate> availableMoves = grid[5][5].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}
}
