package model.boardmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.BoardStateManager;
import model.GameState;
import model.boardmodel.pieces.Bishop;
import model.boardmodel.pieces.IPiece;
import model.boardmodel.pieces.Knight;
import model.boardmodel.pieces.PieceColour;
import model.boardmodel.pieces.PieceType;
import model.boardmodel.pieces.Queen;
import model.boardmodel.pieces.Rook;

public class Board implements Serializable {

	private static final long serialVersionUID = -8725714643234247009L;
	private Square[][] grid;
	private List<IPiece> whitePieces = new ArrayList<>();
	private List<IPiece> blackPieces = new ArrayList<>();
	private PieceColour whoseTurn;
	private int moveCount = 0;
	private GameState gameState;
	private String boardStringRepresentation;
	private boolean lastMoveIsPawnPromotion;

	public Board(Square[][] grid) {
		this.grid = grid;
		whoseTurn = PieceColour.WHITE;
		generateBoardStringRepresentation();
	}

	public Square[][] getGrid() {
		return grid;
	}

	public List<IPiece> getWhitePieces() {
		return whitePieces;
	}

	public List<IPiece> getBlackPieces() {
		return blackPieces;
	}

	public PieceColour getWhoseTurn() {
		return whoseTurn;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public GameState getGameState() {
		return gameState;
	}

	public String getBoardStringRepresentation() {
		return boardStringRepresentation;
	}

	public boolean lastMoveIsPawnPromotion() {
		return lastMoveIsPawnPromotion;
	}

	public void addPiece(IPiece piece) {
		grid[piece.getPosition().getX()][piece.getPosition().getY()].setPiece(piece);
		switch (piece.getColour()) {
		case BLACK:
			blackPieces.add(piece);
			break;
		case WHITE:
			whitePieces.add(piece);
			break;
		}
	}

	/**
	 * <b>Should not be called directly.</b> <br/>(Use movePiece in the GameModel class)
	 **/
	public void movePiece(SquareCoordinate from, SquareCoordinate to) {
		lastMoveIsPawnPromotion = false;
		Square moveFrom = grid[from.getX()][from.getY()];
		IPiece pieceToMove = moveFrom.getPiece();
		Square moveTo = grid[to.getX()][to.getY()];

		// Check if king is castling
		if (pieceToMove.getType().equals(PieceType.KING) && Math.abs(from.getX() - to.getX()) == 2) {
			moveRookForCastle(from, to);
		}
		// Check for pawn special moves
		if (pieceToMove.getType().equals(PieceType.PAWN)) {
			if (!moveTo.hasPiece() && from.getX() != to.getX()) { // En passant
				removePiece(new SquareCoordinate(to.getX(), from.getY()));
			}
			if ((pieceToMove.getColour().equals(PieceColour.WHITE) && to.getY() == 7 // Pawn promotion
					|| pieceToMove.getColour().equals(PieceColour.BLACK) && to.getY() == 0)) {
				removePiece(from);
				lastMoveIsPawnPromotion = true;
			}
		}

		if (moveTo.hasPiece()) {
			removePiece(to);
		}
		moveTo.setPiece(pieceToMove);
		moveFrom.setPiece(null);
		pieceToMove.setPosition(to);
		whoseTurn = whoseTurn.getEnemyColour();
		moveCount++;
		generateBoardStringRepresentation();
	}

	public void removePiece(SquareCoordinate position) {
		IPiece pieceToRemove = grid[position.getX()][position.getY()].getPiece();
		List<IPiece> listToRemoveFrom = pieceToRemove.getColour().equals(PieceColour.WHITE) ? whitePieces : blackPieces;
		listToRemoveFrom.remove(pieceToRemove);
		grid[position.getX()][position.getY()].setPiece(null);
	}

	private void moveRookForCastle(SquareCoordinate kingFrom, SquareCoordinate kingTo) {
		// Determine which side to castle and get the correct rook
		Square moveFrom = null;
		Square moveTo = null;
		IPiece rook = null;
		if (kingFrom.getX() > kingTo.getX()) {
			moveFrom = grid[0][kingFrom.getY()];
			moveTo = grid[3][kingFrom.getY()];
			rook = moveFrom.getPiece();
		} else {
			moveFrom = grid[7][kingFrom.getY()];
			moveTo = grid[5][kingFrom.getY()];
			rook = moveFrom.getPiece();
		}
		moveTo.setPiece(rook);
		moveFrom.setPiece(null);
		rook.setPosition(new SquareCoordinate(5, kingFrom.getY()));
	}

	@SuppressWarnings("incomplete-switch") // Missing enum values guarded against in controller
	public void addPawnPromotionPiece(PieceType pieceType, SquareCoordinate promotionPosition) {
		IPiece promotionPiece = null;
		switch(pieceType) {
		case BISHOP:
			promotionPiece = new Bishop(this, getWhoseTurn().getEnemyColour(), promotionPosition);
			break;
		case KNIGHT:
			promotionPiece = new Knight(this, getWhoseTurn().getEnemyColour(), promotionPosition);
			break;
		case QUEEN:
			promotionPiece = new Queen(this, getWhoseTurn().getEnemyColour(), promotionPosition);
			break;
		case ROOK:
			promotionPiece = new Rook(this, getWhoseTurn().getEnemyColour(), promotionPosition);
			break;
		}
		addPiece(promotionPiece);
	}

	// Evaluate moves for all pieces of whose turn it is to play and determine the game state
	public void evaluateGameState() {
		List<IPiece> piecesToEvaluate = whoseTurn.equals(PieceColour.WHITE) ? whitePieces : blackPieces;
		boolean canMove = false;
		boolean inCheck = false;
		for (IPiece piece : piecesToEvaluate) {
			piece.evaluateMoves();
			if (!piece.getMoves().isEmpty()) {
				canMove = true;
			}
		}
		if (isUnderAttack(whoseTurn.getEnemyColour(), getKingsPosition(whoseTurn))) {
			inCheck = true;
		}
		if (inCheck && canMove) {
			gameState = GameState.CHECK;
		} else if (inCheck && !canMove) {
			gameState = GameState.CHECK_MATE;
		} else if (!inCheck && !canMove) {
			gameState = GameState.STALE_MATE;
		} else {
			gameState = GameState.IN_PLAY;
		}

		checkForInsufficientMaterial();

		if (BoardStateManager.getInstance().positionHasOccurredFivefold(boardStringRepresentation)) {
			gameState = GameState.FIVEFOLD_REPETITION;
		}
	}

	private void checkForInsufficientMaterial()  {
		if (whitePieces.size() == 1 && blackPieces.size() == 1) {
			gameState = GameState.INSUFFICIENT_MATERIAL;
		}

		if (whitePieces.size() == 1 && blackPieces.size() == 2) {
			for (IPiece piece : blackPieces) {
				if (piece.getType().equals(PieceType.BISHOP) || piece.getType().equals(PieceType.KNIGHT)) {
					gameState = GameState.INSUFFICIENT_MATERIAL;
				}
			}
		}

		if (blackPieces.size() == 1 && whitePieces.size() == 2) {
			for (IPiece piece : whitePieces) {
				if (piece.getType().equals(PieceType.BISHOP) || piece.getType().equals(PieceType.KNIGHT)) {
					gameState = GameState.INSUFFICIENT_MATERIAL;
				}
			}
		}
	}

	// Get a king's coordinates from a given colour
	public SquareCoordinate getKingsPosition(PieceColour colour) {
		SquareCoordinate position = null;
		switch (colour) {
		case BLACK:
			for (IPiece piece : blackPieces) {
				if (piece.getType().equals(PieceType.KING)) {
					position = piece.getPosition();
				}
			}
			break;
		case WHITE:
			for (IPiece piece : whitePieces) {
				if (piece.getType().equals(PieceType.KING)) {
					position = piece.getPosition();
				}
			}
			break;
		}
		return position;
	}

	// Generate a string representation of the current board to allow easy comparison (for 5-fold repetition)
	private void generateBoardStringRepresentation() {
		String stateString = "";
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				Square square = grid[x][y];
				if (!square.hasPiece()) {
					stateString += "-";
				} else {
					char pieceChar = ' ';
					switch(square.getPiece().getType()) {
					case BISHOP:
						pieceChar = 'b';
						break;
					case KING:
						pieceChar = 'k';
						break;
					case KNIGHT:
						pieceChar = 'n';
						break;
					case PAWN:
						pieceChar = 'p';
						break;
					case QUEEN:
						pieceChar = 'q';
						break;
					case ROOK:
						pieceChar = 'r';
						break;
					}
					stateString += square.getPiece().getColour().equals(PieceColour.WHITE) ? Character.toUpperCase(pieceChar) : Character.toLowerCase(pieceChar);
				}
			}
			stateString += "\n";
		}
		boardStringRepresentation = stateString;
	}

	// Determine whether a square on the board is under attack
	public boolean isUnderAttack(PieceColour underAttackBy, SquareCoordinate position) {
		// Check for pawns first as attack is dependent on direction
		if (isUnderAttackByPawn(underAttackBy, position)) {
			return true;
		}

		// Check for rooks and queen vertical/horizontal
		if (isUnderAttackByRookOrQueen(underAttackBy, position)) {
			return true;
		}

		// Check for knights
		if (isUnderAttackByKnight(underAttackBy, position)) {
			return true;
		}

		// Check for bishops and queen diagonal
		if (isUnderAttackByBishopOrQueen(underAttackBy, position)) {
			return true;
		}

		// Check for king
		if (isUnderAttackByKing(underAttackBy, position)) {
			return true;
		}

		return false;
	}

	private boolean isUnderAttackByPawn(PieceColour underAttackBy, SquareCoordinate position) {
		Square potentialPawn = null;
		int movementDirection = underAttackBy.equals(PieceColour.WHITE) ? -1 : 1;
		boolean yWithinBounds = underAttackBy.equals(PieceColour.BLACK) ? position.getY() < 6 : position.getY() > 1;
		if (position.getX() > 0 && yWithinBounds) {
			potentialPawn = grid[position.getX() - 1][position.getY() + 1 * movementDirection];
			if (potentialPawn.hasPiece() && potentialPawn.getPiece().getColour().equals(underAttackBy)
					&& potentialPawn.getPiece().getType().equals(PieceType.PAWN)) {
				return true;
			}
		}
		if (position.getX() < 7 && yWithinBounds) {
			potentialPawn = grid[position.getX() + 1][position.getY() + 1 * movementDirection];
			if (potentialPawn.hasPiece() && potentialPawn.getPiece().getColour().equals(underAttackBy)
					&& potentialPawn.getPiece().getType().equals(PieceType.PAWN)) {
				return true;
			}
		}
		return false;
	}

	private boolean isUnderAttackByRookOrQueen(PieceColour underAttackBy, SquareCoordinate position) {
		Square potentialRookOrQueen = null;
		for (int x = position.getX() + 1; x <= 7; x++) { // Check East
			potentialRookOrQueen = grid[x][position.getY()];
			if (potentialRookOrQueen.hasPiece()) {
				if (potentialRookOrQueen.getPiece().getColour().equals(underAttackBy)
						&& (potentialRookOrQueen.getPiece().getType().equals(PieceType.ROOK)
								|| potentialRookOrQueen.getPiece().getType().equals(PieceType.QUEEN))) {
					return true;
				} else break;
			}
		}

		for (int x = position.getX() - 1; x >= 0; x--) { // Check West
			potentialRookOrQueen = grid[x][position.getY()];
			if (potentialRookOrQueen.hasPiece()) {
				if (potentialRookOrQueen.getPiece().getColour().equals(underAttackBy)
						&& (potentialRookOrQueen.getPiece().getType().equals(PieceType.ROOK)
								|| potentialRookOrQueen.getPiece().getType().equals(PieceType.QUEEN))) {
					return true;
				} else break;
			}
		}

		for (int y = position.getY() + 1; y <= 7; y++) { // Check North
			potentialRookOrQueen = grid[position.getX()][y];
			if (potentialRookOrQueen.hasPiece()) {
				if (potentialRookOrQueen.getPiece().getColour().equals(underAttackBy)
						&& (potentialRookOrQueen.getPiece().getType().equals(PieceType.ROOK)
								|| potentialRookOrQueen.getPiece().getType().equals(PieceType.QUEEN))) {
					return true;
				} else break;
			}
		}

		for (int y = position.getY() - 1; y >= 0; y--) { // Check South
			potentialRookOrQueen = grid[position.getX()][y];
			if (potentialRookOrQueen.hasPiece()) {
				if (potentialRookOrQueen.getPiece().getColour().equals(underAttackBy)
						&& (potentialRookOrQueen.getPiece().getType().equals(PieceType.ROOK)
								|| potentialRookOrQueen.getPiece().getType().equals(PieceType.QUEEN))) {
					return true;
				} else break;
			}
		}
		return false;
	}

	private boolean isUnderAttackByKnight(PieceColour underAttackBy, SquareCoordinate position) {
		Square potentialKnight = null;
		if (position.getX() > 1 && position.getY() > 0) {
			potentialKnight = grid[position.getX() - 2][position.getY() - 1];
			if (potentialKnight.hasPiece() && potentialKnight.getPiece().getColour().equals(underAttackBy)
					&& potentialKnight.getPiece().getType().equals(PieceType.KNIGHT)) {
				return true;
			}
		}
		if (position.getX() > 1 && position.getY() < 7) {
			potentialKnight = grid[position.getX() - 2][position.getY() + 1];
			if (potentialKnight.hasPiece() && potentialKnight.getPiece().getColour().equals(underAttackBy)
					&& potentialKnight.getPiece().getType().equals(PieceType.KNIGHT)) {
				return true;
			}
		}
		if (position.getX() > 0 && position.getY() > 1) {
			potentialKnight = grid[position.getX() - 1][position.getY() - 2];
			if (potentialKnight.hasPiece() && potentialKnight.getPiece().getColour().equals(underAttackBy)
					&& potentialKnight.getPiece().getType().equals(PieceType.KNIGHT)) {
				return true;
			}
		}
		if (position.getX() > 0 && position.getY() < 6) {
			potentialKnight = grid[position.getX() - 1][position.getY() + 2];
			if (potentialKnight.hasPiece() && potentialKnight.getPiece().getColour().equals(underAttackBy)
					&& potentialKnight.getPiece().getType().equals(PieceType.KNIGHT)) {
				return true;
			}
		}
		if (position.getX() < 7 && position.getY() > 1) {
			potentialKnight = grid[position.getX() + 1][position.getY() - 2];
			if (potentialKnight.hasPiece() && potentialKnight.getPiece().getColour().equals(underAttackBy)
					&& potentialKnight.getPiece().getType().equals(PieceType.KNIGHT)) {
				return true;
			}
		}
		if (position.getX() < 7 && position.getY() < 6) {
			potentialKnight = grid[position.getX() + 1][position.getY() + 2];
			if (potentialKnight.hasPiece() && potentialKnight.getPiece().getColour().equals(underAttackBy)
					&& potentialKnight.getPiece().getType().equals(PieceType.KNIGHT)) {
				return true;
			}
		}
		if (position.getX() < 6 && position.getY() > 0) {
			potentialKnight = grid[position.getX() + 2][position.getY() - 1];
			if (potentialKnight.hasPiece() && potentialKnight.getPiece().getColour().equals(underAttackBy)
					&& potentialKnight.getPiece().getType().equals(PieceType.KNIGHT)) {
				return true;
			}
		}
		if (position.getX() < 6 && position.getY() < 7) {
			potentialKnight = grid[position.getX() + 2][position.getY() + 1];
			if (potentialKnight.hasPiece() && potentialKnight.getPiece().getColour().equals(underAttackBy)
					&& potentialKnight.getPiece().getType().equals(PieceType.KNIGHT)) {
				return true;
			}
		}
		return false;
	}

	private boolean isUnderAttackByBishopOrQueen(PieceColour underAttackBy, SquareCoordinate position) {
		Square potentialBishopOrQueen = null;
		for(int x = position.getX() + 1, y = position.getY() + 1; x <= 7 && y <= 7; x++, y++) { // Check NorthEast
			potentialBishopOrQueen = grid[x][y];
			if (potentialBishopOrQueen.hasPiece()) {
				if (potentialBishopOrQueen.getPiece().getColour().equals(underAttackBy)
						&& (potentialBishopOrQueen.getPiece().getType().equals(PieceType.BISHOP)
								|| potentialBishopOrQueen.getPiece().getType().equals(PieceType.QUEEN))) {
					return true;
				} else break;
			}
		}

		for(int x = position.getX() + 1, y = position.getY() - 1; x <= 7 && y >= 0; x++, y--) { // Check SouthEast
			potentialBishopOrQueen = grid[x][y];
			if (potentialBishopOrQueen.hasPiece()) {
				if (potentialBishopOrQueen.getPiece().getColour().equals(underAttackBy)
						&& (potentialBishopOrQueen.getPiece().getType().equals(PieceType.BISHOP)
								|| potentialBishopOrQueen.getPiece().getType().equals(PieceType.QUEEN))) {
					return true;
				} else break;
			}
		}

		for(int x = position.getX() - 1, y = position.getY() - 1; x >= 0 && y >= 0; x--, y--) { // Check SouthWest
			potentialBishopOrQueen = grid[x][y];
			if (potentialBishopOrQueen.hasPiece()) {
				if (potentialBishopOrQueen.getPiece().getColour().equals(underAttackBy)
						&& (potentialBishopOrQueen.getPiece().getType().equals(PieceType.BISHOP)
								|| potentialBishopOrQueen.getPiece().getType().equals(PieceType.QUEEN))) {
					return true;
				} else break;
			}
		}

		for(int x = position.getX() - 1, y = position.getY() + 1; x >= 0 && y <= 7; x--, y++) { // Check NorthWest
			potentialBishopOrQueen = grid[x][y];
			if (potentialBishopOrQueen.hasPiece()) {
				if (potentialBishopOrQueen.getPiece().getColour().equals(underAttackBy)
						&& (potentialBishopOrQueen.getPiece().getType().equals(PieceType.BISHOP)
								|| potentialBishopOrQueen.getPiece().getType().equals(PieceType.QUEEN))) {
					return true;
				} else break;
			}
		}
		return false;
	}

	private boolean isUnderAttackByKing(PieceColour underAttackBy, SquareCoordinate position) {
		Square potentialKing = null;
		if (position.getX() > 0) {
			potentialKing = grid[position.getX() - 1][position.getY()]; // West
			if (potentialKing.hasPiece() && potentialKing.getPiece().getColour().equals(underAttackBy)
					&& potentialKing.getPiece().getType().equals(PieceType.KING)) {
				return true;
			}
			if (position.getY() > 0) {
				potentialKing = grid[position.getX() - 1][position.getY() - 1]; // SouthWest
				if (potentialKing.hasPiece() && potentialKing.getPiece().getColour().equals(underAttackBy)
						&& potentialKing.getPiece().getType().equals(PieceType.KING)) {
					return true;
				}
			}
			if (position.getY() < 7) {
				potentialKing = grid[position.getX() - 1][position.getY() + 1]; // NorthWest
				if (potentialKing.hasPiece() && potentialKing.getPiece().getColour().equals(underAttackBy)
						&& potentialKing.getPiece().getType().equals(PieceType.KING)) {
					return true;
				}
			}
		}

		if (position.getX() < 7) {
			potentialKing = grid[position.getX() + 1][position.getY()]; // East
			if (potentialKing.hasPiece() && potentialKing.getPiece().getColour().equals(underAttackBy)
					&& potentialKing.getPiece().getType().equals(PieceType.KING)) {
				return true;
			}
			if (position.getY() > 0) {
				potentialKing = grid[position.getX() + 1][position.getY() - 1]; // SouthEast
				if (potentialKing.hasPiece() && potentialKing.getPiece().getColour().equals(underAttackBy)
						&& potentialKing.getPiece().getType().equals(PieceType.KING)) {
					return true;
				}
			}
			if (position.getY() < 7) {
				potentialKing = grid[position.getX() + 1][position.getY() + 1]; // NorthEast
				if (potentialKing.hasPiece() && potentialKing.getPiece().getColour().equals(underAttackBy)
						&& potentialKing.getPiece().getType().equals(PieceType.KING)) {
					return true;
				}
			}
		}

		if (position.getY() > 0) {
			potentialKing = grid[position.getX()][position.getY() - 1]; // South
			if (potentialKing.hasPiece() && potentialKing.getPiece().getColour().equals(underAttackBy)
					&& potentialKing.getPiece().getType().equals(PieceType.KING)) {
				return true;
			}
		}

		if (position.getY() < 7) {
			potentialKing = grid[position.getX()][position.getY() + 1]; // North
			if (potentialKing.hasPiece() && potentialKing.getPiece().getColour().equals(underAttackBy)
					&& potentialKing.getPiece().getType().equals(PieceType.KING)) {
				return true;
			}
		}
		return false;
	}
}
