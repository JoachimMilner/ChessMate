package model.boardmodel.pieces;

import java.util.ArrayList;
import java.util.List;

import model.boardmodel.Board;
import model.boardmodel.MoveValidity;
import model.boardmodel.Square;
import model.boardmodel.SquareCoordinate;

public class King extends AbstractPiece {

	private static final long serialVersionUID = -3300164183248804906L;
	private boolean hasMoved;

	public King(Board gameBoard, PieceColour colour, SquareCoordinate position) {
		this.gameBoard = gameBoard;
		this.colour = colour;
		this.position = position;
	}

	public PieceType getType() {
		return PieceType.KING;
	}

	@Override
	public void setPosition(SquareCoordinate position) {
		this.position = position;
		hasMoved = true;
	}

	public boolean hasMoved() {
		return hasMoved;
	}

	@Override
	public void evaluateMoves() {
		availableMoves.clear();

		List<SquareCoordinate> movesToValidate = new ArrayList<>();
		int x = position.getX();
		int y = position.getY();
		if (x > 0) {
			movesToValidate.add(new SquareCoordinate(x - 1, y)); // West
			if (y > 0) {
				movesToValidate.add(new SquareCoordinate(x - 1, y - 1)); // SouthWest
			}
			if (y < 7) {
				movesToValidate.add(new SquareCoordinate(x - 1, y + 1)); // NorthWest
			}
		}

		if (x < 7) {
			movesToValidate.add(new SquareCoordinate(x + 1, y)); // East
			if (y > 0) {
				movesToValidate.add(new SquareCoordinate(x + 1, y - 1)); // SouthEast
			}
			if (y < 7) {
				movesToValidate.add(new SquareCoordinate(x + 1, y + 1)); // NorthEast
			}
		}

		if (y > 0) {
			movesToValidate.add(new SquareCoordinate(x, y - 1)); // South
		}

		if (y < 7) {
			movesToValidate.add(new SquareCoordinate(x, y + 1)); // North
		}

		for (SquareCoordinate squareToValidate : movesToValidate) {
			MoveValidity moveValidity = getMoveValidity(squareToValidate);
			if (moveValidity.equals(MoveValidity.EMPTY_SQUARE) || moveValidity.equals(MoveValidity.ENEMY_PIECE)) {
				availableMoves.add(squareToValidate);
			}
		}

		// Check for castling
		if (!hasMoved && !gameBoard.isUnderAttack(colour.getEnemyColour(), position)) { // First check that king isn't in check and hasn't moved
			Square[][] grid = gameBoard.getGrid();

			// Check castling east
			if (grid[7][position.getY()].hasPiece()
					&& grid[7][position.getY()].getPiece().getType().equals(PieceType.ROOK)) { // Check rook is in correct position
				Rook eastRook = (Rook) grid[7][position.getY()].getPiece();
				if (!eastRook.hasMoved() // Check rook hasn't moved and that castling path is empty and not under attack
						&& getMoveValidity(new SquareCoordinate(5, position.getY())).equals(MoveValidity.EMPTY_SQUARE)
						&& getMoveValidity(new SquareCoordinate(6, position.getY())).equals(MoveValidity.EMPTY_SQUARE)) {
					availableMoves.add(new SquareCoordinate(6, position.getY()));
				}
			}

			// Check castling west
			if (grid[0][position.getY()].hasPiece()
					&& grid[0][position.getY()].getPiece().getType().equals(PieceType.ROOK)) { // Check rook is in correct position
				Rook westRook = (Rook) grid[0][position.getY()].getPiece();
				if (!westRook.hasMoved() // Check rook hasn't moved and that castling path is empty and not under attack
						&& getMoveValidity(new SquareCoordinate(3, position.getY())).equals(MoveValidity.EMPTY_SQUARE)
						&& getMoveValidity(new SquareCoordinate(2, position.getY())).equals(MoveValidity.EMPTY_SQUARE)) {
					availableMoves.add(new SquareCoordinate(2, position.getY()));
				}
			}
		}
	}
}
