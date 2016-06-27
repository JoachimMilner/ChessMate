package model.boardmodel.pieces;

import java.util.ArrayList;
import java.util.List;

import model.boardmodel.Board;
import model.boardmodel.MoveValidity;
import model.boardmodel.SquareCoordinate;

public class Knight extends AbstractPiece {

	private static final long serialVersionUID = 3783867328431457795L;

	public Knight(Board gameBoard, PieceColour colour, SquareCoordinate position) {
		this.gameBoard = gameBoard;
		this.colour = colour;
		this.position = position;
	}

	public PieceType getType() {
		return PieceType.KNIGHT;
	}

	public void evaluateMoves() {
		availableMoves.clear();

		List<SquareCoordinate> movesToValidate = new ArrayList<>();
		int x = position.getX();
		int y = position.getY();
		if (x > 1 && y > 0) {
			movesToValidate.add(new SquareCoordinate(x - 2, y - 1));
		}

		if (x > 1 && y < 7) {
			movesToValidate.add(new SquareCoordinate(x - 2, y + 1));
		}

		if (x > 0 && y > 1) {
			movesToValidate.add(new SquareCoordinate(x - 1, y - 2));
		}

		if (x > 0 && y < 6) {
			movesToValidate.add(new SquareCoordinate(x - 1, y + 2));
		}

		if (x < 7 && y > 1) {
			movesToValidate.add(new SquareCoordinate(x + 1, y - 2));
		}

		if (x < 7 && y < 6) {
			movesToValidate.add(new SquareCoordinate(x + 1, y + 2));
		}

		if (x < 6 && y > 0) {
			movesToValidate.add(new SquareCoordinate(x + 2, y - 1));
		}

		if (x < 6 && y < 7) {
			movesToValidate.add(new SquareCoordinate(x + 2, y + 1));
		}

		for (SquareCoordinate squareToValidate : movesToValidate) {
			MoveValidity moveValidity = getMoveValidity(squareToValidate);
			if (moveValidity.equals(MoveValidity.EMPTY_SQUARE) || moveValidity.equals(MoveValidity.ENEMY_PIECE)) {
				availableMoves.add(squareToValidate);
			}
		}
	}
}
