package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controller.IController;
import model.boardmodel.Board;
import model.boardmodel.Square;
import model.boardmodel.SquareCoordinate;
import model.boardmodel.pieces.IPiece;
import model.boardmodel.pieces.King;
import model.boardmodel.pieces.PieceColour;
import model.boardmodel.pieces.PieceType;

public class GameModelTests {
	private GameModel gameModel;

	@Before
	public void setUp() {
		gameModel = new GameModel(new ArrayList<IController>());
	}

	// Test creation of game board model
	@Test
	public void testCreateNewGameBoard() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();

		assertEquals(8, grid.length);
		assertEquals(8, grid[0].length);
	}

	// Test get game board
	@Test
	public void testGetGameBoard() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		assertEquals(gameBoard, gameModel.getGameBoard());
	}

	// Test all pieces are added to correct positions on new game board
	@Test
	public void testAddPiecesToNewBoard() {
		Square[][] grid = gameModel.createNewGameBoard(true, GameType.LOCAL).getGrid();

		int whitePieces = 0;
		int blackPieces = 0;

		// White pawns
		for(int i = 0; i < 8; i++) {
			assertTrue(grid[i][1].getPiece().getType().equals(PieceType.PAWN));
			assertTrue(grid[i][1].getPiece().getColour().equals(PieceColour.WHITE));
			assertEquals(new SquareCoordinate(i, 1), grid[i][1].getPiece().getPosition());
			whitePieces++;
		}
		// Black pawns
		for(int i = 0; i < 8; i++) {
			assertTrue(grid[i][6].getPiece().getType().equals(PieceType.PAWN));
			assertTrue(grid[i][6].getPiece().getColour().equals(PieceColour.BLACK));
			assertEquals(new SquareCoordinate(i, 6), grid[i][6].getPiece().getPosition());
			blackPieces++;
		}

		// White Rooks
		assertTrue(grid[0][0].getPiece().getType().equals(PieceType.ROOK));
		assertTrue(grid[0][0].getPiece().getColour().equals(PieceColour.WHITE));
		assertEquals(new SquareCoordinate(0, 0), grid[0][0].getPiece().getPosition());
		whitePieces++;
		assertTrue(grid[7][0].getPiece().getType().equals(PieceType.ROOK));
		assertTrue(grid[7][0].getPiece().getColour().equals(PieceColour.WHITE));
		assertEquals(new SquareCoordinate(7, 0), grid[7][0].getPiece().getPosition());
		whitePieces++;

		// Black Rooks
		assertTrue(grid[0][7].getPiece().getType().equals(PieceType.ROOK));
		assertTrue(grid[0][7].getPiece().getColour().equals(PieceColour.BLACK));
		assertEquals(new SquareCoordinate(0, 7), grid[0][7].getPiece().getPosition());
		blackPieces++;
		assertTrue(grid[7][7].getPiece().getType().equals(PieceType.ROOK));
		assertTrue(grid[7][7].getPiece().getColour().equals(PieceColour.BLACK));
		assertEquals(new SquareCoordinate(7, 7), grid[7][7].getPiece().getPosition());
		blackPieces++;

		// White Knights
		assertTrue(grid[1][0].getPiece().getType().equals(PieceType.KNIGHT));
		assertTrue(grid[1][0].getPiece().getColour().equals(PieceColour.WHITE));
		assertEquals(new SquareCoordinate(1, 0), grid[1][0].getPiece().getPosition());
		whitePieces++;
		assertTrue(grid[6][0].getPiece().getType().equals(PieceType.KNIGHT));
		assertTrue(grid[6][0].getPiece().getColour().equals(PieceColour.WHITE));
		assertEquals(new SquareCoordinate(6, 0), grid[6][0].getPiece().getPosition());
		whitePieces++;

		// Black Knights
		assertTrue(grid[1][7].getPiece().getType().equals(PieceType.KNIGHT));
		assertTrue(grid[1][7].getPiece().getColour().equals(PieceColour.BLACK));
		assertEquals(new SquareCoordinate(1, 7), grid[1][7].getPiece().getPosition());
		blackPieces++;
		assertTrue(grid[6][7].getPiece().getType().equals(PieceType.KNIGHT));
		assertTrue(grid[6][7].getPiece().getColour().equals(PieceColour.BLACK));
		assertEquals(new SquareCoordinate(6, 7), grid[6][7].getPiece().getPosition());
		blackPieces++;

		// White Bishops
		assertTrue(grid[2][0].getPiece().getType().equals(PieceType.BISHOP));
		assertTrue(grid[2][0].getPiece().getColour().equals(PieceColour.WHITE));
		assertEquals(new SquareCoordinate(2, 0), grid[2][0].getPiece().getPosition());
		whitePieces++;
		assertTrue(grid[5][0].getPiece().getType().equals(PieceType.BISHOP));
		assertTrue(grid[5][0].getPiece().getColour().equals(PieceColour.WHITE));
		assertEquals(new SquareCoordinate(5, 0), grid[5][0].getPiece().getPosition());
		whitePieces++;

		// Black Bishops
		assertTrue(grid[2][7].getPiece().getType().equals(PieceType.BISHOP));
		assertTrue(grid[2][7].getPiece().getColour().equals(PieceColour.BLACK));
		assertEquals(new SquareCoordinate(2, 7), grid[2][7].getPiece().getPosition());
		blackPieces++;
		assertTrue(grid[5][7].getPiece().getType().equals(PieceType.BISHOP));
		assertTrue(grid[5][7].getPiece().getColour().equals(PieceColour.BLACK));
		assertEquals(new SquareCoordinate(5, 7), grid[5][7].getPiece().getPosition());
		blackPieces++;

		// White Queen
		assertTrue(grid[3][0].getPiece().getType().equals(PieceType.QUEEN));
		assertTrue(grid[3][0].getPiece().getColour().equals(PieceColour.WHITE));
		assertEquals(new SquareCoordinate(3, 0), grid[3][0].getPiece().getPosition());
		whitePieces++;

		// Black Queen
		assertTrue(grid[3][7].getPiece().getType().equals(PieceType.QUEEN));
		assertTrue(grid[3][7].getPiece().getColour().equals(PieceColour.BLACK));
		assertEquals(new SquareCoordinate(3, 7), grid[3][7].getPiece().getPosition());
		blackPieces++;

		// White King
		assertTrue(grid[4][0].getPiece().getType().equals(PieceType.KING));
		assertTrue(grid[4][0].getPiece().getColour().equals(PieceColour.WHITE));
		assertEquals(new SquareCoordinate(4, 0), grid[4][0].getPiece().getPosition());
		whitePieces++;

		// Black King
		assertTrue(grid[4][7].getPiece().getType().equals(PieceType.KING));
		assertTrue(grid[4][7].getPiece().getColour().equals(PieceColour.BLACK));
		assertEquals(new SquareCoordinate(4, 7), grid[4][7].getPiece().getPosition());
		blackPieces++;

		assertEquals(16, whitePieces);
		assertEquals(16, blackPieces);
	}

	// Test that the sets of white and black pieces are added to the board object
	@Test
	public void testPieceListsAreAddedToGameBoard() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		List<IPiece> whitePieces = gameBoard.getWhitePieces();
		assertNotNull(whitePieces);
		assertEquals(16, whitePieces.size());

		List<IPiece> blackPieces = gameBoard.getBlackPieces();
		assertNotNull(blackPieces);
		assertEquals(16, blackPieces.size());
	}

	// Test starting a game and getting whose turn it is
	@Test
	public void testGetWhoseTurn() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		assertEquals(PieceColour.WHITE, gameBoard.getWhoseTurn());
		gameBoard.movePiece(new SquareCoordinate(4, 1), new SquareCoordinate(4, 3));
		assertEquals(PieceColour.BLACK, gameBoard.getWhoseTurn());
		gameBoard.movePiece(new SquareCoordinate(4, 6), new SquareCoordinate(4, 4));
		assertEquals(PieceColour.WHITE, gameBoard.getWhoseTurn());
		gameBoard.movePiece(new SquareCoordinate(1, 0), new SquareCoordinate(2, 3));
		assertEquals(PieceColour.BLACK, gameBoard.getWhoseTurn());
	}

	// Test movePiece
	@Test
	public void testMovePieceInGameModel() {
		gameModel.createNewGameBoard(false, GameType.LOCAL);
		Board gameBoard = gameModel.getGameBoard();
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(5, 7)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 3)));
		gameModel.movePiece(new SquareCoordinate(3, 3), new SquareCoordinate(4, 4));
		assertTrue(gameBoard.getGrid()[4][4].hasPiece());
		assertFalse(gameBoard.getGrid()[3][3].hasPiece());
	}

	// Test deep copying game model so memento can be used for move-undo
	@Test
	public void testDeepCopyingGameModel() {
		gameModel.createNewGameBoard(true, GameType.LOCAL);
		long start = System.nanoTime();
		Board boardDeepCopy = gameModel.createBoardDeepCopy();
		long end = System.nanoTime();
		System.out.println("Time taken to deep copy model: " + (end - start) + "ns");
		assertNotNull(boardDeepCopy);
		assertEquals(Board.class, boardDeepCopy.getClass());

		// Change some values to make sure internal objects are different
		// Move a piece
		gameModel.getGameBoard().movePiece(new SquareCoordinate(4, 1), new SquareCoordinate(4, 3));
		assertFalse(gameModel.getGameBoard().getGrid()[4][1].hasPiece());
		assertTrue(gameModel.getGameBoard().getGrid()[4][3].hasPiece());
		assertTrue(boardDeepCopy.getGrid()[4][1].hasPiece());
		assertFalse(boardDeepCopy.getGrid()[4][3].hasPiece());
		IPiece testPiece = boardDeepCopy.getGrid()[4][1].getPiece();
		assertEquals(PieceType.PAWN, testPiece.getType());
		assertEquals(new SquareCoordinate(4, 1), testPiece.getPosition());

		// Check whose turn
		assertEquals(PieceColour.BLACK, gameModel.getGameBoard().getWhoseTurn());
		assertEquals(PieceColour.WHITE, boardDeepCopy.getWhoseTurn());

		// Remove a piece
		gameModel.getGameBoard().removePiece(new SquareCoordinate(3, 1));
		assertFalse(gameModel.getGameBoard().getGrid()[3][1].hasPiece());
		assertEquals(15, gameModel.getGameBoard().getWhitePieces().size());
		assertTrue(boardDeepCopy.getGrid()[3][1].hasPiece());
		assertEquals(16, boardDeepCopy.getWhitePieces().size());

		// Check lists have copied
		assertFalse(gameModel.getGameBoard().getBlackPieces().equals(boardDeepCopy.getBlackPieces()));
	}
}
