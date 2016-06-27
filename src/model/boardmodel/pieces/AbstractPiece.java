package model.boardmodel.pieces;

import java.util.HashSet;
import java.util.Set;

import model.boardmodel.Board;
import model.boardmodel.MoveValidity;
import model.boardmodel.Square;
import model.boardmodel.SquareCoordinate;

public abstract class AbstractPiece implements IPiece {

	private static final long serialVersionUID = 2347698662638655702L;
	protected Board gameBoard;
	protected PieceColour colour;
	protected SquareCoordinate position;
	protected Set<SquareCoordinate> availableMoves = new HashSet<>();

	public PieceColour getColour() {
		return colour;
	}

	public SquareCoordinate getPosition() {
		return position;
	}

	public void setPosition(SquareCoordinate position) {
		this.position = position;
	}

	public Set<SquareCoordinate> getMoves() {
		return availableMoves;
	}

	protected MoveValidity getMoveValidity(SquareCoordinate moveToEvaluate) {
		Square[][] grid = gameBoard.getGrid();
		Square validationSquare = grid[moveToEvaluate.getX()][moveToEvaluate.getY()];
		if (validationSquare.hasPiece() && validationSquare.getPiece().getColour().equals(colour) // Friendly piece
				|| moveResultsInCheck(moveToEvaluate)) { // Moving will result in check
			return MoveValidity.CHECK_OR_FRIENDLY_PIECE;
		}
		if (!validationSquare.hasPiece()) { // Empty square
			return MoveValidity.EMPTY_SQUARE;
		} else { // Enemy piece - can capture
			return MoveValidity.ENEMY_PIECE;
		}
	}

	// Determine if a move exposes friendly check and is therefore invalid
	// To save deep copying the entire board object, move the piece to test for check,
	// then move back before returning.
	// MAYBE RE-WRITE THIS AND JUST DEEP COPY BOARD?
	private boolean moveResultsInCheck(SquareCoordinate moveToEvaluate) {
		Square[][] grid = gameBoard.getGrid();
		Square validationSquare = grid[moveToEvaluate.getX()][moveToEvaluate.getY()];
		// If the target square has an enemy piece, record so we can revert
		IPiece enemyPiece = validationSquare.hasPiece() ? validationSquare.getPiece() : null;
		// Record this piece's original position
		SquareCoordinate originalPosition = position;
		boolean isCheck = false;
		try {
			// Move piece on the board but don't update position member
			validationSquare.setPiece(this);
			grid[position.getX()][position.getY()].setPiece(null);
			position = moveToEvaluate;
			isCheck = gameBoard.isUnderAttack(colour.getEnemyColour(), gameBoard.getKingsPosition(colour));
		} finally { // Make sure board is reverted
			grid[originalPosition.getX()][originalPosition.getY()].setPiece(this);
			position = originalPosition;
			validationSquare.setPiece(enemyPiece);
		}
		return isCheck;
	}
}
