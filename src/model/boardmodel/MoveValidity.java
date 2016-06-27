package model.boardmodel;

import java.io.Serializable;

public enum MoveValidity implements Serializable {
	CHECK_OR_FRIENDLY_PIECE, EMPTY_SQUARE, ENEMY_PIECE
}
