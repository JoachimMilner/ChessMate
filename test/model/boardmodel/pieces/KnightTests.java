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

public class KnightTests {
	private GameModel gameModel;

	@Before
	public void setUp() {
		gameModel = new GameModel(new ArrayList<IController>());
	}

	// Test knight constructor and core attributes
	@Test
	public void testCreateKnight() {
		IPiece knight = new Knight(gameModel.createNewGameBoard(false, GameType.LOCAL), PieceColour.WHITE, new SquareCoordinate(1, 1));
		assertNotNull(knight);
		assertEquals(PieceType.KNIGHT, knight.getType());
		assertEquals(PieceColour.WHITE, knight.getColour());
		assertEquals(new SquareCoordinate(1, 1), knight.getPosition());
		knight.setPosition(new SquareCoordinate(7, 7));
		assertEquals(new SquareCoordinate(7, 7), knight.getPosition());
	}

	// Test get knight moves from initial game setup
	@Test
	public void testGetMovesFromInitialSetup() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Knight whiteKnight = (Knight) gameBoard.getGrid()[1][0].getPiece();
		whiteKnight.evaluateMoves();
		Set<SquareCoordinate> whiteExpectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(0, 2), new SquareCoordinate(2, 2)));
		Set<SquareCoordinate> whiteAvailableMoves = whiteKnight.getMoves();
		assertNotNull(whiteAvailableMoves);
		assertEquals(whiteExpectedMoves, whiteAvailableMoves);

		Knight blackKnight = (Knight) gameBoard.getGrid()[6][7].getPiece();
		blackKnight.evaluateMoves();
		Set<SquareCoordinate> blackExpectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(7, 5), new SquareCoordinate(5, 5)));
		Set<SquareCoordinate> blackAvailableMoves = blackKnight.getMoves();
		assertNotNull(blackAvailableMoves);
		assertEquals(blackExpectedMoves, blackAvailableMoves);
	}

	// Test get knight moves on empty board
	@Test
	public void testGetMovesEmptyBoard() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 2)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 1)));
		grid[5][2].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(3, 1), new SquareCoordinate(3, 3), new SquareCoordinate(4, 0),
						new SquareCoordinate(4, 4), new SquareCoordinate(6, 0), new SquareCoordinate(6, 4),
						new SquareCoordinate(7, 1), new SquareCoordinate(7, 3)));
		Set<SquareCoordinate> availableMoves = grid[5][2].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get knight moves with friendly and enemy pieces in paths
	@Test
	public void testGetMovesPiecesBlocking() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 5)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(1, 4)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(2, 3)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 4)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(1, 6)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 3)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 7)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(5, 6)));
		grid[3][5].getPiece().evaluateMoves();
		Set<SquareCoordinate> expectedMoves = new HashSet<>(
				Arrays.asList(new SquareCoordinate(1, 6), new SquareCoordinate(2, 7), new SquareCoordinate(4, 3),
						new SquareCoordinate(4, 7), new SquareCoordinate(5, 6)));
		Set<SquareCoordinate> availableMoves = grid[3][5].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(expectedMoves, availableMoves);
	}

	// Test get knight moves with knight blocking check against friendly king
	@Test
	public void testGetMovesWithFriendlyCheckExposure() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.WHITE, new SquareCoordinate(2, 4)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(2, 2)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 6)));
		grid[2][4].getPiece().evaluateMoves();
		Set<SquareCoordinate> availableMoves = grid[2][4].getPiece().getMoves();
		assertNotNull(availableMoves);
		assertEquals(0, availableMoves.size());
	}
}
