package model.boardmodel.pieces;

import java.io.Serializable;
import java.util.Set;

import model.boardmodel.SquareCoordinate;

public interface IPiece extends Serializable{
	PieceColour getColour();
	SquareCoordinate getPosition();
	void setPosition(SquareCoordinate currentPosition);
	PieceType getType();
	void evaluateMoves();
	Set<SquareCoordinate> getMoves();
}
