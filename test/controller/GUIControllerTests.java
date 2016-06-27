package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Test;
import org.loadui.testfx.GuiTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.GameModel;
import model.boardmodel.pieces.PieceColour;
import model.boardmodel.pieces.PieceType;

public class GUIControllerTests extends GuiTest {

	private GUIController guiController;

	@Override
	protected Parent getRootNode() {
        Parent parent = null;
        try {
        	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/GUI.fxml"));
            parent = fxmlLoader.load();
            guiController = fxmlLoader.<GUIController>getController();
            return parent;
        } catch (IOException ex) {

        }
        return parent;
	}

	// Check GUI and board grid pane have loaded
	@Test
	public void testGUILoaded() {
		assertNotNull(find("#rootPane"));
		assertNotNull(find("#boardGridPane"));
	}

	@Test
	public void testControllerCreatesNewGameInModel() {
		Button newGameButton = find("#localGameButton");
		click(newGameButton);
		Class<?> cls = guiController.getClass();
		Field field = null;
		GameModel gameModel = null;
		try {
			field = cls.getDeclaredField("gameModel");
			field.setAccessible(true);
			gameModel = (GameModel) field.get(guiController);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		assertNotNull(gameModel);
		assertNotNull(gameModel.getGameBoard());
	}

	@Test
	public void testUndoMoveButton() {
		Button newGameButton = find("#localGameButton");
		click(newGameButton);
		GridPane boardGridPane = find("#boardGridPane");
		Pane pawnSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 6);
		assertFalse(pawnSquareMoveFrom.getChildren().isEmpty());
		Pane pawnSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 4);
		assertTrue(pawnSquareMoveTo.getChildren().isEmpty());

		drag(pawnSquareMoveFrom).to(pawnSquareMoveTo);
		assertTrue(pawnSquareMoveFrom.getChildren().isEmpty());
		assertFalse(pawnSquareMoveTo.getChildren().isEmpty());

		Button undoMoveButton = find("#undoMoveButton");
		click(undoMoveButton);
		assertFalse(pawnSquareMoveFrom.getChildren().isEmpty());
		assertTrue(pawnSquareMoveTo.getChildren().isEmpty());
	}

	@Test
	public void testGameStateLabelBasicOutput() {
		Label gameStateLabel = find("#gameStateLabel");
		assertEquals("Welcome to ChessMate\nSelect game mode to begin", gameStateLabel.getText());
		Button newGameButton = find("#localGameButton");
		click(newGameButton);
		assertEquals("White to move", gameStateLabel.getText());

		GridPane boardGridPane = find("#boardGridPane");
		Pane pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 6, 7);
		Pane pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 5, 5);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);
		assertEquals("Black to move", gameStateLabel.getText());

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);
		assertEquals("White to move", gameStateLabel.getText());
	}

	@Test
	public void testGameStateLabelAndRecordDisplaysCheck() {
		Label gameStateLabel = find("#gameStateLabel");
		Button newGameButton = find("#localGameButton");
		click(newGameButton);

		GridPane boardGridPane = find("#boardGridPane");
		Pane pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 6);
		Pane pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 4);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 1, 7);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 2, 5);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 5, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 5, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 7);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 7, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);
		assertEquals("Check!\nBlack to move", gameStateLabel.getText());
		TextArea moveRecordTextArea = find("#moveRecordTextArea");
		assertTrue(moveRecordTextArea.getText().contains("+"));
	}

	@Test
	public void testGameStateLabelAndRecordDisplaysCheckMate() {
		// Hippopotamus mate
		Label gameStateLabel = find("#gameStateLabel");
		Button newGameButton = find("#localGameButton");
		click(newGameButton);

		GridPane boardGridPane = find("#boardGridPane");
		Pane pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 6);
		Pane pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 4);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 6, 7);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 6);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 0);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 7, 4);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 1, 7);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 2, 5);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 1, 0);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 2, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 6, 6);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 6, 5);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 7, 4);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 6, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 6);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 4);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 2, 2);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 4);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 2, 7);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 6, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 4);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 5, 5);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);
		assertEquals("Check Mate!\nBlack wins", gameStateLabel.getText());
		TextArea moveRecordTextArea = find("#moveRecordTextArea");
		assertTrue(moveRecordTextArea.getText().contains("#"));
	}

	@Test
	public void testGameStateLabelAndRecordDisplaysStaleMate() {
		Label gameStateLabel = find("#gameStateLabel");
		Button newGameButton = find("#localGameButton");
		click(newGameButton);

		GridPane boardGridPane = find("#boardGridPane");
		Pane pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 6);
		Pane pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 5);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 0, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 0, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 7);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 7, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 0, 0);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 0, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 7, 3);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 0, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 7, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 7, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 0, 3);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 2, 1);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 0, 2);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 7, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 7, 6);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 7, 4);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 5, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 5, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 2, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 1);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 0);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 5, 1);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 1, 1);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 0);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 5);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 1, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 1, 0);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 5);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 7, 1);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 1, 0);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 2, 0);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 5, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 6, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 2, 0);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);
		assertEquals("Stale Mate!\nDraw", gameStateLabel.getText());
		TextArea moveRecordTextArea = find("#moveRecordTextArea");
		assertTrue(moveRecordTextArea.getText().contains(Character.toString((char)171) + "-" + Character.toString((char)171)));
	}

	@Test
	public void testPawnPromotionFunctionality() {
		Button newGameButton = find("#localGameButton");
		click(newGameButton);

		GridPane boardGridPane = find("#boardGridPane");
		Pane pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 6);
		Pane pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 4);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 6);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 4);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 3);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 4);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 4);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 0);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 2, 6);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 2, 5);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 2);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 0, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 3);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 0);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 1);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 2, 5);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 2, 4);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 2, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 2);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 1);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 1, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 1, 2);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 0);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		move(520, 380);
		click();

		Class<?> cls = guiController.getClass();
		Field field = null;
		GameModel gameModel = null;
		try {
			field = cls.getDeclaredField("gameModel");
			field.setAccessible(true);
			gameModel = (GameModel) field.get(guiController);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		assertNotNull(gameModel);
		assertTrue(gameModel.getGameBoard().getGrid()[3][7].hasPiece());
		assertEquals(PieceColour.WHITE, gameModel.getGameBoard().getGrid()[3][7].getPiece().getColour());
		assertEquals(PieceType.QUEEN, gameModel.getGameBoard().getGrid()[3][7].getPiece().getType());
	}

	@Test
	public void testMoveRecordDisplayed() {
		TextArea moveRecordTextArea = find("#moveRecordTextArea");
		Button newGameButton = find("#localGameButton");
		click(newGameButton);

		GridPane boardGridPane = find("#boardGridPane");
		Pane pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 6);
		Pane pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 4);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		assertEquals("1. e4", moveRecordTextArea.getText());

		pieceSquareMoveFrom = getNodeFromGridPane(boardGridPane, 3, 1);
		pieceSquareMoveTo = getNodeFromGridPane(boardGridPane, 3, 3);
		drag(pieceSquareMoveFrom).to(pieceSquareMoveTo);

		assertEquals("1. e4 d5\n", moveRecordTextArea.getText());
	}

	/**
	 *  Used to get a cell's contents from the board grid pane by index
	 * @param gridPane
	 * @param col
	 * @param row
	 * @return
	 */
	private Pane getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return (Pane) node;
	        }
	    }
	    return null;
	}
}
