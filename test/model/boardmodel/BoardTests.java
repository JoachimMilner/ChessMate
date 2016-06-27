package model.boardmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import controller.IController;
import model.GameModel;
import model.GameState;
import model.GameType;
import model.boardmodel.pieces.Bishop;
import model.boardmodel.pieces.King;
import model.boardmodel.pieces.Knight;
import model.boardmodel.pieces.Pawn;
import model.boardmodel.pieces.PieceColour;
import model.boardmodel.pieces.PieceType;
import model.boardmodel.pieces.Queen;
import model.boardmodel.pieces.Rook;

public class BoardTests {
	private GameModel gameModel;

	@Before
	public void setUp() {
		gameModel = new GameModel(new ArrayList<IController>());
	}

	// Basic board constructor test
	@Test
	public void testCreateBoard() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		assertNotNull(gameBoard);
		assertNotNull(gameBoard.getGrid());
	}

	// Test adding new piece to the board
	@Test
	public void testAddPiece() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Pawn pawn = new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(0, 0));
		gameBoard.addPiece(pawn);
		assertEquals(pawn, gameBoard.getGrid()[0][0].getPiece());
		assertTrue(gameBoard.getWhitePieces().contains(pawn));
	}

	// Test isUnderAttack testing both piece colours (underAttackBy) and squareCoordinate set
	// with no pieces attacking square
	@Test
	public void testIsUnderAttackStartingSetup() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		assertFalse(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(1, 3)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(1, 3)));
	}

	//////////////////////
	/////// PAWNS ///////
	// Test both colours as pawn's attack is dependent on direction
	// squareCoordinate set and square under attack by white pawn
	@Test
	public void testIsUnderAttackInFrontOfWhitePawn() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 3)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(3, 4)));
	}

	// squareCoordinate set and square under attack by black pawn
	@Test
	public void testIsUnderAttackInFrontOfBlackPawn() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 4)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(5, 3)));
	}

	// squareCoordinate set to square behind a white pawn (not under attack)
	@Test
	public void testIsUnderAttackBehindWhitePawn() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 3)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(5, 2)));
	}

	// squareCoordinate set to square behind black pawn (not under attack)
	@Test
	public void testIsUnderAttackBehindBlackPawn() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 4)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(3, 5)));
	}

	//////////////////////
	/////// ROOKS ///////
	// squareCoordinate set and square under attack by rook
	@Test
	public void testIsUnderAttackInlineWithRook() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(1, 1)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(0, 1)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(1, 0)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(1, 2)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(1, 7)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(2, 1)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(7, 1)));
	}

	// squareCoordinate set, test squares not in line with Rook and in line squares blocked by another piece
	@Test
	public void testIsUnderAttackRookNotInlineAndBehindPiece() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.BLACK, new SquareCoordinate(3, 2)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(5, 2)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(2, 1)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(4, 5)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(0, 7)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(4, 2)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(6, 2)));
	}

	//////////////////////
	////// BISHOPS //////
	// squareCoordinate set and square under attack by bishop
	@Test
	public void testIsUnderAttackBishopDiagonals() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 5)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(0, 0)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(4, 4)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(7, 7)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(7, 3)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(3, 7)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(2, 2)));
	}

	// squareCoordinate set, test squares not diagonal to bishop and diagonal squares blocked by another piece
	@Test
	public void testIsUnderAttackBishopNotDiagonalAndBehindPiece() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.BLACK, new SquareCoordinate(3, 4)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(5, 6)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(1, 4)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(3, 5)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(7, 3)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(6, 7)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(7, 7)));
	}

	//////////////////////
	////// KNIGHTS //////
	// squareCoordinate set, test all squares that knight can attack
	@Test
	public void testIsUnderAttackSquaresThatKnightCanMoveTo() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 2)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(3, 1)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(3, 3)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(4, 0)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(4, 4)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(6, 0)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(6, 4)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(7, 1)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(7, 3)));
	}

	// squareCoordinate set, test some squares that knight can't move to
	@Test
	public void testIsUnderAttackSquaresThatKnightCantMoveTo() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.BLACK, new SquareCoordinate(1, 3)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(1, 0)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(1, 4)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(2, 2)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(4, 4)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(5, 1)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(4, 6)));
	}

	////////////////////
	////// QUEEN //////
	// squareCoordinate set, test squares in line and diagonal to queen
	@Test
	public void testIsUnderAttackQueenInlineAndDiagonal() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.BLACK, new SquareCoordinate(1, 6)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(1, 0)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(1, 5)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(0, 5)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(0, 7)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(1, 7)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(7, 0)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(3, 4)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(5, 6)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(2, 7)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(7, 6)));
	}

	// squareCoordinate set, test squares queen can't move to and behind some blocking pieces
	@Test
	public void testIsUnderAttackSquaresThatQueenCantMoveToAndBlocking() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.WHITE, new SquareCoordinate(1, 2)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.BLACK, new SquareCoordinate(1, 6)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 2)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(0, 0)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(1, 7)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(2, 4)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(4, 4)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(5, 2)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(4, 6)));
	}

	////////////////////
	////// KING ///////
	// squareCoordinate set, test squares under attack by a king
	@Test
	public void testIsUnderAttackSquaresAroundKing() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(6, 4)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(6, 3)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(5, 3)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(5, 4)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(5, 5)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(6, 5)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(7, 5)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(7, 4)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.WHITE, new SquareCoordinate(7, 3)));
	}

	// squareCoordinate set, test some squares not under attack by a king
	@Test
	public void testIsUnderAttackNotAttackedByKing() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(6, 4)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(0, 0)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(7, 7)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(3, 4)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.BLACK, new SquareCoordinate(1, 2)));
	}

	//////////////////////////////
	/// CHECK-SPECIFIC TESTS ////
	// Test getKingsPosition method
	@Test
	public void testGetKingsPositionWhite() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		SquareCoordinate kingsCoords = gameBoard.getKingsPosition(PieceColour.WHITE);
		SquareCoordinate expectedCoords = new SquareCoordinate(4, 0);
		assertEquals(expectedCoords, kingsCoords);
	}

	@Test
	public void testGetKingsPositionBlack() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		SquareCoordinate kingsCoords = gameBoard.getKingsPosition(PieceColour.BLACK);
		SquareCoordinate expectedCoords = new SquareCoordinate(4, 7);
		assertEquals(expectedCoords, kingsCoords);
	}

	// Test some check and non-check scenarios

	@Test
	public void testIsUnderAttackKingInCheck() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(2, 2)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 6)));
		assertTrue(gameBoard.isUnderAttack(PieceColour.BLACK, gameBoard.getKingsPosition(PieceColour.WHITE)));
	}

	@Test
	public void testIsUnderAttackKingNotInCheck() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 3)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 1)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(6, 2)));
		assertFalse(gameBoard.isUnderAttack(PieceColour.WHITE, gameBoard.getKingsPosition(PieceColour.BLACK)));
	}

	//////////////////////////
	/// MOVE PIECE TESTS ////
	@Test
	public void testMovePiece() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		Knight knight = (Knight) grid[1][0].getPiece();
		gameBoard.movePiece(knight.getPosition(), new SquareCoordinate(2, 1));
		assertEquals(knight, grid[2][1].getPiece());
		assertFalse(grid[1][0].hasPiece());
		assertEquals(new SquareCoordinate(2, 1), knight.getPosition());
	}

	///////////////////////////
	/// REMOVE PIECE TESTS ///
	@Test
	public void testRemovePiece() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		// Test removing white piece
		Pawn pawn = (Pawn) grid[4][1].getPiece();
		gameBoard.removePiece(new SquareCoordinate(4, 1));
		assertFalse(grid[4][1].hasPiece());
		assertFalse(gameBoard.getWhitePieces().contains(pawn));

		// Test removing black piece
		Knight knight = (Knight) grid[1][7].getPiece();
		gameBoard.removePiece(new SquareCoordinate(1, 7));
		assertFalse(grid[1][7].hasPiece());
		assertFalse(gameBoard.getBlackPieces().contains(knight));
	}

	//////////////////////////////
	/// CAPTURING PIECE TESTS ///
	// Test that pieces are removed from the board properly when captured
	@Test
	public void testCapturePieceRemoval() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		Pawn whiteTestPawn = (Pawn) grid[4][1].getPiece();
		Pawn blackTestPawn = (Pawn) grid[3][6].getPiece();
		gameBoard.movePiece(whiteTestPawn.getPosition(), new SquareCoordinate(4, 3));
		gameBoard.movePiece(blackTestPawn.getPosition(), new SquareCoordinate(3, 4));
		gameBoard.movePiece(whiteTestPawn.getPosition(), blackTestPawn.getPosition());
		assertFalse(gameBoard.getBlackPieces().contains(blackTestPawn));
		blackTestPawn = (Pawn) grid[2][6].getPiece();
		gameBoard.movePiece(blackTestPawn.getPosition(), new SquareCoordinate(2, 5));
		gameBoard.movePiece(new SquareCoordinate(2, 1), new SquareCoordinate(2, 3));
		gameBoard.movePiece(blackTestPawn.getPosition(), whiteTestPawn.getPosition());
		assertFalse(gameBoard.getWhitePieces().contains(whiteTestPawn));
	}

	///////////////////////
	/// CASTLING TESTS ///
	// This will be called from the controller
	@Test
	public void testCastlingEast() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(0, 0)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 0)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 7)));
		gameBoard.movePiece(new SquareCoordinate(4, 0), new SquareCoordinate(6, 0));
		assertFalse(grid[4][0].hasPiece());
		assertEquals(PieceType.KING, grid[6][0].getPiece().getType());
		assertEquals(PieceColour.WHITE, grid[6][0].getPiece().getColour());
		assertEquals(PieceType.ROOK, grid[5][0].getPiece().getType());
		assertEquals(PieceColour.WHITE, grid[5][0].getPiece().getColour());
		assertEquals(PieceColour.BLACK, gameBoard.getWhoseTurn());
	}

	@Test
	public void testCastlingWest() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(0, 0)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 0)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 7)));
		gameBoard.movePiece(new SquareCoordinate(4, 0), new SquareCoordinate(2, 0));
		assertFalse(grid[4][0].hasPiece());
		assertEquals(PieceType.KING, grid[2][0].getPiece().getType());
		assertEquals(PieceColour.WHITE, grid[2][0].getPiece().getColour());
		assertEquals(PieceType.ROOK, grid[3][0].getPiece().getType());
		assertEquals(PieceColour.WHITE, grid[3][0].getPiece().getColour());
		assertEquals(PieceColour.BLACK, gameBoard.getWhoseTurn());
	}

	////////////////////////
	/// PROMOTION TESTS ///
	// First test that model recognises promotion correctly (superficial as in reality it will notify
	// the UI to prompt the user to choose a promotion piece)
	@Test
	public void testPromotionRecognitionInMovePiece() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 6)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 5)));
		Pawn pawn = (Pawn) grid[5][6].getPiece();
		gameBoard.movePiece(new SquareCoordinate(5, 6), new SquareCoordinate(5, 7));
		assertFalse(grid[5][6].hasPiece());
		assertTrue(gameBoard.lastMoveIsPawnPromotion());
		assertFalse(gameBoard.getWhitePieces().contains(pawn));
		assertEquals(PieceColour.BLACK, gameBoard.getWhoseTurn());
	}

	// Test that pawn is promoted once user has chosen a promotion piece
	@Test
	public void testPromotionInvokedInModelWithQueen() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 6)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 5)));
		Pawn pawn = (Pawn) grid[5][6].getPiece();
		gameBoard.movePiece(new SquareCoordinate(5, 6), new SquareCoordinate(5, 7));
		gameBoard.addPawnPromotionPiece(PieceType.QUEEN, new SquareCoordinate(5, 7));
		assertFalse(grid[5][6].hasPiece());
		assertTrue(grid[5][7].hasPiece());
		assertEquals(PieceType.QUEEN, grid[5][7].getPiece().getType());
		assertEquals(PieceColour.WHITE, grid[5][7].getPiece().getColour());
		assertFalse(gameBoard.getWhitePieces().contains(pawn));
		assertEquals(PieceColour.BLACK, gameBoard.getWhoseTurn());
	}

	@Test
	public void testPromotionInvokedInModelWithBishop() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 6)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 5)));
		Pawn pawn = (Pawn) grid[5][6].getPiece();
		gameBoard.movePiece(new SquareCoordinate(5, 6), new SquareCoordinate(5, 7));
		gameBoard.addPawnPromotionPiece(PieceType.BISHOP, new SquareCoordinate(5, 7));
		assertFalse(grid[5][6].hasPiece());
		assertTrue(grid[5][7].hasPiece());
		assertEquals(PieceType.BISHOP, grid[5][7].getPiece().getType());
		assertEquals(PieceColour.WHITE, grid[5][7].getPiece().getColour());
		assertFalse(gameBoard.getWhitePieces().contains(pawn));
		assertEquals(PieceColour.BLACK, gameBoard.getWhoseTurn());
	}

	@Test
	public void testPromotionInvokedInModelWithKnight() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 6)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 5)));
		Pawn pawn = (Pawn) grid[5][6].getPiece();
		gameBoard.movePiece(new SquareCoordinate(5, 6), new SquareCoordinate(5, 7));
		gameBoard.addPawnPromotionPiece(PieceType.KNIGHT, new SquareCoordinate(5, 7));
		assertFalse(grid[5][6].hasPiece());
		assertTrue(grid[5][7].hasPiece());
		assertEquals(PieceType.KNIGHT, grid[5][7].getPiece().getType());
		assertEquals(PieceColour.WHITE, grid[5][7].getPiece().getColour());
		assertFalse(gameBoard.getWhitePieces().contains(pawn));
		assertEquals(PieceColour.BLACK, gameBoard.getWhoseTurn());
	}

	@Test
	public void testPromotionInvokedInModelWithRook() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 6)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 5)));
		Pawn pawn = (Pawn) grid[5][6].getPiece();
		gameBoard.movePiece(new SquareCoordinate(5, 6), new SquareCoordinate(5, 7));
		gameBoard.addPawnPromotionPiece(PieceType.ROOK, new SquareCoordinate(5, 7));
		assertFalse(grid[5][6].hasPiece());
		assertTrue(grid[5][7].hasPiece());
		assertEquals(PieceType.ROOK, grid[5][7].getPiece().getType());
		assertEquals(PieceColour.WHITE, grid[5][7].getPiece().getColour());
		assertFalse(gameBoard.getWhitePieces().contains(pawn));
		assertEquals(PieceColour.BLACK, gameBoard.getWhoseTurn());
	}

	/////////////////////////
	/// EN PASSANT TESTS ///
	// Test that model recognises en passant (so it removes enemy pawn correctly)
	@Test
	public void testEnPassantDetectedInMovePiece() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		Square[][] grid = gameBoard.getGrid();
		Pawn pawn = (Pawn) grid[4][1].getPiece();
		Pawn enemyPawn = (Pawn) grid[5][6].getPiece();
		gameBoard.movePiece(pawn.getPosition(), new SquareCoordinate(4, 3));
		gameBoard.movePiece(new SquareCoordinate(1, 7), new SquareCoordinate(2, 5));
		gameBoard.movePiece(pawn.getPosition(), new SquareCoordinate(4, 4));
		gameBoard.movePiece(new SquareCoordinate(5, 6), new SquareCoordinate(5, 4));
		gameBoard.movePiece(pawn.getPosition(), new SquareCoordinate(5, 5));
		assertFalse(grid[4][4].hasPiece());
		assertFalse(grid[5][4].hasPiece());
		assertEquals(pawn, grid[5][5].getPiece());
		assertFalse(gameBoard.getBlackPieces().contains(enemyPawn));
		assertEquals(PieceColour.BLACK, gameBoard.getWhoseTurn());
	}

	/////////////////////////
	/// GAME STATE TESTS ///
	// Called by controller after each move to determine whether the game is to continue

	// Test function returns not in check from starting position
	@Test
	public void testGetGameStateNotInCheckFromStartingPosition() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		GameState gameState = gameBoard.getGameState();
		assertEquals(GameState.IN_PLAY, gameState);

		// Move a piece and check for black
		gameBoard.movePiece(new SquareCoordinate(6, 0), new SquareCoordinate(5, 2));
		gameState = gameBoard.getGameState();
		assertEquals(GameState.IN_PLAY, gameState);
	}

	// Test function returns not in check from a different position
	@Test
	public void testGetGameStateNotInCheck() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 2)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(1, 3)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 2)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 3)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 4)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 7)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.BLACK, new SquareCoordinate(3, 7)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 6)));
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.BLACK, new SquareCoordinate(5, 6)));
		gameBoard.evaluateGameState();
		GameState gameState = gameBoard.getGameState();
		assertEquals(GameState.IN_PLAY, gameState);

		gameBoard.movePiece(new SquareCoordinate(7, 4), new SquareCoordinate(5, 6));
		gameState = gameBoard.getGameState();
		assertEquals(GameState.IN_PLAY, gameState);
	}

	// Test function returns in check
	@Test
	public void testGetGameStateInCheck() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 2)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(2, 2)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(6, 3)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 3)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.BLACK, new SquareCoordinate(5, 6)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(3, 5)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.BLACK, new SquareCoordinate(1, 5)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 6)));
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 6)));
		gameBoard.evaluateGameState();
		GameState gameState = gameBoard.getGameState();
		assertEquals(GameState.CHECK, gameState);

		gameBoard.movePiece(new SquareCoordinate(4, 3), new SquareCoordinate(3, 3));
		gameState = gameBoard.getGameState();
		assertEquals(GameState.CHECK, gameState);
	}

	// Test function returns check mate
	@Test
	public void testGetGameStateCheckMate() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 1)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(1, 2)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.BLACK, new SquareCoordinate(5, 2)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 1)));
		gameBoard.evaluateGameState();
		GameState gameState = gameBoard.getGameState();
		assertEquals(GameState.CHECK_MATE, gameState);
	}

	// Test function returns stale mate
	@Test
	public void testGetGameStateStaleMate() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 2)));
		gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 5)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 6)));
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.BLACK, new SquareCoordinate(5, 3)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 1)));
		gameBoard.evaluateGameState();
		GameState gameState = gameBoard.getGameState();
		assertEquals(GameState.STALE_MATE, gameState);
	}

	// Test function returns insufficient material
	@Test
	public void testGetGameStateInsufficientMaterialOnlyKings() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 2)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 6)));
		gameBoard.evaluateGameState();
		GameState gameState = gameBoard.getGameState();
		assertEquals(GameState.INSUFFICIENT_MATERIAL, gameState);
	}

	@Test
	public void testGetGameStateInsufficientMaterialKingAndBishop() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 2)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(6, 2)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 6)));
		gameBoard.evaluateGameState();
		GameState gameState = gameBoard.getGameState();
		assertEquals(GameState.INSUFFICIENT_MATERIAL, gameState);
	}

	@Test
	public void testGetGameStateInsufficientMaterialKingAndKnight() {
		Board gameBoard = gameModel.createNewGameBoard(false, GameType.LOCAL);
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 2)));
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 6)));
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.BLACK, new SquareCoordinate(3, 6)));
		gameBoard.evaluateGameState();
		GameState gameState = gameBoard.getGameState();
		assertEquals(GameState.INSUFFICIENT_MATERIAL, gameState);
	}

	// Test function returns fivefold repetition
	@Test
	public void testGetGameStateFivefoldRepetitionConsecutive() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		gameModel.movePiece(new SquareCoordinate(4, 1), new SquareCoordinate(4, 3));
		gameModel.movePiece(new SquareCoordinate(4, 6), new SquareCoordinate(4, 4));
		gameModel.movePiece(new SquareCoordinate(5, 0), new SquareCoordinate(3, 2));
		gameModel.movePiece(new SquareCoordinate(5, 7), new SquareCoordinate(3, 5));
		gameModel.movePiece(new SquareCoordinate(3, 2), new SquareCoordinate(5, 0));
		gameModel.movePiece(new SquareCoordinate(3, 5), new SquareCoordinate(5, 7));

		gameModel.movePiece(new SquareCoordinate(5, 0), new SquareCoordinate(3, 2));
		gameModel.movePiece(new SquareCoordinate(5, 7), new SquareCoordinate(3, 5));
		gameModel.movePiece(new SquareCoordinate(3, 2), new SquareCoordinate(5, 0));
		gameModel.movePiece(new SquareCoordinate(3, 5), new SquareCoordinate(5, 7));

		gameModel.movePiece(new SquareCoordinate(5, 0), new SquareCoordinate(3, 2));
		gameModel.movePiece(new SquareCoordinate(5, 7), new SquareCoordinate(3, 5));
		gameModel.movePiece(new SquareCoordinate(3, 2), new SquareCoordinate(5, 0));
		gameModel.movePiece(new SquareCoordinate(3, 5), new SquareCoordinate(5, 7));

		gameModel.movePiece(new SquareCoordinate(5, 0), new SquareCoordinate(3, 2));
		gameModel.movePiece(new SquareCoordinate(5, 7), new SquareCoordinate(3, 5));
		gameModel.movePiece(new SquareCoordinate(3, 2), new SquareCoordinate(5, 0));
		gameModel.movePiece(new SquareCoordinate(3, 5), new SquareCoordinate(5, 7));

		GameState gameState = gameBoard.getGameState();
		assertEquals(GameState.FIVEFOLD_REPETITION, gameState);
	}

	@Test
	public void testGetGameStateFivefoldRepetitionNonConsecutive() {
		Board gameBoard = gameModel.createNewGameBoard(true, GameType.LOCAL);
		gameModel.movePiece(new SquareCoordinate(4, 1), new SquareCoordinate(4, 3));
		gameModel.movePiece(new SquareCoordinate(4, 6), new SquareCoordinate(4, 4));
		gameModel.movePiece(new SquareCoordinate(5, 0), new SquareCoordinate(3, 2));
		gameModel.movePiece(new SquareCoordinate(5, 7), new SquareCoordinate(3, 5));
		gameModel.movePiece(new SquareCoordinate(3, 2), new SquareCoordinate(5, 0));
		gameModel.movePiece(new SquareCoordinate(3, 5), new SquareCoordinate(5, 7));

		gameModel.movePiece(new SquareCoordinate(5, 0), new SquareCoordinate(3, 2));
		gameModel.movePiece(new SquareCoordinate(5, 7), new SquareCoordinate(3, 5));
		gameModel.movePiece(new SquareCoordinate(3, 2), new SquareCoordinate(5, 0));
		gameModel.movePiece(new SquareCoordinate(3, 5), new SquareCoordinate(5, 7));

		gameModel.movePiece(new SquareCoordinate(5, 0), new SquareCoordinate(3, 2));
		gameModel.movePiece(new SquareCoordinate(5, 7), new SquareCoordinate(3, 5));
		gameModel.movePiece(new SquareCoordinate(3, 2), new SquareCoordinate(5, 0));
		gameModel.movePiece(new SquareCoordinate(3, 5), new SquareCoordinate(5, 7));

		gameModel.movePiece(new SquareCoordinate(5, 0), new SquareCoordinate(3, 2));
		gameModel.movePiece(new SquareCoordinate(5, 7), new SquareCoordinate(3, 5));
		gameModel.movePiece(new SquareCoordinate(3, 2), new SquareCoordinate(5, 0));
		gameModel.movePiece(new SquareCoordinate(3, 5), new SquareCoordinate(5, 7));

		GameState gameState = gameBoard.getGameState();
		assertEquals(GameState.FIVEFOLD_REPETITION, gameState);
	}
}
