package model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

import model.boardmodel.Board;

public class BoardStateManager {

	private static BoardStateManager instance = null;
	private Deque<Board> boardStates = new ArrayDeque<>();

	private BoardStateManager() {
	}

	public static BoardStateManager getInstance() {
		if (instance == null) {
			instance = new BoardStateManager();
		}
		return instance;
	}

	public void pushState(Board state) {
		boardStates.push(state);
	}

	public Board popState() {
		try {
			return boardStates.pop();
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public void clearStates() {
		boardStates.clear();
	}

	public boolean positionHasOccurredFivefold(String currentBoard) {
		int positionCount = 1;
		for (Board board : boardStates) {
			if (board.getBoardStringRepresentation().equals(currentBoard)) {
				positionCount++;
			}
		}
		return positionCount == 5 ? true : false;
	}
}
