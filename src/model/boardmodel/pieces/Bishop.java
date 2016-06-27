package model.boardmodel.pieces;

import java.io.Serializable;

import model.boardmodel.Board;
import model.boardmodel.MoveValidity;
import model.boardmodel.SquareCoordinate;

public class Bishop extends AbstractPiece implements Serializable {

	private static final long serialVersionUID = 4966043068434876069L;

	public Bishop(Board gameBoard, PieceColour colour, SquareCoordinate position) {
		this.gameBoard = gameBoard;
		this.colour = colour;
		this.position = position;
	}

	public PieceType getType() {
		return PieceType.BISHOP;
	}

	public void evaluateMoves() {
		availableMoves.clear();

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
