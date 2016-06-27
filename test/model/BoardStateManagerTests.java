package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import controller.IController;
import model.boardmodel.SquareCoordinate;
import model.boardmodel.pieces.King;
import model.boardmodel.pieces.PieceColour;
import model.boardmodel.pieces.Queen;

public class BoardStateManagerTests {

	GameModel gameModel;

	@Before
	public void setUp() {
		gameModel = new GameModel(new ArrayList<IController>());
	}

	// Test memento functionality for undo-move button
	@Test
	public void testSaveAndUndoModelState() {
		gameModel.createNewGameBoard(false, GameType.LOCAL);
		//Board gameBoard = gameModel.getGameBoard();
		gameModel.getGameBoard().addPiece(new King(gameModel.getGameBoard(), PieceColour.BLACK, new SquareCoordinate(4, 6)));
		gameModel.getGameBoard().addPiece(new King(gameModel.getGameBoard(), PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameModel.getGameBoard().addPiece(new Queen(gameModel.getGameBoard(), PieceColour.WHITE, new SquareCoordinate(0, 0)));
		gameModel.movePiece(new SquareCoordinate(0, 0), new SquareCoordinate(1, 1));
		assertFalse(gameModel.getGameBoard().getGrid()[0][0].hasPiece());
		assertEquals(gameModel.getGameBoard().getWhoseTurn(), PieceColour.BLACK);

		gameModel.movePiece(new SquareCoordinate(1, 1), new SquareCoordinate(2, 2));
		assertFalse(gameModel.getGameBoard().getGrid()[1][1].hasPiece());
		assertEquals(gameModel.getGameBoard().getWhoseTurn(), PieceColour.WHITE);

		gameModel.movePiece(new SquareCoordinate(2, 2), new SquareCoordinate(3, 3));
		assertFalse(gameModel.getGameBoard().getGrid()[2][2].hasPiece());
		assertTrue(gameModel.getGameBoard().getGrid()[3][3].hasPiece());
		assertEquals(gameModel.getGameBoard().getWhoseTurn(), PieceColour.BLACK);

		gameModel.undoMove();
		assertFalse(gameModel.getGameBoard().getGrid()[3][3].hasPiece());
		assertTrue(gameModel.getGameBoard().getGrid()[2][2].hasPiece());
		assertEquals(gameModel.getGameBoard().getWhoseTurn(), PieceColour.WHITE);

		gameModel.undoMove();
		assertFalse(gameModel.getGameBoard().getGrid()[2][2].hasPiece());
		assertTrue(gameModel.getGameBoard().getGrid()[1][1].hasPiece());
		assertEquals(gameModel.getGameBoard().getWhoseTurn(), PieceColour.BLACK);

		gameModel.undoMove();
		assertFalse(gameModel.getGameBoard().getGrid()[1][1].hasPiece());
		assertEquals(gameModel.getGameBoard().getWhoseTurn(), PieceColour.WHITE);
	}

	// Test that model state manager is cleared when a new game board is set
	@Test
	public void testStateManagerResetsWithNewBoard() {
		gameModel.createNewGameBoard(false, GameType.LOCAL);
		//Board gameBoard = gameModel.getGameBoard();
		gameModel.getGameBoard().addPiece(new King(gameModel.getGameBoard(), PieceColour.BLACK, new SquareCoordinate(4, 6)));
		gameModel.getGameBoard().addPiece(new King(gameModel.getGameBoard(), PieceColour.WHITE, new SquareCoordinate(4, 0)));
		gameModel.getGameBoard().addPiece(new Queen(gameModel.getGameBoard(), PieceColour.WHITE, new SquareCoordinate(0, 0)));
		gameModel.movePiece(new SquareCoordinate(0, 0), new SquareCoordinate(1, 1));
		assertFalse(gameModel.getGameBoard().getGrid()[0][0].hasPiece());

		gameModel.movePiece(new SquareCoordinate(1, 1), new SquareCoordinate(2, 2));
		assertFalse(gameModel.getGameBoard().getGrid()[1][1].hasPiece());
		assertTrue(gameModel.getGameBoard().getGrid()[2][2].hasPiece());

		gameModel.undoMove();
		assertFalse(gameModel.getGameBoard().getGrid()[2][2].hasPiece());
		assertTrue(gameModel.getGameBoard().getGrid()[1][1].hasPiece());

		gameModel.createNewGameBoard(false, GameType.LOCAL);
		assertNull(BoardStateManager.getInstance().popState());
	}
}
