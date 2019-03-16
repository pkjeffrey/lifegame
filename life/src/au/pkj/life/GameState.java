package au.pkj.life;

public class GameState {
	private boolean[][] _gameBoard;
	
	public GameState(int width, int height) {
		if (width < 3 || height < 3)
			throw new IllegalArgumentException("Width and Height arguments must be positive integers greater than 2.");
		
		_gameBoard = createGameBoard(width, height);
	}
	
	public int getWidth() {
		return _gameBoard[0].length;
	}
	
	public int getHeight() {
		return _gameBoard.length;
	}
	
	public boolean isAlive(int row, int col) {
		return _gameBoard[row][col];
	}
	
	public void setup(String seed) {
		// "3 3 bo$2bo$3o!"
		String[] parts = seed.split(" ");
		if (parts.length == 3) {
			int width = Integer.parseInt(parts[0]);
			int height = Integer.parseInt(parts[1]);
			int colOffset = (getWidth() - width) / 2;
			int rowOffset = (getHeight() - height) / 2;
			int col = colOffset;
			int row = rowOffset;
			int count = 0;
			for (int i = 0; i < parts[2].length(); i++) {
				char c = parts[2].charAt(i);
				if (c == 'b') {
					col += count == 0 ? 1 : count;
					count = 0;
				} else if (c == 'o') {
					int inc = count == 0 ? 1 : count;
					while (inc-- > 0)
						_gameBoard[row][col++] = true;
					count = 0;
				} else if (c == '$') {
					col = colOffset;
					row += count == 0 ? 1 : count;
					count = 0;
				} else if (c == '!') {
					// nothing, reached end
				} else {
					int digit = c - '0';
					count = count * 10 + digit;
				}
			}
		}
	}
	
	public void update() {
		boolean[][] newGameBoard = createGameBoard(getWidth(), getHeight());
		
		for (int row = 0; row < getHeight(); row++) {
			for (int col = 0; col < getWidth(); col++) {
				int liveNeighbours = countLiveNeighbours(row, col);
				
				if (
						(_gameBoard[row][col] && liveNeighbours >= 2 && liveNeighbours <= 3) ||
						(!_gameBoard[row][col] && liveNeighbours == 3))
					newGameBoard[row][col] = true;
			}
		}
		
		_gameBoard = newGameBoard;
	}
	
	private int countLiveNeighbours(int row, int col) {
		int prevRow = (row == 0 ? getHeight() - 1 : row - 1);
		int nextRow = (row == getHeight() -1 ? 0 : row + 1);
		int prevCol = (col == 0 ? getWidth() - 1 : col - 1);
		int nextCol = (col == getWidth() - 1 ? 0 : col + 1);
		
		return (_gameBoard[prevRow][prevCol] ? 1 : 0) +
				(_gameBoard[prevRow][col] ? 1 : 0) +
				(_gameBoard[prevRow][nextCol] ? 1 : 0) +
				(_gameBoard[row][prevCol] ? 1 : 0) +
				(_gameBoard[row][nextCol] ? 1 : 0) +
				(_gameBoard[nextRow][prevCol] ? 1 : 0) +
				(_gameBoard[nextRow][col] ? 1 : 0) +
				(_gameBoard[nextRow][nextCol] ? 1 : 0);
	}
	
	private boolean[][] createGameBoard(int width, int height) {
		return new boolean[height][width];
	}
}
