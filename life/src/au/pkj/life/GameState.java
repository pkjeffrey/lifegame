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
		// initialise gameboard
	}
	
	public void update() {
		boolean[][] newGameBoard = createGameBoard(getWidth(), getHeight());
		
		for (int row = 1; row < getHeight(); row++) {
			for (int col = 1; col < getWidth(); col++) {
				int liveNeighbours = countLiveNeighbours(row, col);
				
				if (_gameBoard[row][col] && (liveNeighbours < 2 || liveNeighbours > 3))
					_gameBoard[row][col] = false;
				else if (!_gameBoard[row][col] && liveNeighbours == 3)
					_gameBoard[row][col] = true;
			}
		}
		
		_gameBoard = newGameBoard;
	}
	
	private int countLiveNeighbours(int row, int col) {
		int prevRow = (row == 1 ? getHeight() - 1 : row - 1);
		int nextRow = (row == getHeight() -1 ? 0 : row + 1);
		int prevCol = (col == 1 ? getWidth() - 1 : col - 1);
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
