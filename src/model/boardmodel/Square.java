package model.boardmodel;

import java.io.Serializable;

import model.boardmodel.pieces.IPiece;

public class Square implements Serializable {

	private static final long serialVersionUID = -6431194022715058717L;
	private IPiece piece;

	public IPiece getPiece() {
		return piece;
	}

	public void setPiece(IPiece piece) {
		this.piece = piece;
	}

	public boolean hasPiece() {
		return piece != null;
	}
}
