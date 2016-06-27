package model.boardmodel.pieces;

import model.boardmodel.Board;
import model.boardmodel.MoveValidity;
import model.boardmodel.SquareCoordinate;

public class Rook extends AbstractPiece {

	private static final long serialVersionUID = 4474936622789884259L;
	private boolean hasMoved;

	public Rook(Board gameBoard, PieceColour colour, SquareCoordinate position) {
		this.gameBoard = gameBoard;
		this.colour = colour;
		this.position = position;
	}

	public PieceType getType() {
		return PieceType.ROOK;
	}

	@Override
	public void setPosition(SquareCoordinate position) {
		this.position = position;
		hasMoved = true;
	}

	public boolean hasMoved() {
		return hasMoved;
	}

	public void evaluateMoves() {
		availableMoves.clear();

		checkEast:
		for (int x = position.getX() + 1; x <= 7; x++) {
			SquareCoordinate squareToValidate = new SquareCoordinate(x, position.getY());
			MoveValidity moveValidity = getMoveValidity(squareToValidate);
			switch(moveValidity) {
			case CHECK_OR_FRIENDLY_PIECE:
				break checkEast;
			case EMPTY_SQUARE:
				availableMoves.add(squareToValidate);
				break;
			case ENEMY_PIECE:
				availableMoves.add(squareToValidate);
				break checkEast;
			}
		}

		checkWest:
		for (int x = position.getX() - 1; x >= 0; x--) {
			SquareCoordinate squareToValidate = new SquareCoordinate(x, position.getY());
			MoveValidity moveValidity = getMoveValidity(squareToValidate);
			switch(moveValidity) {
			case CHECK_OR_FRIENDLY_PIECE:
				break checkWest;
			case EMPTY_SQUARE:
				availableMoves.add(squareToValidate);
				break;
			case ENEMY_PIECE:
				availableMoves.add(squareToValidate);
				break checkWest;
			}
		}

		checkNorth:
		for (int y = position.getY() + 1; y <= 7; y++) {
			SquareCoordinate squareToValidate = new SquareCoordinate(position.getX(), y);
			MoveValidity moveValidity = getMoveValidity(squareToValidate);
			switch(moveValidity) {
			case CHECK_OR_FRIENDLY_PIECE:
				break checkNorth;
			case EMPTY_SQUARE:
				availableMoves.add(squareToValidate);
				break;
			case ENEMY_PIECE:
				availableMoves.add(squareToValidate);
				break checkNorth;
			}
		}

		checkSouth:
		for (int y = position.getY() - 1; y >= 0; y--) {
			SquareCoordinate squareToValidate = new SquareCoordinate(position.getX(), y);
			MoveValidity moveValidity = getMoveValidity(squareToValidate);
			switch(moveValidity) {
			case CHECK_OR_FRIENDLY_PIECE:
				break checkSouth;
			case EMPTY_SQUARE:
				availableMoves.add(squareToValidate);
				break;
			case ENEMY_PIECE:
				availableMoves.add(squareToValidate);
				break checkSouth;
			}
		}
	}
}
