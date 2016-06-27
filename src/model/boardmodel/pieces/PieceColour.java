package model.boardmodel.pieces;

import java.io.Serializable;

public enum PieceColour implements Serializable {
	WHITE, BLACK;

	private PieceColour enemyColour;
	private String lowerCase;

	static {
		WHITE.enemyColour = BLACK;
		WHITE.lowerCase = "White";
		BLACK.enemyColour = WHITE;
		BLACK.lowerCase = "Black";
	}

	public PieceColour getEnemyColour() {
		return enemyColour;
	}

	public String toLowerCase() {
		return lowerCase;
	}
}
