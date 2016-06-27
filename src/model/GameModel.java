package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import ai.ChessAI;
import controller.GUIController;
import controller.IController;
import model.boardmodel.Board;
import model.boardmodel.Square;
import model.boardmodel.SquareCoordinate;
import model.boardmodel.pieces.Bishop;
import model.boardmodel.pieces.IPiece;
import model.boardmodel.pieces.King;
import model.boardmodel.pieces.Knight;
import model.boardmodel.pieces.Pawn;
import model.boardmodel.pieces.PieceColour;
import model.boardmodel.pieces.PieceType;
import model.boardmodel.pieces.Queen;
import model.boardmodel.pieces.Rook;

public class GameModel {

	private Board gameBoard;
	private GameType gameType;
	private PieceColour playerColour = null;
	private List<IController> controllers;
	private Deque<String> moveList = new ArrayDeque<>();

	public GameModel(List<IController> controllers) {
		this.controllers = controllers;
	}

	public Board createBoardDeepCopy() {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(gameBoard);

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			return (Board) objectInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Create a board model with pieces in position for a new game
	public Board createNewGameBoard(boolean withPieces, GameType gameType) {
		this.gameType = gameType;
		switch (gameType) {
		case LOCAL:
			playerColour = null;
			break;
		case VS_AI_AS_BLACK:
			playerColour = PieceColour.BLACK;
			break;
		case VS_AI_AS_WHITE:
			playerColour = PieceColour.WHITE;
			break;
		}

		BoardStateManager.getInstance().clearStates();
		moveList.clear();
		gameBoard = new Board(generateNewGameGrid());
		if (withPieces) {
			setPiecesForNewGame();
		}
		refreshUI();
		if (gameType.equals(GameType.VS_AI_AS_BLACK)) {
			ChessAI.makeMove(this);
		}
		return gameBoard;
	}

	public Board getGameBoard() {
		return gameBoard;
	}

	public GameType getGameType() {
		return gameType;
	}

	public PieceColour getPlayerColour() {
		return playerColour;
	}

	public Deque<String> getMoveList() {
		return moveList;
	}

	// Generate a 2d array of squares
	private Square[][] generateNewGameGrid() {
		Square[][] grid = new Square[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				grid[i][j] = new Square();
			}
		}
		return grid;
	}

	// Add black and white pieces to correct positions on board for new game
	private void setPiecesForNewGame() {
		// White pawns
		for (int i = 0; i < 8; i++) {
			gameBoard.addPiece(new Pawn(gameBoard, PieceColour.WHITE, new SquareCoordinate(i, 1)));
		}
		// Black pawns
		for (int i = 0; i < 8; i++) {
			gameBoard.addPiece(new Pawn(gameBoard, PieceColour.BLACK, new SquareCoordinate(i, 6)));
		}

		// White Rooks
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(0, 0)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.WHITE, new SquareCoordinate(7, 0)));

		// Black Rooks
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.BLACK, new SquareCoordinate(0, 7)));
		gameBoard.addPiece(new Rook(gameBoard, PieceColour.BLACK, new SquareCoordinate(7, 7)));

		// White Knights
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.WHITE, new SquareCoordinate(1, 0)));
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.WHITE, new SquareCoordinate(6, 0)));

		// Black Knights
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.BLACK, new SquareCoordinate(1, 7)));
		gameBoard.addPiece(new Knight(gameBoard, PieceColour.BLACK, new SquareCoordinate(6, 7)));

		// White Bishops
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(2, 0)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.WHITE, new SquareCoordinate(5, 0)));

		// Black Bishops
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.BLACK, new SquareCoordinate(2, 7)));
		gameBoard.addPiece(new Bishop(gameBoard, PieceColour.BLACK, new SquareCoordinate(5, 7)));

		// White Queen
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.WHITE, new SquareCoordinate(3, 0)));

		// Black Queen
		gameBoard.addPiece(new Queen(gameBoard, PieceColour.BLACK, new SquareCoordinate(3, 7)));

		// White King
		gameBoard.addPiece(new King(gameBoard, PieceColour.WHITE, new SquareCoordinate(4, 0)));

		// Black King
		gameBoard.addPiece(new King(gameBoard, PieceColour.BLACK, new SquareCoordinate(4, 7)));

		gameBoard.evaluateGameState();
	}

	/**
	 * Should always be used so the board state can be recorded on each turn.
	 * @param from Board coordinate to move piece from
	 * @param to Board coordinate to move piece to
	 */
	public void movePiece(SquareCoordinate from, SquareCoordinate to) {
		BoardStateManager.getInstance().pushState(createBoardDeepCopy());

		// A bit messy but allows formal notation to be recorded properly
		boolean isDirectCapture = gameBoard.getGrid()[to.getX()][to.getY()].hasPiece();
		IPiece movingPiece = gameBoard.getGrid()[from.getX()][from.getY()].getPiece();

		gameBoard.movePiece(from, to);
		if (gameBoard.lastMoveIsPawnPromotion()) {
			gameBoard.addPawnPromotionPiece(getPawnPromotionPieceTypeFromUI(), to);
		}
		gameBoard.evaluateGameState();
		moveList.push(Utils.getMoveNotation(gameBoard, movingPiece, from, to, isDirectCapture));
		refreshUI();
		if (!gameType.equals(GameType.LOCAL) && !gameBoard.getWhoseTurn().equals(playerColour)) {
			ChessAI.makeMove(this);
		}
	}

	public void undoMove() {
		// If playing vs AI, undo 2 moves so it goes back to player's last move.
		// TODO need to fix this for first move when playing vs ai as black
		if (!gameType.equals(GameType.LOCAL) && gameBoard.getWhoseTurn().equals(playerColour)) {
			gameBoard = BoardStateManager.getInstance().popState();
			moveList.pop();
		}
		gameBoard = BoardStateManager.getInstance().popState();
		moveList.pop();
		refreshUI();
	}

	public int getMoveCount() {
		return gameBoard.getMoveCount();
	}

	public PieceColour getWhoseTurn() {
		return gameBoard.getWhoseTurn();
	}

	public GameState getGameState() {
		return gameBoard.getGameState();
	}

	private void refreshUI() {
		for (IController controller : controllers) {
			controller.refreshUI();
		}
	}

	// Hacky way to prompt for pawn promotion piece from UI
	private PieceType getPawnPromotionPieceTypeFromUI() {
		PieceType promotionPieceType = null;
		for (IController controller : controllers) {
			if (controller.getClass().equals(GUIController.class)) {
				GUIController guiController = (GUIController) controller;
				promotionPieceType = guiController.getPawnPromotionPieceType();
			}
		}
		return promotionPieceType;
	}
}
