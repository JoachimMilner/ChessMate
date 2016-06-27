package model;

import java.util.List;

import model.boardmodel.Board;
import model.boardmodel.SquareCoordinate;
import model.boardmodel.pieces.IPiece;
import model.boardmodel.pieces.PieceColour;
import model.boardmodel.pieces.PieceType;

public class Utils {

	// Method to generate standard notation for a move
	public static String getMoveNotation(Board gameBoard, IPiece movingPiece, SquareCoordinate from, SquareCoordinate to, boolean isDirectCapture) {
		String moveString = "";

		// First check for castling
		if (movingPiece.getType().equals(PieceType.KING) && Math.abs(from.getX() - to.getX()) == 2) {
			moveString += "0-0";
			if (from.getX() > to.getX()) {
				moveString += "-0";
			}
		} else { // Non-castle move
			// Insert shorthand for piece type
			moveString += movingPiece.getType().toNotationForm();

			// Insert file or rank if 2 pieces of the same type could have made the move
			boolean fromPositionNoted = false;
			// Get opposite colour as the move has been made on the board at this point
			List<IPiece> piecesToCheck = gameBoard.getWhoseTurn().equals(PieceColour.BLACK) ? gameBoard.getWhitePieces()
					: gameBoard.getBlackPieces();
			for (IPiece piece : piecesToCheck) {
				if (piece.getType().equals(movingPiece.getType()) && !piece.equals(movingPiece)
						&& piece.getMoves().contains(to)) {
					fromPositionNoted = true;
					if (piece.getPosition().getX() != from.getX()) {
						moveString += "abcdefgh".charAt(from.getX());
					} else {
						moveString += (from.getY() + 1);
					}
					break;
				}
			}

			// Insert x if a capture was made
			if (isDirectCapture || (movingPiece.getType().equals(PieceType.PAWN) && from.getX() != to.getX())) {
				if (movingPiece.getType().equals(PieceType.PAWN) && !fromPositionNoted) {
					moveString += "abcdefgh".charAt(from.getX());
				}
				moveString += "x";
			}

			// Insert file and rank of square moved to
			moveString += "abcdefgh".charAt(to.getX());
			moveString += (to.getY() + 1);

			// Insert promotion piece type if move is pawn promotion
			if (movingPiece.getType().equals(PieceType.PAWN)
					&& (movingPiece.getColour().equals(PieceColour.WHITE) && to.getY() == 7
							|| movingPiece.getColour().equals(PieceColour.BLACK) && to.getY() == 0)) {
				moveString += "=" + gameBoard.getGrid()[to.getX()][to.getY()].getPiece().getType().toNotationForm();
			}
		}

		// Append final notation for game state (check etc.)
		switch (gameBoard.getGameState()) {
		case CHECK:
			moveString += "+";
			break;
		case CHECK_MATE:
			moveString += "# ";
			moveString += gameBoard.getWhoseTurn().equals(PieceColour.WHITE) ? "0–1" : "1-0";
			break;
		case FIVEFOLD_REPETITION:
		case INSUFFICIENT_MATERIAL:
		case STALE_MATE:
			moveString += Character.toString((char)171) + "-" + Character.toString((char)171);
			break;
		case IN_PLAY:
			break;
		}

		return moveString;
	}
}
