package controller;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.GameModel;
import model.GameType;
import model.boardmodel.Square;
import model.boardmodel.SquareCoordinate;
import model.boardmodel.pieces.IPiece;

public class BoardController implements IController {

	@FXML
	private GridPane boardGridPane;

	private GameModel gameModel;

	private Pane[][] boardPanes = new Pane[8][8];

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		boardGridPane.getStylesheets().add("/view/board.css");
		for (int x = 0; x < 8; x++) {
			for (int y = 0, yInverse = 7; y < 8; y++, yInverse--) {
				Pane boardSquarePane = new Pane();
				setupDrop(boardSquarePane);
				if ((x + y) % 2 == 0) { // Dark squares
					boardSquarePane.getStyleClass().add("dark-square");
				} else { // Light square
					boardSquarePane.getStyleClass().add("light-square");
				}
				boardGridPane.add(boardSquarePane, x, yInverse);
				boardPanes[x][y] = boardSquarePane;
			}
		}

		// Make sure drag event is cancelled when mouse leaves the board
		// to stop buggy behaviour
		boardGridPane.setOnDragExited(event -> {
			try {
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ESCAPE);
				robot.keyRelease(KeyEvent.VK_ESCAPE);
			} catch (AWTException e) {
				e.printStackTrace();
			}
			refreshUI();
			event.consume();
		});
	}

	public void setGameModel(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	@Override
	public void refreshUI() {
		Square[][] grid = gameModel.getGameBoard().getGrid();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Square square = grid[x][y];
				Pane squarePane = boardPanes[x][y];
				for (Iterator<String> iterator = squarePane.getStyleClass().iterator(); iterator.hasNext();) {
					if (iterator.next().contains("valid-square-")) {
						iterator.remove();
					}
				}
				squarePane.getChildren().clear();
				if (square.hasPiece()) {
					IPiece piece = square.getPiece();
					String imageUrl = "res/" + piece.getColour().toString() + "_" + piece.getType().toString() + ".png";
					Image pieceImage = new Image(imageUrl);
					ImageView imageView = new ImageView(pieceImage);
					imageView.setImage(pieceImage);
					squarePane.getChildren().add(imageView);
					if (gameModel.getGameBoard().getWhoseTurn().equals(piece.getColour())
							&& (gameModel.getGameType().equals(GameType.LOCAL)
									|| piece.getColour().equals(gameModel.getPlayerColour()))) {
						setupClickAndDrag(imageView);
					}
				}
			}
		}
	}

	private void setupClickAndDrag(ImageView imageView) {
		imageView.setOnMousePressed(event -> {
			// Get available moves for piece and highlight squares
			Pane squarePane = (Pane) imageView.getParent();
			int squareX = GridPane.getColumnIndex(squarePane);
			int squareY = getInverse(GridPane.getRowIndex(squarePane));
			Square[][] grid = gameModel.getGameBoard().getGrid();
			Set<SquareCoordinate> availableMovesForPiece = grid[squareX][squareY].getPiece().getMoves();
			for (SquareCoordinate availableMove : availableMovesForPiece) {
				Pane pane = boardPanes[availableMove.getX()][availableMove.getY()];
				if (pane.getStyleClass().contains("light-square")) {
					pane.getStyleClass().add("valid-square-light");
				} else {
					pane.getStyleClass().add("valid-square-dark");
				}
			}
			event.consume();
		});

		imageView.setOnMouseReleased(event -> {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					Pane squarePane = boardPanes[x][y];
					for (Iterator<String> iterator = squarePane.getStyleClass().iterator(); iterator.hasNext();) {
						if (iterator.next().contains("valid-square-")) {
							iterator.remove();
						}
					}
				}
			}
			event.consume();
		});

		imageView.setOnDragDetected(event -> {
			Image image = imageView.getImage();
			if (image == null) {
				event.consume();
			} else {
				Pane squarePane = (Pane) imageView.getParent();
				int squareX = GridPane.getColumnIndex(squarePane);
				int squareY = getInverse(GridPane.getRowIndex(squarePane));
				ClipboardContent clipboardContent = new ClipboardContent();
				clipboardContent.putImage(imageView.getImage());
				clipboardContent.putString("" + squareX + squareY);
				imageView.startDragAndDrop(TransferMode.MOVE).setContent(clipboardContent);
				squarePane.getChildren().clear();
				event.consume();
			}
		});
	}

	private void setupDrop(Pane pane) {
		pane.setOnDragOver(event -> {
			if (!event.getGestureSource().equals(pane) && event.getDragboard().hasImage()) {
				event.acceptTransferModes(TransferMode.MOVE);
			}
			event.consume();
		});

		pane.setOnDragDropped(event -> {
			if (!pane.getStyleClass().contains("valid-square-light")
					&& !pane.getStyleClass().contains("valid-square-dark")) {
				refreshUI();
			} else {
				event.acceptTransferModes(TransferMode.ANY);
				Dragboard dragboard = event.getDragboard();
				int squareX = GridPane.getColumnIndex(pane);
				int squareY = getInverse(GridPane.getRowIndex(pane));
				String moveFromString = dragboard.getString();
				gameModel.movePiece(
						new SquareCoordinate(Character.getNumericValue(moveFromString.charAt(0)),
								Character.getNumericValue(moveFromString.charAt(1))),
						new SquareCoordinate(squareX, squareY));
			}
			event.consume();
		});
	}

	/**
	 * Get the inverse of a coordinate index to account for GridPane's y indexes
	 * being reversed.
	 **/
	private int getInverse(int index) {
		int result = 0;
		for (int i = index; i < 7; i++) {
			result++;
		}
		return result;
	}
}
