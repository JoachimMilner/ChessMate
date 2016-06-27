package model.boardmodel.pieces;

import java.io.Serializable;

public enum PieceType implements Serializable {
	PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING;

	private String notationForm;

	static {
		PAWN.notationForm = "";
		ROOK.notationForm = "R";
		KNIGHT.notationForm = "N";
		BISHOP.notationForm = "B";
		QUEEN.notationForm = "Q";
		KING.notationForm = "K";
	}

	public String toNotationForm() {
		return notationForm;
	}
}
