package model.boardmodel.pieces;

import model.boardmodel.Board;
import model.boardmodel.MoveValidity;
import model.boardmodel.Square;
import model.boardmodel.SquareCoordinate;

public class Pawn extends AbstractPiece {

	private static final long serialVersionUID = 5777161205668163295L;
	private int moveCount = 0;
	private int movementDirection;
	private boolean canEnPassantWest = true;
	private boolean canEnPassantEast = true;

	public Pawn(Board gameBoard, PieceColour colour, SquareCoordinate position) {
		this.gameBoard = gameBoard;
		this.colour = colour;
		this.position = position;
		movementDirection = colour.equals(PieceColour.WHITE) ? 1 : -1;
	}

	public PieceType getType() {
		return PieceType.PAWN;
	}

	@Override
	public void setPosition(SquareCoordinate position) {
		this.position = position;
		moveCount++;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void evaluateMoves() {
		availableMoves.clear();

		// Check one square ahead of pawn
		SquareCoordinate oneSquareAhead = new SquareCoordinate(position.getX(), position.getY() + 1 * movementDirection);
		if (getMoveValidity(oneSquareAhead).equals(MoveValidity.EMPTY_SQUARE)) {
			availableMoves.add(oneSquareAhead);

			// Check if pawn can advance 2 squares (on first move)
			if (moveCount == 0) {
				SquareCoordinate twoSquaresAhead = new SquareCoordinate(position.getX(), position.getY() + 2 * movementDirection);
				if (getMoveValidity(twoSquaresAhead).equals(MoveValidity.EMPTY_SQUARE)) {
					availableMoves.add(twoSquaresAhead);
				}
			}
		}

		if (position.getX() < 7) {
			SquareCoordinate eastDiagonal = new SquareCoordinate(position.getX() + 1, position.getY() + 1 * movementDirection);
			if (getMoveValidity(eastDiagonal).equals(MoveValidity.ENEMY_PIECE)) {
				availableMoves.add(eastDiagonal);
			}

			// Check en passant east
			Square enPassantCheckEastSquare = gameBoard.getGrid()[position.getX() + 1][position.getY()];
			if (canEnPassantEast && enPassantCheckEastSquare.hasPiece() && enPassantCheckEastSquare.getPiece().getType().equals(PieceType.PAWN)) {
				Pawn pawn = (Pawn) enPassantCheckEastSquare.getPiece();
				if (pawn.getColour().equals(colour.getEnemyColour()) && pawn.getMoveCount() == 1
						&& (pawn.getColour().equals(PieceColour.WHITE) && pawn.getPosition().getY() == 3
								|| pawn.getColour().equals(PieceColour.BLACK) && pawn.getPosition().getY() == 4)) {
					availableMoves.add(new SquareCoordinate(position.getX() + 1, position.getY() + 1 * movementDirection));
					canEnPassantEast = false;
				}
			}
		}

		if (position.getX() > 0) {
			SquareCoordinate westDiagonal = new SquareCoordinate(position.getX() - 1, position.getY() + 1 * movementDirection);
			if (getMoveValidity(westDiagonal).equals(MoveValidity.ENEMY_PIECE)) {
				availableMoves.add(westDiagonal);
			}

			// Check en passant west
			Square enPassantCheckWestSquare = gameBoard.getGrid()[position.getX() - 1][position.getY()];
			if (canEnPassantWest && enPassantCheckWestSquare.hasPiece() && enPassantCheckWestSquare.getPiece().getType().equals(PieceType.PAWN)) {
				Pawn pawn = (Pawn) enPassantCheckWestSquare.getPiece();
				if (pawn.getColour().equals(colour.getEnemyColour()) && pawn.getMoveCount() == 1
						&& (pawn.getColour().equals(PieceColour.WHITE) && pawn.getPosition().getY() == 3
								|| pawn.getColour().equals(PieceColour.BLACK) && pawn.getPosition().getY() == 4)) {
					availableMoves.add(new SquareCoordinate(position.getX() - 1, position.getY() + 1 * movementDirection));
					canEnPassantWest = false;
				}
			}
		}
	}
}
