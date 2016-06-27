package model;

import java.io.Serializable;

public enum GameState implements Serializable {
	IN_PLAY, CHECK, CHECK_MATE, STALE_MATE, INSUFFICIENT_MATERIAL, FIVEFOLD_REPETITION

/*	private String lowerCase;

	static {
		IN_PLAY.lowerCase = "In Play";
		CHECK.lowerCase = "Check";
		CHECK_MATE.lowerCase = "Check Mate";
		STALE_MATE.lowerCase = "Stale Mate";

	}*/
}
