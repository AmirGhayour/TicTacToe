
public class MiniMax {

	// ******************************************************************* MiniMax
	public static int miniMax(Board board, Board.State player) {

		if (board.isGameOver()) {
			return score(board, player);
		}

		if (board.getTurn() == player) {
			return getMax(board, player);
		} else {
			return getMin(board, player);
		}

	}

	// ******************************************************************* getMax
	private static int getMax(Board board, Board.State player) {

		int bestScore = Integer.MIN_VALUE;
		int bestMove = -1;

		for (Integer move : board.getMoves()) {

			Board eBoard = board.getCopy();
			eBoard.move(move);

			int score = miniMax(eBoard, player);

			if (score >= bestScore) {
				bestScore = score;
				bestMove = move;
			}

		}

		board.move(bestMove);
		return bestScore;

	}

	// ******************************************************************* getMin
	private static int getMin(Board board, Board.State player) {

		int bestScore = Integer.MAX_VALUE;
		int bestMove = -1;

		for (Integer move : board.getMoves()) {

			Board eBoard = board.getCopy();
			eBoard.move(move);

			int score = miniMax(eBoard, player);

			if (score <= bestScore) {
				bestScore = score;
				bestMove = move;
			}

		}

		board.move(bestMove);
		return bestScore;

	}

	// ******************************************************************* Score
	private static int score(Board board, Board.State player) {

		if (player == Board.State.Blank) {
			throw new IllegalArgumentException("Player must be X or O");
		}

		Board.State opponent = (player == Board.State.X) ? Board.State.O : Board.State.X;

		if (board.isGameOver() && board.getWinner() == player) {
			return 10;
		} else if (board.isGameOver() && board.getWinner() == opponent) {
			return -10;
		} else {
			return 0;
		}

	}

}
