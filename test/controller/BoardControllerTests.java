package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.loadui.testfx.GuiTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class BoardControllerTests extends GuiTest {

	//private GUIController guiController;

	@Override
	protected Parent getRootNode() {
        Parent parent = null;
        try {
        	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/GUI.fxml"));
            parent = fxmlLoader.load();
            //guiController = fxmlLoader.<GUIController>getController();
            return parent;
        } catch (IOException ex) {

        }
        return parent;
	}

	@Test
	public void testGridSquaresCreated() {
		GridPane boardGridPane = find("#boardGridPane");
		assertNotNull(boardGridPane);
		assertEquals(64, boardGridPane.getChildren().size());
		int lightSquares = 0;
		int darkSquares = 0;
		for (Node node : boardGridPane.getChildren()) {
			if (node.getStyleClass().contains("light-square")) {
				lightSquares++;
			}
			if (node.getStyleClass().contains("dark-square")) {
				darkSquares++;
			}
		}
		assertEquals(32, lightSquares);
		assertEquals(32, darkSquares);
	}

	@Test
	public void testBoardInitiallyLoaded() {
		Button newGameButton = find("#localGameButton");
		click(newGameButton);
		GridPane boardGridPane = find("#boardGridPane");

		int pieceCount = 0;
		for (Node node :  boardGridPane.getChildren()) {
			Pane pane = (Pane) node;
			if (!pane.getChildren().isEmpty()) {
				pieceCount++;
			}
		}
		assertEquals(32, pieceCount);
	}

	@Test
	public void testDragAndDropPawn() {
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
	}

	@Test
	public void testDragAndDropPawnToInvalidSquare() {
		Button newGameButton = find("#localGameButton");
		click(newGameButton);
		GridPane boardGridPane = find("#boardGridPane");
		Pane pawnSquareMoveFrom = getNodeFromGridPane(boardGridPane, 4, 6);
		assertFalse(pawnSquareMoveFrom.getChildren().isEmpty());
		Pane pawnSquareMoveTo = getNodeFromGridPane(boardGridPane, 4, 3);
		assertTrue(pawnSquareMoveTo.getChildren().isEmpty());

		drag(pawnSquareMoveFrom).to(pawnSquareMoveTo);
		assertFalse(pawnSquareMoveFrom.getChildren().isEmpty());
		assertTrue(pawnSquareMoveTo.getChildren().isEmpty());
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
