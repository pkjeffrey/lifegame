package au.pkj.life;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GameUI extends JFrame implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_SPEED = 90;
	private static final int MIN_SPEED = 1;
	private static final int MAX_SPEED = 99;
	
	private int _speed = MAX_SPEED + 1 - DEFAULT_SPEED;
	private JButton _startStopButton;
	private JTextField _seedText;
	private GameBoard _gameBoard;
	private GameState _gameState;
	private GameThread _gameThread;
	
	public GameUI() {
		initFrame();
	}
	
	public void onSetup() {
		_gameState = new GameState(_gameBoard.getGameWidth(), _gameBoard.getGameHeight());
		_gameState.setup(_seedText.getText());
	}
	
	public void onStart() {
		if (_gameState != null) {
			_startStopButton.setText("Stop");
			_startStopButton.setActionCommand("stop");
			_gameThread = new GameThread(this);
			_gameThread.setFramePeriod(_speed * 10);
			_gameThread.setRunning(true);
			_gameThread.start();
		}
	}
	
	public void onStop() {
		_startStopButton.setText("Start");
		_startStopButton.setActionCommand("start");
		if (_gameThread != null) {
			_gameThread.setRunning(false);
			while (_gameThread != null) {
				try {
					_gameThread.join();
					_gameThread = null;
				} catch (InterruptedException ex) {}
			}
		}
	}
	
	public void render() {
		_gameBoard.repaint();
	}
	
	public void update() {
		_gameState.update();
	}
	
	private void initFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Conway's Game of Life");
		
		_gameBoard = new GameBoard();
		getContentPane().add(_gameBoard, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		ButtonGroup radioGroup = new ButtonGroup();
		JRadioButton radio = new JRadioButton("Small");
		radio.setActionCommand("gridSmall");
		radio.setSelected(true);
		radio.addActionListener(this);
		bottomPanel.add(radio);
		radioGroup.add(radio);
		radio = new JRadioButton("Medium");
		radio.setActionCommand("gridMedium");
		radio.addActionListener(this);
		bottomPanel.add(radio);
		radioGroup.add(radio);
		radio = new JRadioButton("Large");
		radio.setActionCommand("gridLarge");
		radio.addActionListener(this);
		bottomPanel.add(radio);
		radioGroup.add(radio);
		bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		
		JLabel label = new JLabel("Speed:");
		bottomPanel.add(label);
		JSpinner speed = new JSpinner(new SpinnerNumberModel(DEFAULT_SPEED, MIN_SPEED, MAX_SPEED, 1));
		speed.addChangeListener(this);
		bottomPanel.add(speed);
		bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		
		JLabel seedLabel = new JLabel("Seed:");
		bottomPanel.add(seedLabel);
		_seedText = new JTextField(5);
		bottomPanel.add(_seedText);
		bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		
		JButton button = new JButton("Setup");
		button.setActionCommand("setup");
		button.addActionListener(this);
		bottomPanel.add(button);
		bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		
		_startStopButton = new JButton("Start");
		_startStopButton.setActionCommand("start");
		_startStopButton.addActionListener(this);
		bottomPanel.add(_startStopButton);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("setup".equals(e.getActionCommand())) {
			onSetup();
			_gameBoard.repaint();
		} else if ("start".equals(e.getActionCommand())) {
			onStart();
		} else if ("stop".equals(e.getActionCommand())) {
			onStop();
		} else if ("gridSmall".equals(e.getActionCommand())) {
			_gameBoard._gridSize = GameBoard.GRID_SMALL;
			_gameBoard.repaint();
		} else if ("gridMedium".equals(e.getActionCommand())) {
			_gameBoard._gridSize = GameBoard.GRID_MEDIUM;
			_gameBoard.repaint();
		} else if ("gridLarge".equals(e.getActionCommand())) {
			_gameBoard._gridSize = GameBoard.GRID_LARGE;
			_gameBoard.repaint();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSpinner spinner = (JSpinner)(e.getSource());
		_speed = MAX_SPEED + 1 - (int) spinner.getValue();
		if (_gameThread != null)
			_gameThread.setFramePeriod(_speed * 10);
	}
	
	class GameBoard extends JPanel {
		private static final long serialVersionUID = 1L;	
		
		private static final int GRID_SMALL = 10;
		private static final int GRID_MEDIUM = 25;
		private static final int GRID_LARGE = 50;
		
		private int _gridSize = GRID_SMALL;
		
		public GameBoard() {
			setBackground(new Color(33, 33, 33));
		}
		
		public int getGameWidth() {
			return getWidth() / _gridSize;
		}
		
		public int getGameHeight() {
			return getHeight() / _gridSize;
		}
		
		public void setGridSize(int gridSize) {
			_gridSize = gridSize;
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(800, 600);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int gridWidth = getWidth() / _gridSize;
			int gridHeight = getHeight() / _gridSize;
			int xOffset = (getWidth() - gridWidth * _gridSize) / 2;
			int yOffset = (getHeight() - gridHeight * _gridSize) / 2;
			g.setColor(new Color(128, 128, 128));
			for (int x = 0; x <= gridWidth; x++) {
				g.drawLine(xOffset + x * _gridSize, yOffset, xOffset + x * _gridSize, yOffset + gridHeight * _gridSize);
			}
			for (int y = 0; y <= gridHeight; y++) {
				g.drawLine(xOffset, yOffset + y * _gridSize, xOffset + gridWidth * _gridSize, yOffset + y * _gridSize);
			}
			g.setColor(new Color(255, 153, 0));
			if (_gameState != null) {
				for (int x = 0; x < _gameState.getWidth(); x++) {
					for (int y = 0; y < _gameState.getHeight(); y++) {
						if (_gameState.isAlive(y, x))
						g.fillRect(xOffset + x * _gridSize + 1, yOffset + y * _gridSize + 1, _gridSize - 1, _gridSize - 1);
					}
				}
			}
		}
	}
}
