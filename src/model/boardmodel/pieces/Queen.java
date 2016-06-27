package model.boardmodel.pieces;

import model.boardmodel.Board;
import model.boardmodel.MoveValidity;
import model.boardmodel.SquareCoordinate;

public class Queen extends AbstractPiece {

	private static final long serialVersionUID = -8657654500194186225L;

	public Queen(Board gameBoard, PieceColour colour, SquareCoordinate position) {
		this.gameBoard = gameBoard;
		this.colour = colour;
		this.position = position;
	}

	public PieceType getType() {
		return PieceType.QUEEN;
	}

	public void evaluateMoves() {
		availableMoves.clear();

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

		checkNorthEast:
		for(int x = position.getX() + 1, y = position.getY() + 1; x <= 7 && y <= 7; x++, y++) {
			SquareCoordinate squareToValidate = new SquareCoordinate(x, y);
			MoveValidity moveValidity = getMoveValidity(squareToValidate);
			switch(moveValidity) {
			case CHECK_OR_FRIENDLY_PIECE:
				break checkNorthEast;
			case EMPTY_SQUARE:
				availableMoves.add(squareToValidate);
				break;
			case ENEMY_PIECE:
				availableMoves.add(squareToValidate);
				break checkNorthEast;
			}
		}

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

		checkSouthEast:
		for(int x = position.getX() + 1, y = position.getY() - 1; x <= 7 && y >= 0; x++, y--) {
			SquareCoordinate squareToValidate = new SquareCoordinate(x, y);
			MoveValidity moveValidity = getMoveValidity(squareToValidate);
			switch(moveValidity) {
			case CHECK_OR_FRIENDLY_PIECE:
				break checkSouthEast;
			case EMPTY_SQUARE:
				availableMoves.add(squareToValidate);
				break;
			case ENEMY_PIECE:
				availableMoves.add(squareToValidate);
				break checkSouthEast;
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

		checkSouthWest:
		for(int x = position.getX() - 1, y = position.getY() - 1; x >= 0 && y >= 0; x--, y--) {
			SquareCoordinate squareToValidate = new SquareCoordinate(x, y);
			MoveValidity moveValidity = getMoveValidity(squareToValidate);
			switch(moveValidity) {
			case CHECK_OR_FRIENDLY_PIECE:
				break checkSouthWest;
			case EMPTY_SQUARE:
				availableMoves.add(squareToValidate);
				break;
			case ENEMY_PIECE:
				availableMoves.add(squareToValidate);
				break checkSouthWest;
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

		checkNorthWest:
		for(int x = position.getX() - 1, y = position.getY() + 1; x >= 0 && y <= 7; x--, y++) {
			SquareCoordinate squareToValidate = new SquareCoordinate(x, y);
			MoveValidity moveValidity = getMoveValidity(squareToValidate);
			switch(moveValidity) {
			case CHECK_OR_FRIENDLY_PIECE:
				break checkNorthWest;
			case EMPTY_SQUARE:
				availableMoves.add(squareToValidate);
				break;
			case ENEMY_PIECE:
				availableMoves.add(squareToValidate);
				break checkNorthWest;
			}
		}
	}
}
