package ai;

import java.util.List;
import java.util.Random;

import model.GameModel;
import model.GameState;
import model.boardmodel.SquareCoordinate;
import model.boardmodel.pieces.IPiece;
import model.boardmodel.pieces.PieceColour;

public class ChessAI {

	public static void makeMove(GameModel gameModel) {
/*		if (gameModel.getPlayerColour().equals(PieceColour.WHITE)) {
			if (gameModel.getGameBoard().getGrid()[4][6].hasPiece()) {
				gameModel.movePiece(new SquareCoordinate(4, 6), new SquareCoordinate(4, 4));
			} else {
				gameModel.movePiece(new SquareCoordinate(3, 6), new SquareCoordinate(3, 5));
			}
		} else {
			if (gameModel.getGameBoard().getGrid()[4][1].hasPiece()) {
				gameModel.movePiece(new SquareCoordinate(4, 1), new SquareCoordinate(4, 3));
			} else {
				gameModel.movePiece(new SquareCoordinate(3, 1), new SquareCoordinate(3, 2));
			}
		}*/
		if (gameModel.getGameState().equals(GameState.CHECK) || gameModel.getGameState().equals(GameState.IN_PLAY)) {
			List<IPiece> piecesToEvaluate = gameModel.getPlayerColour().equals(PieceColour.WHITE)
					? gameModel.getGameBoard().getBlackPieces() : gameModel.getGameBoard().getWhitePieces();
			Random random = new Random();
			IPiece randomPiece = piecesToEvaluate.get(random.nextInt(piecesToEvaluate.size()));
			while (randomPiece.getMoves().isEmpty()) {
				randomPiece = piecesToEvaluate.get(random.nextInt(piecesToEvaluate.size()));
			}
			int randomIndex = random.nextInt(randomPiece.getMoves().size());
			int i = 0;
			SquareCoordinate randomMove = null;
			for (SquareCoordinate move : randomPiece.getMoves()) {
				if (i == randomIndex) {
					randomMove = move;
					break;
				}
				i++;
			}
			gameModel.movePiece(randomPiece.getPosition(), randomMove);
		}
	}

}
