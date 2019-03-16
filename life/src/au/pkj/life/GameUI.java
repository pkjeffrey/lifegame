package au.pkj.life;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameUI extends JFrame implements ActionListener, ComponentListener {
	private static final long serialVersionUID = 1L;
	
	private GameBoard _gameBoard;
	private GameState _gameState;
	private GameThread _gameThread;
	
	public GameUI() {
		initFrame();
	}
	
	public void onSetup() {
		_gameState = new GameState(_gameBoard.getGridWidth(), _gameBoard.getGridHeight());
		_gameState.setup("47 5 2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o$2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2obo2bo$44b2o$2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o$2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o!");
	}
	
	public void onStart() {
		if (_gameState != null) {
			_gameThread = new GameThread(this);
			_gameThread.setRunning(true);
			_gameThread.start();
		}
	}
	
	public void onStop() {
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
		
		JPanel buttonsPanel = new JPanel();
		JButton button;
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		buttonsPanel.add(Box.createHorizontalGlue());
		button = new JButton("Setup");
		button.setActionCommand("setup");
		button.addActionListener(this);
		buttonsPanel.add(button);
		buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		button = new JButton("Start");
		button.setActionCommand("start");
		button.addActionListener(this);
		buttonsPanel.add(button);
		buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		button = new JButton("Stop");
		button.setActionCommand("stop");
		button.addActionListener(this);
		buttonsPanel.add(button);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		
		pack();
		addComponentListener(this);
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
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// nothing
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// nothing
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		System.out.println(String.format("%1$d, %2$d", _gameBoard.getWidth(), _gameBoard.getHeight()));
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// nothing
	}
	
	class GameBoard extends JPanel {
		private static final long serialVersionUID = 1L;
		
		public static final int GRID_SIZE = 10;
		
		public GameBoard() {
			setBackground(new Color(33, 33, 33));
		}
		
		public int getGridWidth() {
			return getWidth() / GRID_SIZE;
		}
		
		public int getGridHeight() {
			return getHeight() / GRID_SIZE;
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(800, 600);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int gridWidth = getWidth() / GRID_SIZE;
			int gridHeight = getHeight() / GRID_SIZE;
			int xOffset = (getWidth() - gridWidth * GRID_SIZE) / 2;
			int yOffset = (getHeight() - gridHeight * GRID_SIZE) / 2;
			g.setColor(new Color(128, 128, 128));
			for (int x = 0; x <= gridWidth; x++) {
				g.drawLine(xOffset + x * GRID_SIZE, yOffset, xOffset + x * GRID_SIZE, yOffset + gridHeight * GRID_SIZE);
			}
			for (int y = 0; y <= gridHeight; y++) {
				g.drawLine(xOffset, yOffset + y * GRID_SIZE, xOffset + gridWidth * GRID_SIZE, yOffset + y * GRID_SIZE);
			}
			g.setColor(new Color(255, 153, 0));
			if (_gameState != null) {
				for (int x = 0; x < _gameState.getWidth(); x++) {
					for (int y = 0; y < _gameState.getHeight(); y++) {
						if (_gameState.isAlive(y, x))
						g.fillRect(x * GRID_SIZE + 1, y * GRID_SIZE + 1, GRID_SIZE - 1, GRID_SIZE - 1);
					}
				}
			}
		}
	}
}
