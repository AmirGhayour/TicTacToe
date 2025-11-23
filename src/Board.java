import java.util.HashSet;

public class Board {

	public static final int BOARD_LENGTH = 3;

	public enum State {
		Blank, X, O
	}

	private State[][] board;
	private State pTurn, winner;
	private boolean gameOver;
	private HashSet<Integer> moves;
	private int moveCount, lastMove;

	// **************************************************** Constructor
	public Board() {
		board = new State[BOARD_LENGTH][BOARD_LENGTH];
		moves = new HashSet<>();
		reset();
	}

	// **************************************************** reset
	public void reset() {

		gameOver = false;
		pTurn = State.X;
		winner = State.Blank;
		moveCount = 0;

		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				board[i][j] = State.Blank;
			}
		}

		moves.clear();

		for (int i = 0; i < BOARD_LENGTH * BOARD_LENGTH; i++) {
			moves.add(i);
		}

	}

	// **************************************************** move
	public boolean move(int index) {
		lastMove = index;

		int x = index % BOARD_LENGTH;
		int y = index / BOARD_LENGTH;

		if (gameOver) {
			throw new IllegalStateException("Game is over");
		}

		if (board[y][x] == State.Blank) {
			board[y][x] = pTurn;
		} else {
			return false;
		}

		moveCount++;
		moves.remove(y * BOARD_LENGTH + x);

		// check game for draw.
		if (moveCount == BOARD_LENGTH * BOARD_LENGTH) {
			winner = State.Blank;
			gameOver = true;
		}

		checkWinner(x, y);

		pTurn = (pTurn == State.X) ? State.O : State.X;
		return true;
	}

	private void checkWinner(int x, int y) {

		for (int i = 1; i < BOARD_LENGTH; i++) {
			if (board[y][i] != board[y][i - 1]) {
				break;
			}
			if (i == BOARD_LENGTH - 1) {
				winner = pTurn;
				gameOver = true;
				return;
			}
		}

		for (int i = 1; i < BOARD_LENGTH; i++) {
			if (board[i][x] != board[i - 1][x]) {
				break;
			}
			if (i == BOARD_LENGTH - 1) {
				winner = pTurn;
				gameOver = true;
				return;
			}
		}

		if (x == y) {
			for (int i = 1; i < BOARD_LENGTH; i++) {
				if (board[i][i] != board[i - 1][i - 1]) {
					break;
				}
				if (i == BOARD_LENGTH - 1) {
					winner = pTurn;
					gameOver = true;
					return;
				}
			}
		}

		if (BOARD_LENGTH - 1 - x == y) {
			for (int i = 1; i < BOARD_LENGTH; i++) {
				if (board[BOARD_LENGTH - 1 - i][i] != board[BOARD_LENGTH - i][i - 1]) {
					break;
				}
				if (i == BOARD_LENGTH - 1) {
					winner = pTurn;
					gameOver = true;
					return;
				}
			}
		}
	}

	// **************************************************** Getter
	public State[][] toArray() {
		return board.clone();
	}

	public State getTurn() {
		return pTurn;
	}

	public State getWinner() {
		if (!gameOver) {
			throw new IllegalStateException("Game is not over yet");
		}
		return winner;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public HashSet<Integer> getMoves() {
		return moves;
	}

	public int getLastMove() {
		return lastMove;
	}

	public Board getCopy() {

		Board board = new Board();

		for (int i = 0; i < board.board.length; i++) {
			board.board[i] = this.board[i].clone();
		}

		board.pTurn = this.pTurn;
		board.winner = this.winner;
		board.gameOver = this.gameOver;
		board.moves = new HashSet<>();
		board.moves.addAll(this.moves);
		board.moveCount = this.moveCount;

		return board;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		for (int y = 0; y < BOARD_LENGTH; y++) {
			for (int x = 0; x < BOARD_LENGTH; x++) {

				if (board[y][x] == State.Blank) {
					sb.append("-");
				} else {
					sb.append(board[y][x].name());
				}
				sb.append(" ");

			}
			if (y != BOARD_LENGTH - 1) {
				sb.append("\n");
			}
		}

		return new String(sb);
	}

}