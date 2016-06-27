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

public class BishopTests {
	private GameModel gameModel;

	@Before
	public void setUp() {
		gameModel = new GameModel(new ArrayList<IController>());
	}

	// Test bishop constructor and core attributes
	@Test
	public void testCreateBishop() {
		IPiece bishop = new Bishop(gameModel.createNewGameBoard(false, GameType.LOCAL), PieceColour.WHITE, new SquareCoordinate(1, 1));
		assertNotNull(bishop);
		assertEquals(PieceType.BISHOP, bishop.getType());
		assertEquals(PieceColour.WHITE, bishop.getColour());
		assertEquals(new SquareCoordinate(1, 1), bishop.getPosition());
		bishop.setPosition(new SquareCoordinate(7, 7));
		assertEquals(new SquareCoordinate(7, 7), bishop.getPosition());
	}

	// Test get bishop moves from initial game setup
	@Test
	public void testGetMovesFromInitialSetup() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Bishop bishop = (Bishop) gameBoard.getGrid()[2][0].getPiece();
		bishop.evaluateMoves();
		Set<SquareCoordinate> availableMoves = bishop.getMoves();
		assertNotNull(availableMoves);
		assertEquals(0, availableMoves.size());
	}

	// Test get bishop moves on empty board
	@Test
	public void testGetMovesEmptyBoard() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 5)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		grid[4][5].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(5, 6), new SquareCoordinate(6, 7), new SquareCoordinate(3, 4),
						new SquareCoordinate(2, 3), new SquareCoordinate(1, 2), new SquareCoordinate(0, 1),
						new SquareCoordinate(3, 6), new SquareCoordinate(2, 7), new SquareCoordinate(5, 4),
						new SquareCoordinate(6, 3), new SquareCoordinate(7, 2)));
		Set<SquareCoordinate> availableMoves = grid[4][5].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get bishop moves with friendly and enemy pieces in paths
	@Test
	public void testGetMovesPiecesBlocking() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 4)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(6, 6)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 1)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(3, 5)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.BLACK, new SquareCoordinate(1, 1)));
		grid[4][4].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(3, 3), new SquareCoordinate(2, 2), new SquareCoordinate(1, 1),
						new SquareCoordinate(3, 5), new SquareCoordinate(5, 5), new SquareCoordinate(5, 3),
						new SquareCoordinate(6, 2)));
		Set<SquareCoordinate> availableMoves = grid[4][4].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get bishop moves with rook blocking check against friendly king
	@Test
	public void testGetMovesWithFriendlyCheckExposure() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 2)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(6, 0)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 4)));
		grid[4][2].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(5, 1), new SquareCoordinate(3, 3), new SquareCoordinate(2, 4)));
		Set<SquareCoordinate> availableMoves = grid[4][2].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}
}
