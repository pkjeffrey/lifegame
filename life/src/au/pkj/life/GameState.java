package au.pkj.life;

import java.awt.Dimension;
import java.util.Random;

public class GameState {
	private boolean[][] _gameBoard;
	private Random _random;
	
	public GameState(int width, int height) {
		if (width < 3 || height < 3)
			throw new IllegalArgumentException("Width and Height arguments must be positive integers greater than 2.");
		
		_gameBoard = createGameBoard(width, height);
		_random = new Random(System.currentTimeMillis());
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
		if ("random".equals(seed.substring(0, 6))) {
			int number = Integer.parseInt(seed.substring(6).trim());
			for (int i = 0; i < number; i++) {
				int row = weightedRandom(getHeight());
				int col = weightedRandom(getWidth());
				_gameBoard[row][col] = true;
			}
		} else {
			Dimension seedSize = calcSeedSize(seed);
			if (seedSize.width <= getWidth() && seedSize.height <= getHeight()) {
				int colOffset = (getWidth() - seedSize.width) / 2;
				int rowOffset = (getHeight() - seedSize.height) / 2;
				int col = colOffset;
				int row = rowOffset;
				int count = 0;
				for (int i = 0; i < seed.length(); i++) {
					char c = seed.charAt(i);
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
	
	private Dimension calcSeedSize(String seed) {
		Dimension size = new Dimension(0, 0);
		
		int col = 0;
		int row = 0;
		int count = 0;
		for (int i = 0; i < seed.length(); i++) {
			char c = seed.charAt(i);
			if (c == 'b' || c == 'o') {
				col += count == 0 ? 1 : count;
				count = 0;
			} else if (c == '$' || c == '!') {
				if (col > size.width)
					size.width = col;
				col = 0;
				row += count == 0 ? 1 : count;
				count = 0;
			} else {
				int digit = c - '0';
				count = count * 10 + digit;
			}
		}
		size.height = row;
		
		return size;
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
	
	private int weightedRandom(int max) {
	    int num = 0;
	    for (int i = 0; i < 5; i++) {
	        num += _random.nextInt(max) / 5;
	    }    
	    return num;
	}
}
