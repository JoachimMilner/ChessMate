package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.GameModel;
import model.GameType;
import model.boardmodel.pieces.PieceColour;
import model.boardmodel.pieces.PieceType;

public class GUIController implements IController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Pane boardPane;

    @FXML
    private Pane gameControlsPane;

    @FXML
    private Label gameStateLabel;

    @FXML
    private Button undoMoveButton;

    @FXML
    private TextArea moveRecordTextArea;

    private GameModel gameModel;

    private Stage stage;

	@Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		rootPane.getStylesheets().add("/view/gui.css");
		GridPane boardGridPane = null;
		BoardController boardController = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Board.fxml"));
			boardGridPane = fxmlLoader.load();
			boardController = fxmlLoader.<BoardController>getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boardPane.getChildren().setAll(boardGridPane);

		// Add controllers to list to be passed to model
		List<IController> controllers = new ArrayList<>();
		controllers.add(this);
		controllers.add(boardController);
		gameModel = new GameModel(controllers);
		boardController.setGameModel(gameModel);

		undoMoveButton.setDisable(true);
		gameStateLabel.setText("Welcome to ChessMate\nSelect mode to begin");
		gameStateLabel.setAlignment(Pos.CENTER);
		gameStateLabel.setTextAlignment(TextAlignment.CENTER);
		moveRecordTextArea.setEditable(false);

	}

	public void setPrimaryStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	private void createNewLocalGame() {
		createNewGame(GameType.LOCAL);
	}

	@FXML
	private void createNewGameVsAIWhite() {
		createNewGame(GameType.VS_AI_AS_WHITE);
	}

	@FXML
	private void createNewGameVsAIBlack() {
		createNewGame(GameType.VS_AI_AS_BLACK);
	}

	private void createNewGame(GameType gameType) {
		boardPane.getChildren().get(0).setDisable(false);
		gameModel.createNewGameBoard(true, gameType);
		setBoardOrientation();
	}

	@FXML
	private void undoMove() {
		boardPane.getChildren().get(0).setDisable(false);
		gameModel.undoMove();
	}

	@Override
	public void refreshUI() {
		if (gameModel.getMoveCount() > 0) {
			undoMoveButton.setDisable(false);
		} else {
			undoMoveButton.setDisable(true);
		}

		String gameStateText = "";
		switch(gameModel.getGameState()) {
		case CHECK:
			gameStateText += "Check!\n";
		case IN_PLAY:
			gameStateText += gameModel.getWhoseTurn().toLowerCase() + " to move";
			break;
		case CHECK_MATE:
			boardPane.getChildren().get(0).setDisable(true);
			String winningColour = gameModel.getWhoseTurn().equals(PieceColour.WHITE) ? "Black" : "White";
			gameStateText += "Check Mate!\n" + winningColour + " wins";
			break;
		case STALE_MATE:
			boardPane.getChildren().get(0).setDisable(true);
			gameStateText += "Stale Mate!\nDraw";
			break;
		case INSUFFICIENT_MATERIAL:
			boardPane.getChildren().get(0).setDisable(true);
			gameStateText += "Insufficient Material!\nDraw";
			break;
		case FIVEFOLD_REPETITION:
			boardPane.getChildren().get(0).setDisable(true);
			gameStateText += "Fivefold Repetition!\nDraw";
			break;
		}
		gameStateLabel.setText(gameStateText);

		moveRecordTextArea.clear();
		int i = 0;
		for (Iterator<String> dequeIterator = gameModel.getMoveList().descendingIterator(); dequeIterator.hasNext();) {
			String moveString = dequeIterator.next();
			if (i % 2 == 0) {
				moveRecordTextArea.appendText((i / 2 + 1) + ". ");
				moveRecordTextArea.appendText(moveString);
			} else {
				moveRecordTextArea.appendText(" " + moveString + "\n");
			}
			i++;
		}
	}

	public PieceType getPawnPromotionPieceType() {
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.setContentText("Choose a Promotion Piece:");

		PieceColour pieceColour = gameModel.getGameBoard().getWhoseTurn().getEnemyColour();
		ButtonType queenButtonType = new ButtonType("Queen");
		dialog.getDialogPane().getButtonTypes().add(queenButtonType);
		Button queenPromotionButton = (Button) dialog.getDialogPane().lookupButton(queenButtonType);
		/* TODO
		 * Setting the button ID stops the pawn promotion test from breaking on different screen resolutions.
		 * Not happy with this fix but will leave until I think of a better solution.
		*/
		queenPromotionButton.setId("queenPromotionButton");
		queenPromotionButton.setText("");
		Image queenImage = new Image("res/" + pieceColour.toString() + "_QUEEN.png");
		ImageView queenImageView = new ImageView(queenImage);
		queenPromotionButton.setGraphic(queenImageView);

		ButtonType knightButtonType = new ButtonType("Knight");
		dialog.getDialogPane().getButtonTypes().add(knightButtonType);
		Button knightPromotionButton = (Button) dialog.getDialogPane().lookupButton(knightButtonType);
		knightPromotionButton.setText("");
		Image knightImage = new Image("res/" + pieceColour.toString() + "_KNIGHT.png");
		ImageView knightImageView = new ImageView(knightImage);
		knightPromotionButton.setGraphic(knightImageView);

		ButtonType bishopButtonType = new ButtonType("Bishop");
		dialog.getDialogPane().getButtonTypes().add(bishopButtonType);
		Button bishopPromotionButton = (Button) dialog.getDialogPane().lookupButton(bishopButtonType);
		bishopPromotionButton.setText("");
		Image bishopImage = new Image("res/" + pieceColour.toString() + "_BISHOP.png");
		ImageView bishopImageView = new ImageView(bishopImage);
		bishopPromotionButton.setGraphic(bishopImageView);

		ButtonType rookButtonType = new ButtonType("Rook");
		dialog.getDialogPane().getButtonTypes().add(rookButtonType);
		Button rookPromotionButton = (Button) dialog.getDialogPane().lookupButton(rookButtonType);
		rookPromotionButton.setText("");
		Image rookImage = new Image("res/" + pieceColour.toString() + "_ROOK.png");
		ImageView rookImageView = new ImageView(rookImage);
		rookPromotionButton.setGraphic(rookImageView);

		dialog.initStyle(StageStyle.UTILITY);
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);

		Optional<ButtonType> result = dialog.showAndWait();
		switch (result.get().getText()) {
		case "Bishop":
			return PieceType.BISHOP;
		case "Rook":
			return PieceType.ROOK;
		case "Knight":
			return PieceType.KNIGHT;
		case "Queen":
			return PieceType.QUEEN;
		}
		return null;
	}

	private void setBoardOrientation() {
		int rotationDegrees = 0;
		if (gameModel.getPlayerColour() != null && gameModel.getPlayerColour().equals(PieceColour.BLACK)) {
			rotationDegrees = 180;
		}
		GridPane boardGridPane = (GridPane) boardPane.getChildren().get(0);
		boardGridPane.setRotate(rotationDegrees);
		for (Node node : boardGridPane.getChildren()) {
			node.setRotate(rotationDegrees);
		}
	}
}
