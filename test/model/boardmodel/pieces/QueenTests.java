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

public class QueenTests {
	private GameModel gameModel;

	@Before
	public void setUp() {
		gameModel = new GameModel(new ArrayList<IController>());
	}

	// Test queen constructor and core attributes
	@Test
	public void testCreateQueen() {
		IPiece queen = new Queen(gameModel.createNewGameBoard(false, GameType.LOCAL), PieceColour.WHITE, new SquareCoordinate(1, 1));
		assertNotNull(queen);
		assertEquals(PieceType.QUEEN, queen.getType());
		assertEquals(PieceColour.WHITE, queen.getColour());
		assertEquals(new SquareCoordinate(1, 1), queen.getPosition());
		queen.setPosition(new SquareCoordinate(7, 7));
		assertEquals(new SquareCoordinate(7, 7), queen.getPosition());
	}

	// Test get queen moves from initial game setup
	@Test
	public void testGetMovesFromInitialSetup() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Queen queen = (Queen) gameBoard.getGrid()[3][0].getPiece();
		queen.evaluateMoves();
		Set<SquareCoordinate> availableMoves = queen.getMoves();
		assertNotNull(availableMoves);
		assertEquals(0, availableMoves.size());
	}

	// Test get queen moves on empty board
	@Test
	public void testGetMovesEmptyBoard() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 3)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(2, 1)));
		grid[3][3].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(0, 0), new SquareCoordinate(1, 1), new SquareCoordinate(2, 2),
						new SquareCoordinate(3, 2), new SquareCoordinate(3, 1), new SquareCoordinate(3, 0),
						new SquareCoordinate(4, 2), new SquareCoordinate(5, 1), new SquareCoordinate(6, 0),
						new SquareCoordinate(4, 3), new SquareCoordinate(5, 3), new SquareCoordinate(6, 3),
						new SquareCoordinate(7, 3), new SquareCoordinate(4, 4), new SquareCoordinate(5, 5),
						new SquareCoordinate(6, 6), new SquareCoordinate(7, 7), new SquareCoordinate(3, 4),
						new SquareCoordinate(3, 5), new SquareCoordinate(3, 6), new SquareCoordinate(3, 7),
						new SquareCoordinate(2, 4), new SquareCoordinate(1, 5), new SquareCoordinate(0, 6),
						new SquareCoordinate(2, 3), new SquareCoordinate(1, 3), new SquareCoordinate(0, 3)));
		Set<SquareCoordinate> availableMoves = grid[3][3].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get queen moves with friendly and enemy pieces in paths
	@Test
	public void testGetMovesPiecesBlocking() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 2)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(6, 1)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 4)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(5, 5)));
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 3)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 2)));
		grid[5][2].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(4, 1), new SquareCoordinate(3, 0), new SquareCoordinate(5, 1),
						new SquareCoordinate(5, 0), new SquareCoordinate(6, 2), new SquareCoordinate(7, 2),
						new SquareCoordinate(6, 3), new SquareCoordinate(5, 3), new SquareCoordinate(5, 4),
						new SquareCoordinate(5, 5), new SquareCoordinate(4, 3), new SquareCoordinate(4, 2),
						new SquareCoordinate(3, 2), new SquareCoordinate(2, 2)));
		Set<SquareCoordinate> availableMoves = grid[5][2].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get queen moves with queen blocking check against friendly king
	@Test
	public void testGetMovesWithFriendlyCheckExposure() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.WHITE, new SquareCoordinate(2, 1)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 0)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.BLACK, new SquareCoordinate(0, 3)));
		grid[2][1].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(1, 2), new SquareCoordinate(0, 3)));
		Set<SquareCoordinate> availableMoves = grid[2][1].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}
}
